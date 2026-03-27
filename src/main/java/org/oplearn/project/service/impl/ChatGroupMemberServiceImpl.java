package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.chat.ChatGroupMember;
import org.oplearn.project.exception.base.chat.MemberAlreadyExistsException;
import org.oplearn.project.exception.base.chat.NotGroupMemberException;
import org.oplearn.project.repository.ChatGroupMemberRepository;
import org.oplearn.project.service.ChatGroupMemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.ROLE_ADMIN;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGroupMemberServiceImpl implements ChatGroupMemberService {

    private final ChatGroupMemberRepository chatGroupMemberRepository;

    @Transactional
    @Override
    public ChatGroupMember addMember(Long groupId, Long userId, String role) {
        if (chatGroupMemberRepository.existsByGroupIdAndUserIdAndDeletedFalse(groupId, userId)) {
            throw new MemberAlreadyExistsException();
        }
        ChatGroupMember member = ChatGroupMember.builder()
                .groupId(groupId)
                .userId(userId)
                .role(role)
                .deleted(false)
                .build();
        return chatGroupMemberRepository.save(member);
    }

    @Transactional
    @Override
    public void removeMember(Long groupId, Long userId) {
        ChatGroupMember member = chatGroupMemberRepository
                .findByGroupIdAndUserIdAndDeletedFalse(groupId, userId)
                .orElseThrow(NotGroupMemberException::new);
        member.setDeleted(true);
        chatGroupMemberRepository.save(member);
    }

    @Transactional
    @Override
    public void makeAdmin(Long groupId, Long userId) {
        ChatGroupMember member = chatGroupMemberRepository
                .findByGroupIdAndUserIdAndDeletedFalse(groupId, userId)
                .orElseThrow(NotGroupMemberException::new);
        member.setRole(ROLE_ADMIN);
        chatGroupMemberRepository.save(member);
    }

    @Override
    public List<ChatGroupMember> getMembersByGroupId(Long groupId) {
        return chatGroupMemberRepository.findAllByGroupIdAndDeletedFalse(groupId);
    }

    @Override
    public ChatGroupMember getMember(Long groupId, Long userId) {
        return chatGroupMemberRepository
                .findByGroupIdAndUserIdAndDeletedFalse(groupId, userId)
                .orElseThrow(NotGroupMemberException::new);
    }

    @Override
    public boolean isMember(Long groupId, Long userId) {
        return chatGroupMemberRepository.existsByGroupIdAndUserIdAndDeletedFalse(groupId, userId);
    }

    @Override
    public boolean isAdmin(Long groupId, Long userId) {
        return chatGroupMemberRepository
                .findByGroupIdAndUserIdAndDeletedFalse(groupId, userId)
                .map(m -> ROLE_ADMIN.equals(m.getRole()))
                .orElse(false);
    }

    @Override
    public int countMembers(Long groupId) {
        return chatGroupMemberRepository.countByGroupIdAndDeletedFalse(groupId);
    }

    @Override
    public int countAdmins(Long groupId) {
        return chatGroupMemberRepository.countByGroupIdAndRoleAndDeletedFalse(groupId, ROLE_ADMIN);
    }
}
