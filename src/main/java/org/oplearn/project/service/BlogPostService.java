package org.oplearn.project.service;

import org.oplearn.project.dto.request.BlogPostFilterRequest;
import org.oplearn.project.dto.request.BlogPostRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.blog.BlogPostFilterResponse;
import org.oplearn.project.entity.blog.BlogPost;

public interface BlogPostService {

    BlogPostFilterResponse create(Long userId, BlogPostRequest request);

    BlogPostFilterResponse update(Long postId, Long userId, BlogPostRequest request);

    void delete(Long postId, Long userId);

    BlogPostFilterResponse getDetail(Long postId, Long currentUserId);

    PageResponse<BlogPostFilterResponse> filter(BlogPostFilterRequest request);

    PageResponse<BlogPostFilterResponse> getByUserId(Long userId, int page, int size);

    BlogPost checkExistById(Long postId);

    long countByUserId(Long userId);
}
