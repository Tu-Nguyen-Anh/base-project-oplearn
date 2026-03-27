package org.oplearn.project.entity.chat;

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
@Table(name = "chat_group_members")
public class ChatGroupMember extends AuditEntity {
    private Long groupId;
    private Long userId;
    private String role;
    private Boolean deleted;
}
