package org.oplearn.project.entity.chat;

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
@Table(name = "chat_messages")
public class ChatMessage extends AuditEntity {

    private Long groupId;
    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String messageType;

    @Builder.Default
    private Boolean deleted = false;

    @Builder.Default
    @Column(name = "recalled", nullable = false)
    private Boolean recalled = false;
}