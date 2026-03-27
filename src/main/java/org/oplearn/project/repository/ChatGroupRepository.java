package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends BaseRepository<ChatGroup> {
    Optional<ChatGroup> findByIdAndDeletedFalse(Long id);

    @Query("""
            SELECT g FROM ChatGroup g
            INNER JOIN ChatGroupMember m ON m.groupId = g.id AND m.deleted = false
            WHERE m.userId = :userId AND g.deleted = false
            ORDER BY g.createdAt DESC
            """)
    List<ChatGroup> findAllGroupsByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT g FROM ChatGroup g
            WHERE g.isDirect = true AND g.deleted = false
            AND EXISTS (SELECT m FROM ChatGroupMember m WHERE m.groupId = g.id AND m.userId = :userId1 AND m.deleted = false)
            AND EXISTS (SELECT m FROM ChatGroupMember m WHERE m.groupId = g.id AND m.userId = :userId2 AND m.deleted = false)
            AND (SELECT COUNT(m) FROM ChatGroupMember m WHERE m.groupId = g.id AND m.deleted = false) = 2
            """)
    Optional<ChatGroup> findDirectGroup(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
