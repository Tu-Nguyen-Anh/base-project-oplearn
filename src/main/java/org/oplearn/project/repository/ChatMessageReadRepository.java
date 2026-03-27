package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatMessageRead;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageReadRepository extends BaseRepository<ChatMessageRead> {
    List<ChatMessageRead> findAllByMessageId(Long messageId);

    boolean existsByMessageIdAndUserId(Long messageId, Long userId);

    List<ChatMessageRead> findAllByMessageIdIn(List<Long> messageIds);
}
