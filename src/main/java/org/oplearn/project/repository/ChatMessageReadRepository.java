package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatMessageRead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatMessageReadRepository extends BaseRepository<ChatMessageRead> {
    List<ChatMessageRead> findAllByMessageId(Long messageId);

    boolean existsByMessageIdAndUserId(Long messageId, Long userId);

    List<ChatMessageRead> findAllByMessageIdIn(List<Long> messageIds);

    @Query("SELECT r.messageId FROM ChatMessageRead r WHERE r.messageId IN :messageIds AND r.userId = :userId")
    Set<Long> findAlreadyReadMessageIds(@Param("messageIds") List<Long> messageIds, @Param("userId") Long userId);
}
