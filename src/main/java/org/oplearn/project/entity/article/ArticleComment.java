package org.oplearn.project.entity.article;

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
@Table(name = "article_comments")
public class ArticleComment extends AuditEntity {
    private Long articleId;
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean deleted;
}
