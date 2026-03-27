package org.oplearn.project.service;

import org.oplearn.project.dto.request.BlogPostShareRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;

public interface BlogPostShareService {
    void share(Long postId, Long userId, BlogPostShareRequest request);
    PageResponse<BlogPostFilterResponse> getSharedByUserId(Long userId, int page, int size);
}
