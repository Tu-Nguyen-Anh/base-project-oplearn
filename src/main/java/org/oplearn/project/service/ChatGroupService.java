package org.oplearn.project.service;

import org.oplearn.project.entity.chat.ChatGroup;

import java.util.List;
import java.util.Optional;

public interface ChatGroupService {
    ChatGroup create(String name, String avatar);

    ChatGroup createDirect();

    Optional<ChatGroup> findDirectGroup(Long userId1, Long userId2);

    ChatGroup getById(Long id);

    List<ChatGroup> getGroupsByUserId(Long userId);

    ChatGroup rename(Long id, String newName);

    void delete(Long id);
}
