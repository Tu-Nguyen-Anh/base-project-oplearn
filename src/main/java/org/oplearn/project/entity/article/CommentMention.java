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
@Table(name = "comment_mentions")
public class CommentMention extends AuditEntity {
    private Long commentId;
    private Long mentionedUserId;
}
