package org.oplearn.project.entity.blog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.base.AuditEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "blog_post_comments")
public class BlogPostComment extends AuditEntity {
    private Long postId;
    private Long userId;
    private Long parentCommentId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean deleted;
}
