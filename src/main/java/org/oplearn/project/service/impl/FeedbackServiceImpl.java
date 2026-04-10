package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.feedback.CreateFeedbackRequest;
import org.oplearn.project.dto.request.feedback.UpdateFeedbackStatusRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.feedback.FeedbackImageResponse;
import org.oplearn.project.dto.response.feedback.FeedbackResponse;
import org.oplearn.project.entity.feedback.Feedback;
import org.oplearn.project.entity.feedback.FeedbackImage;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.feedback.FeedbackNotFoundException;
import org.oplearn.project.exception.base.feedback.FeedbackNotOwnerException;
import org.oplearn.project.repository.FeedbackImageRepository;
import org.oplearn.project.repository.FeedbackRepository;
import org.oplearn.project.service.FeedbackService;
import org.oplearn.project.service.StorageService;
import org.oplearn.project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.oplearn.project.constanst.OpLearnConstants.FeedbackStatus;
import static org.oplearn.project.constanst.OpLearnConstants.StorageConstants.FEEDBACK_IMAGE_FOLDER;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackImageRepository feedbackImageRepository;
    private final UserService userService;
    private final StorageService storageService;

    @Transactional
    @Override
    public FeedbackResponse create(CreateFeedbackRequest request, Long userId) {
        Feedback feedback = Feedback.builder()
                .userId(userId)
                .title(request.getTitle())
                .content(request.getContent())
                .status(FeedbackStatus.PENDING)
                .deleted(false)
                .build();
        feedback = feedbackRepository.save(feedback);

        List<FeedbackImage> images = buildImages(request.getImageUrls(), feedback.getId());
        if (!images.isEmpty()) {
            feedbackImageRepository.saveAll(images);
        }

        User user = userService.getById(userId);
        return toResponse(feedback, user, images);
    }

    @Override
    public FeedbackResponse getById(Long feedbackId, Long userId) {
        Feedback feedback = feedbackRepository.findByIdAndDeletedFalse(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        if (!feedback.getUserId().equals(userId)) {
            throw new FeedbackNotOwnerException();
        }
        User user = userService.getById(userId);
        List<FeedbackImage> images = feedbackImageRepository.findAllByFeedbackId(feedbackId);
        return toResponse(feedback, user, images);
    }

    @Override
    public PageResponse<FeedbackResponse> getMyFeedbacks(Long userId, int page, int size) {
        Page<Feedback> feedbackPage = feedbackRepository
                .findAllByUserIdAndDeletedFalseOrderByCreatedAtDesc(userId, PageRequest.of(page, size));

        User user = userService.getById(userId);
        List<Feedback> feedbacks = feedbackPage.getContent();
        Map<Long, List<FeedbackImage>> imageMap = batchLoadImages(feedbacks);

        List<FeedbackResponse> responses = feedbacks.stream()
                .map(f -> toResponse(f, user, imageMap.getOrDefault(f.getId(), List.of())))
                .toList();

        return PageResponse.of(responses, (int) feedbackPage.getTotalElements());
    }

    @Transactional
    @Override
    public void delete(Long feedbackId, Long userId) {
        Feedback feedback = feedbackRepository.findByIdAndDeletedFalse(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        if (!feedback.getUserId().equals(userId)) {
            throw new FeedbackNotOwnerException();
        }

        // Xóa file trên RustFS
        feedbackImageRepository.findAllByFeedbackId(feedbackId)
                .forEach(img -> {
                    if (img.getStorageKey() != null) {
                        storageService.delete(img.getStorageKey());
                    }
                });
        feedbackImageRepository.deleteAllByFeedbackId(feedbackId);

        feedback.setDeleted(true);
        feedbackRepository.save(feedback);
        log.info("(delete) Feedback {} deleted by userId {}", feedbackId, userId);
    }

    // ─── Admin ───────────────────────────────────────────────────────────────

    @Override
    public PageResponse<FeedbackResponse> getAllFeedbacks(Integer status, int page, int size) {
        Page<Feedback> feedbackPage = (status != null)
                ? feedbackRepository.findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(status, PageRequest.of(page, size))
                : feedbackRepository.findAllByDeletedFalseOrderByCreatedAtDesc(PageRequest.of(page, size));

        List<Feedback> feedbacks = feedbackPage.getContent();
        Set<Long> userIds = feedbacks.stream().map(Feedback::getUserId).collect(Collectors.toSet());
        Map<Long, User> userMap = userService.getByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
        Map<Long, List<FeedbackImage>> imageMap = batchLoadImages(feedbacks);

        List<FeedbackResponse> responses = feedbacks.stream()
                .map(f -> toResponse(f, userMap.get(f.getUserId()), imageMap.getOrDefault(f.getId(), List.of())))
                .toList();

        return PageResponse.of(responses, (int) feedbackPage.getTotalElements());
    }

    @Transactional
    @Override
    public FeedbackResponse updateStatus(Long feedbackId, UpdateFeedbackStatusRequest request) {
        Feedback feedback = feedbackRepository.findByIdAndDeletedFalse(feedbackId)
                .orElseThrow(FeedbackNotFoundException::new);
        feedback.setStatus(request.getStatus());
        feedback = feedbackRepository.save(feedback);

        User user = userService.getById(feedback.getUserId());
        List<FeedbackImage> images = feedbackImageRepository.findAllByFeedbackId(feedbackId);
        log.info("(updateStatus) Feedback {} → status {}", feedbackId, request.getStatus());
        return toResponse(feedback, user, images);
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private List<FeedbackImage> buildImages(List<String> imageUrls, Long feedbackId) {
        if (CollectionUtils.isEmpty(imageUrls)) return Collections.emptyList();
        long now = System.currentTimeMillis();
        return imageUrls.stream()
                .map(url -> {
                    String key = extractKeyQuietly(url);
                    return FeedbackImage.builder()
                            .feedbackId(feedbackId)
                            .imageUrl(url)
                            .storageKey(key)
                            .uploadedAt(now)
                            .build();
                })
                .toList();
    }

    private String extractKeyQuietly(String url) {
        try {
            return storageService.extractKey(url);
        } catch (Exception e) {
            log.warn("(extractKey) Cannot extract key from url: {}", url);
            return null;
        }
    }

    private Map<Long, List<FeedbackImage>> batchLoadImages(List<Feedback> feedbacks) {
        if (feedbacks.isEmpty()) return Map.of();
        List<Long> ids = feedbacks.stream().map(Feedback::getId).toList();
        return feedbackImageRepository.findAllByFeedbackIdIn(ids)
                .stream()
                .collect(Collectors.groupingBy(FeedbackImage::getFeedbackId));
    }

    private FeedbackResponse toResponse(Feedback feedback, User user, List<FeedbackImage> images) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .userId(feedback.getUserId())
                .userFullName(user != null ? user.getFullName() : null)
                .userAvatar(user != null ? user.getAvatar() : null)
                .title(feedback.getTitle())
                .content(feedback.getContent())
                .status(feedback.getStatus())
                .statusLabel(FeedbackStatus.label(feedback.getStatus()))
                .createdAt(feedback.getCreatedAt())
                .updatedAt(feedback.getLastUpdatedAt())
                .images(images.stream()
                        .map(img -> FeedbackImageResponse.builder()
                                .id(img.getId())
                                .imageUrl(img.getImageUrl())
                                .originalName(img.getOriginalName())
                                .uploadedAt(img.getUploadedAt())
                                .build())
                        .toList())
                .build();
    }
}
