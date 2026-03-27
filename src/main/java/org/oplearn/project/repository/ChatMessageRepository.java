package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends BaseRepository<ChatMessage> {
    Page<ChatMessage> findAllByGroupIdAndDeletedFalseOrderByCreatedAtDesc(Long groupId, Pageable pageable);

    Optional<ChatMessage> findByIdAndDeletedFalse(Long id);

    @Query("SELECT m.id FROM ChatMessage m WHERE m.groupId = :groupId AND m.deleted = false")
    List<Long> findIdsByGroupId(@Param("groupId") Long groupId);

    @Modifying
    @Query("UPDATE ChatMessage m SET m.recalled = true WHERE m.id = :id")
    void recallById(@Param("id") Long id);
}
