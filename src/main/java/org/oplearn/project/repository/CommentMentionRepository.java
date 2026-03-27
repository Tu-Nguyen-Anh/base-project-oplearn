package org.oplearn.project.repository;

import org.oplearn.project.dto.response.article.CommentResponse;
import org.oplearn.project.entity.article.CommentMention;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentMentionRepository extends BaseRepository<CommentMention> {

    List<CommentMention> findByCommentId(Long commentId);

    @Query("""
            SELECT new org.oplearn.project.dto.response.article.CommentResponse$MentionedUserResponse(
                u.id,
                u.username,
                u.fullName
            )
            FROM CommentMention m
            LEFT JOIN User u ON m.mentionedUserId = u.id
            WHERE m.commentId = :commentId
            """)
    List<CommentResponse.MentionedUserResponse> findMentionedUsersByCommentId(@Param("commentId") Long commentId);
}
