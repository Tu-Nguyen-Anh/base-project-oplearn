package org.oplearn.project.entity.article;

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
@Table(name = "user_favorite_articles")
public class UserFavoriteArticle extends AuditEntity {
    private Long userId;
    private Long articleId;
    private Boolean deleted;
}
