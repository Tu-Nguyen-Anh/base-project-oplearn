package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.BlogPostShareRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.entity.blog.BlogPostShare;
import org.oplearn.project.repository.BlogPostShareRepository;
import org.oplearn.project.service.BlogPostShareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostShareServiceImpl implements BlogPostShareService {

    private final BlogPostShareRepository shareRepository;

    @Transactional
    @Override
    public void share(Long postId, Long userId, BlogPostShareRequest request) {
        log.debug("(share) postId: {}, userId: {}", postId, userId);

        shareRepository.save(BlogPostShare.builder()
                .postId(postId)
                .userId(userId)
                .content(request != null ? request.getContent() : null)
                .build());
    }

    @Override
    public PageResponse<BlogPostFilterResponse> getSharedByUserId(Long userId, int page, int size) {
        log.debug("(getSharedByUserId) userId: {}", userId);

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPostFilterResponse> result = shareRepository.findSharedByUserId(userId, pageable);
        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }
}
