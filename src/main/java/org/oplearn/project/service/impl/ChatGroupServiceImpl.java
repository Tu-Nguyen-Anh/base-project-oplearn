package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.chat.ChatGroup;
import org.oplearn.project.exception.base.chat.ChatGroupNotFoundException;
import org.oplearn.project.repository.ChatGroupRepository;
import org.oplearn.project.service.ChatGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGroupServiceImpl implements ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;

    @Transactional
    @Override
    public ChatGroup create(String name, String avatar) {
        ChatGroup group = ChatGroup.builder()
                .name(name)
                .avatar(avatar)
                .deleted(false)
                .isDirect(false)
                .build();
        return chatGroupRepository.save(group);
    }

    @Transactional
    @Override
    public ChatGroup createDirect() {
        ChatGroup group = ChatGroup.builder()
                .deleted(false)
                .isDirect(true)
                .build();
        return chatGroupRepository.save(group);
    }

    @Override
    public Optional<ChatGroup> findDirectGroup(Long userId1, Long userId2) {
        return chatGroupRepository.findDirectGroup(userId1, userId2);
    }

    @Override
    public ChatGroup getById(Long id) {
        return chatGroupRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(ChatGroupNotFoundException::new);
    }

    @Override
    public List<ChatGroup> getGroupsByUserId(Long userId) {
        return chatGroupRepository.findAllGroupsByUserId(userId);
    }

    @Transactional
    @Override
    public ChatGroup rename(Long id, String newName) {
        ChatGroup group = this.getById(id);
        group.setName(newName);
        return chatGroupRepository.save(group);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        ChatGroup group = this.getById(id);
        group.setDeleted(true);
        chatGroupRepository.save(group);
    }
}
