package org.oplearn.project.entity.notification;

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
@Table(name = "notifications")
public class Notification extends AuditEntity {
    private Long recipientUserId;
    private Long senderUserId;
    private String senderFullName;
    private String type;
    private Long articleId;
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Builder.Default
    private Boolean isRead = false;
}
