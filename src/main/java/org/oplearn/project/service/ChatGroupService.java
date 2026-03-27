package org.oplearn.project.service;

import org.oplearn.project.entity.chat.ChatGroup;

import java.util.List;

public interface ChatGroupService {
    ChatGroup create(String name, String avatar);

    ChatGroup getById(Long id);

    List<ChatGroup> getGroupsByUserId(Long userId);

    ChatGroup rename(Long id, String newName);

    void delete(Long id);
}
