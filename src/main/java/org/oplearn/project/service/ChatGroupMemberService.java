package org.oplearn.project.service;

import org.oplearn.project.entity.chat.ChatGroupMember;

import java.util.List;

public interface ChatGroupMemberService {
    ChatGroupMember addMember(Long groupId, Long userId, String role);

    void removeMember(Long groupId, Long userId);

    void makeAdmin(Long groupId, Long userId);

    List<ChatGroupMember> getMembersByGroupId(Long groupId);

    ChatGroupMember getMember(Long groupId, Long userId);

    boolean isMember(Long groupId, Long userId);

    boolean isAdmin(Long groupId, Long userId);

    int countMembers(Long groupId);

    int countAdmins(Long groupId);
}
