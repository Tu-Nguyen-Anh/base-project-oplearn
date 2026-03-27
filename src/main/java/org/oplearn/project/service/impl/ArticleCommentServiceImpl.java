package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.CommentRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.entity.article.ArticleComment;
import org.oplearn.project.entity.article.CommentMention;
import org.oplearn.project.exception.base.ForbiddenException;
import org.oplearn.project.exception.base.article.CommentNotFoundException;
import org.oplearn.project.repository.ArticleCommentRepository;
import org.oplearn.project.repository.CommentMentionRepository;
import org.oplearn.project.service.ArticleCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleCommentServiceImpl implements ArticleCommentService {
    private final ArticleCommentRepository commentRepository;
    private final CommentMentionRepository mentionRepository;

    @Transactional
    @Override
    public CommentResponse create(Long articleId, Long userId, CommentRequest request) {
        log.info("=== Start create comment");
        log.debug("(create) articleId: {}, userId: {}", articleId, userId);

        ArticleComment comment = ArticleComment.builder()
                .articleId(articleId)
                .userId(userId)
                .content(request.getContent())
                .deleted(false)
                .build();

        comment = commentRepository.save(comment);

        if (request.getMentionedUserIds() != null && !request.getMentionedUserIds().isEmpty()) {
            final Long commentId = comment.getId();
            List<CommentMention> mentions = request.getMentionedUserIds().stream()
                    .distinct()
                    .filter(mentionedUserId -> !mentionedUserId.equals(userId))
                    .map(mentionedUserId -> CommentMention.builder()
                            .commentId(commentId)
                            .mentionedUserId(mentionedUserId)
                            .build())
                    .toList();
            mentionRepository.saveAll(mentions);
        }

        CommentResponse response = new CommentResponse(
                comment.getId(), comment.getArticleId(), comment.getUserId(),
                null, null, null, comment.getContent(), comment.getCreatedAt()
        );

        if (request.getMentionedUserIds() != null && !request.getMentionedUserIds().isEmpty()) {
            List<CommentResponse.MentionedUserResponse> mentionedUsers =
                    mentionRepository.findMentionedUsersByCommentId(comment.getId());
            response.setMentionedUsers(mentionedUsers);
        }

        return response;
    }

    @Transactional
    @Override
    public void delete(Long commentId, Long currentUserId) {
        log.info("=== Start delete comment");
        log.debug("(delete) commentId: {}, currentUserId: {}", commentId, currentUserId);

        ArticleComment comment = checkExistById(commentId);

        if (!comment.getUserId().equals(currentUserId)) {
            throw new ForbiddenException();
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
    }

    @Override
    public PageResponse<CommentResponse> getByArticleId(Long articleId, int page, int size) {
        log.info("=== Start getByArticleId");
        log.debug("(getByArticleId) articleId: {}, page: {}, size: {}", articleId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponse> result = commentRepository.findByArticleId(articleId, pageable);

        List<CommentResponse> enriched = result.getContent().stream()
                .peek(comment -> {
                    List<CommentResponse.MentionedUserResponse> mentions =
                            mentionRepository.findMentionedUsersByCommentId(comment.getId());
                    if (!mentions.isEmpty()) {
                        comment.setMentionedUsers(mentions);
                    }
                })
                .toList();

        return PageResponse.of(enriched, (int) result.getTotalElements());
    }

    @Override
    public ArticleComment checkExistById(Long commentId) {
        return commentRepository.findByIdAndDeletedFalse(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }
}
