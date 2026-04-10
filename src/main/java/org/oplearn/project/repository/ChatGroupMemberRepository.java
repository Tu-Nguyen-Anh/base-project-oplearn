package org.oplearn.project.repository;

import org.oplearn.project.entity.chat.ChatGroupMember;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupMemberRepository extends BaseRepository<ChatGroupMember> {
    Optional<ChatGroupMember> findByGroupIdAndUserIdAndDeletedFalse(Long groupId, Long userId);

    List<ChatGroupMember> findAllByGroupIdAndDeletedFalse(Long groupId);

    List<ChatGroupMember> findAllByGroupIdInAndDeletedFalse(Collection<Long> groupIds);

    boolean existsByGroupIdAndUserIdAndDeletedFalse(Long groupId, Long userId);

    int countByGroupIdAndDeletedFalse(Long groupId);

    int countByGroupIdAndRoleAndDeletedFalse(Long groupId, String role);
}
