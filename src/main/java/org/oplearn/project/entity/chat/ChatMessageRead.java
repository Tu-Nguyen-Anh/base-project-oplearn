package org.oplearn.project.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.base.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_message_reads")
public class ChatMessageRead extends BaseEntity {
    private Long messageId;
    private Long userId;
    private Long readAt;
}
