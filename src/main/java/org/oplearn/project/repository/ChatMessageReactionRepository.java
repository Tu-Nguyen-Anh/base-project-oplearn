package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatMessageReaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageReactionRepository extends BaseRepository<ChatMessageReaction> {
    List<ChatMessageReaction> findAllByMessageId(Long messageId);

    Optional<ChatMessageReaction> findByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);

    boolean existsByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);

    List<ChatMessageReaction> findAllByMessageIdIn(List<Long> messageIds);
}
