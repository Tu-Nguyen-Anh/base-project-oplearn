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
@Table(name = "blog_post_shares")
public class BlogPostShare extends AuditEntity {
    private Long postId;
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String content;
}
