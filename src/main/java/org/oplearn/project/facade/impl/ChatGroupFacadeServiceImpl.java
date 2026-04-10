package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.chat.AddMemberRequest;
import org.oplearn.project.dto.request.chat.AddReactionRequest;
import org.oplearn.project.dto.request.chat.CreateGroupRequest;
import org.oplearn.project.dto.request.chat.RenameGroupRequest;
import org.oplearn.project.dto.request.chat.SendMessageRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse;
import org.oplearn.project.dto.response.chat.ChatMessageResponse;
import org.oplearn.project.dto.response.chat.GroupDetailResponse;
import org.oplearn.project.dto.response.chat.GroupMemberResponse;
import org.oplearn.project.dto.response.chat.GroupResponse;
import org.oplearn.project.dto.response.chat.ReactionEvent;
import org.oplearn.project.dto.response.chat.ReactionResponse;
import org.oplearn.project.dto.response.chat.ReadReceiptEvent;
import org.oplearn.project.dto.response.chat.ReaderResponse;
import org.oplearn.project.dto.response.chat.RecallEvent;
import org.oplearn.project.entity.chat.ChatGroup;
import org.oplearn.project.entity.chat.ChatGroupMember;
import org.oplearn.project.entity.chat.ChatMessage;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.chat.CannotLeaveGroupException;
import org.oplearn.project.exception.base.chat.NotGroupAdminException;
import org.oplearn.project.exception.base.chat.NotGroupMemberException;
import org.oplearn.project.facade.ChatGroupFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.ChatGroupHistoryService;
import org.oplearn.project.service.ChatGroupMemberService;
import org.oplearn.project.service.ChatGroupService;
import org.oplearn.project.service.ChatMessageReactionService;
import org.oplearn.project.service.ChatMessageReadService;
import org.oplearn.project.service.ChatMessageService;
import org.oplearn.project.service.PresenceService;
import org.oplearn.project.service.UserService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.CHAT_TOPIC;
import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.ROLE_ADMIN;
import static org.oplearn.project.constanst.OpLearnConstants.ChatConstants.ROLE_MEMBER;
import static org.oplearn.project.constanst.OpLearnConstants.ChatHistoryAction.ADD_MEMBER;
import static org.oplearn.project.constanst.OpLearnConstants.ChatHistoryAction.CREATE_GROUP;
import static org.oplearn.project.constanst.OpLearnConstants.ChatHistoryAction.LEAVE_GROUP;
import static org.oplearn.project.constanst.OpLearnConstants.ChatHistoryAction.REMOVE_MEMBER;
import static org.oplearn.project.constanst.OpLearnConstants.ChatHistoryAction.RENAME_GROUP;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.REACTION_ADD;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.REACTION_REMOVE;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.REACTION_TOPIC;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.READ_RECEIPT_TOPIC;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.RECALL_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGroupFacadeServiceImpl implements ChatGroupFacadeService {

    private final ChatGroupService chatGroupService;
    private final ChatGroupMemberService chatGroupMemberService;
    private final ChatMessageService chatMessageService;
    private final ChatGroupHistoryService chatGroupHistoryService;
    private final ChatMessageReadService chatMessageReadService;
    private final ChatMessageReactionService chatMessageReactionService;
    private final PresenceService presenceService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @Override
    public GroupResponse createGroup(CreateGroupRequest request) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        ChatGroup group = chatGroupService.create(request.getName(), request.getAvatar());
        chatGroupMemberService.addMember(group.getId(), currentUser.getId(), ROLE_ADMIN);

        for (Long memberId : request.getMemberIds()) {
            if (!memberId.equals(currentUser.getId())) {
                userService.checkExistById(memberId);
                chatGroupMemberService.addMember(group.getId(), memberId, ROLE_MEMBER);
            }
        }

        chatGroupHistoryService.saveHistory(group.getId(), CREATE_GROUP,
                String.format("%s đã tạo nhóm \"%s\"", currentUser.getFullName(), group.getName()));

        int memberCount = chatGroupMemberService.countMembers(group.getId());
        return mapToGroupResponse(group, ROLE_ADMIN, memberCount);
    }

    @Transactional
    @Override
    public GroupResponse getOrCreateDirectMessage(Long targetUserId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        userService.checkExistById(targetUserId);

        return chatGroupService.findDirectGroup(currentUser.getId(), targetUserId)
                .map(existing -> {
                    int memberCount = chatGroupMemberService.countMembers(existing.getId());
                    return mapToGroupResponse(existing, ROLE_MEMBER, memberCount);
                })
                .orElseGet(() -> {
                    ChatGroup group = chatGroupService.createDirect();
                    chatGroupMemberService.addMember(group.getId(), currentUser.getId(), ROLE_ADMIN);
                    chatGroupMemberService.addMember(group.getId(), targetUserId, ROLE_ADMIN);
                    return mapToGroupResponse(group, ROLE_ADMIN, 2);
                });
    }

    @Transactional
    @Override
    public void leaveGroup(Long groupId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        int totalMembers = chatGroupMemberService.countMembers(groupId);
        boolean isAdmin = chatGroupMemberService.isAdmin(groupId, currentUser.getId());
        int adminCount = chatGroupMemberService.countAdmins(groupId);

        if (isAdmin && adminCount == 1 && totalMembers > 1) {
            throw new CannotLeaveGroupException();
        }

        chatGroupMemberService.removeMember(groupId, currentUser.getId());
        chatGroupHistoryService.saveHistory(groupId, LEAVE_GROUP,
                String.format("%s đã rời khỏi nhóm", currentUser.getFullName()));

        if (totalMembers == 1) {
            chatGroupService.delete(groupId);
        }
    }

    /**
     * Tối ưu: thay vì N*2 queries (isAdmin + countMembers mỗi group),
     * dùng 1 batch query lấy tất cả members rồi xử lý trong memory.
     */
    @Override
    public List<GroupResponse> getMyGroups() {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        List<ChatGroup> groups = chatGroupService.getGroupsByUserId(currentUser.getId());
        if (groups.isEmpty()) return List.of();

        List<Long> groupIds = groups.stream().map(ChatGroup::getId).toList();
        Map<Long, List<ChatGroupMember>> membersByGroup = chatGroupMemberService.getMembersByGroupIds(groupIds);

        return groups.stream().map(group -> {
            List<ChatGroupMember> members = membersByGroup.getOrDefault(group.getId(), List.of());
            String myRole = members.stream()
                    .filter(m -> m.getUserId().equals(currentUser.getId()))
                    .findFirst()
                    .map(ChatGroupMember::getRole)
                    .orElse(ROLE_MEMBER);
            return mapToGroupResponse(group, myRole, members.size());
        }).toList();
    }

    /**
     * Tối ưu: thay vì N queries getById per member,
     * dùng 1 batch query getByIds rồi map trong memory.
     */
    @Override
    public GroupDetailResponse getGroupDetail(Long groupId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        ChatGroup group = chatGroupService.getById(groupId);
        String myRole = chatGroupMemberService.isAdmin(groupId, currentUser.getId()) ? ROLE_ADMIN : ROLE_MEMBER;

        List<ChatGroupMember> members = chatGroupMemberService.getMembersByGroupId(groupId);
        List<Long> memberUserIds = members.stream().map(ChatGroupMember::getUserId).toList();

        // Batch load users — 1 DB query
        Map<Long, User> userMap = userService.getByIds(new HashSet<>(memberUserIds)).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // Batch check online — 1 Redis pipeline
        Set<Long> onlineIds = presenceService.getOnlineUserIds(memberUserIds);

        List<GroupMemberResponse> memberResponses = members.stream()
                .map(m -> {
                    User user = userMap.get(m.getUserId());
                    boolean online = onlineIds.contains(user.getId());
                    return GroupMemberResponse.builder()
                            .memberId(m.getId())
                            .userId(user.getId())
                            .username(user.getUsername())
                            .fullName(user.getFullName())
                            .avatar(user.getAvatar())
                            .role(m.getRole())
                            .online(online)
                            .lastSeen(online ? null : presenceService.getLastSeen(user.getId()))
                            .build();
                })
                .toList();

        return GroupDetailResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .avatar(group.getAvatar())
                .myRole(myRole)
                .createdAt(group.getCreatedAt())
                .members(memberResponses)
                .build();
    }

    @Transactional
    @Override
    public GroupResponse renameGroup(Long groupId, RenameGroupRequest request) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isAdmin(groupId, currentUser.getId())) {
            throw new NotGroupAdminException();
        }

        ChatGroup group = chatGroupService.getById(groupId);
        String oldName = group.getName();
        group = chatGroupService.rename(groupId, request.getName());

        chatGroupHistoryService.saveHistory(groupId, RENAME_GROUP,
                String.format("%s đã đổi tên nhóm từ \"%s\" thành \"%s\"",
                        currentUser.getFullName(), oldName, request.getName()));

        int memberCount = chatGroupMemberService.countMembers(groupId);
        return mapToGroupResponse(group, ROLE_ADMIN, memberCount);
    }

    @Transactional
    @Override
    public GroupMemberResponse addMember(Long groupId, AddMemberRequest request) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isAdmin(groupId, currentUser.getId())) {
            throw new NotGroupAdminException();
        }

        chatGroupService.getById(groupId);
        User newUser = userService.checkExistById(request.getUserId());
        ChatGroupMember member = chatGroupMemberService.addMember(groupId, request.getUserId(), ROLE_MEMBER);

        chatGroupHistoryService.saveHistory(groupId, ADD_MEMBER,
                String.format("%s đã thêm %s vào nhóm", currentUser.getFullName(), newUser.getFullName()));

        boolean online = presenceService.isOnline(newUser.getId());
        return mapToMemberResponse(member, newUser, online);
    }

    @Transactional
    @Override
    public void removeMember(Long groupId, Long userId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isAdmin(groupId, currentUser.getId())) {
            throw new NotGroupAdminException();
        }

        User removedUser = userService.getById(userId);
        chatGroupMemberService.removeMember(groupId, userId);

        chatGroupHistoryService.saveHistory(groupId, REMOVE_MEMBER,
                String.format("%s đã xóa %s khỏi nhóm", currentUser.getFullName(), removedUser.getFullName()));
    }

    @Transactional
    @Override
    public void makeAdmin(Long groupId, Long userId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isAdmin(groupId, currentUser.getId())) {
            throw new NotGroupAdminException();
        }
        chatGroupMemberService.makeAdmin(groupId, userId);
    }

    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        ChatGroup group = chatGroupService.getById(groupId);

        boolean isDirect = Boolean.TRUE.equals(group.getIsDirect());
        if (isDirect) {
            if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
                throw new NotGroupMemberException();
            }
        } else {
            if (!chatGroupMemberService.isAdmin(groupId, currentUser.getId())) {
                throw new NotGroupAdminException();
            }
        }
        chatGroupService.delete(groupId);
    }

    @Transactional
    @Override
    public ChatMessageResponse sendMessage(Long groupId, SendMessageRequest request) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        ChatMessage message = chatMessageService.save(
                groupId, currentUser.getId(), request.getContent(), request.getMessageType());

        ChatMessageResponse response = buildMessageResponse(message, currentUser, List.of(), List.of());
        messagingTemplate.convertAndSend(CHAT_TOPIC + groupId, response);
        return response;
    }

    @Transactional
    @Override
    public ChatMessageResponse recallMessage(Long groupId, Long messageId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        ChatMessage message = chatMessageService.recallMessage(messageId, currentUser.getId());

        RecallEvent event = RecallEvent.builder()
                .messageId(messageId)
                .groupId(groupId)
                .recalledBy(currentUser.getId())
                .recalledAt(System.currentTimeMillis())
                .build();
        messagingTemplate.convertAndSend(String.format(RECALL_TOPIC, groupId), event);

        return buildMessageResponse(message, currentUser, List.of(), List.of());
    }

    @Override
    public PageResponse<ChatMessageResponse> getMessages(Long groupId, int page, int size) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        var messagePage = chatMessageService.getMessages(groupId, page, size);
        List<ChatMessage> messages = messagePage.getContent();
        List<Long> messageIds = messages.stream().map(ChatMessage::getId).toList();

        Map<Long, List<ReaderResponse>> readersMap = chatMessageReadService.getReadersForMessages(messageIds);
        Map<Long, List<ReactionResponse>> reactionsMap =
                chatMessageReactionService.getReactionsForMessages(messageIds, currentUser.getId());

        Set<Long> senderIds = messages.stream().map(ChatMessage::getSenderId).collect(Collectors.toSet());
        Map<Long, User> senderMap = userService.getByIds(senderIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        List<ChatMessageResponse> responses = messages.stream()
                .map(msg -> buildMessageResponse(msg, senderMap.get(msg.getSenderId()),
                        readersMap.getOrDefault(msg.getId(), List.of()),
                        reactionsMap.getOrDefault(msg.getId(), List.of())))
                .toList();

        return PageResponse.of(responses, (int) messagePage.getTotalElements());
    }

    @Override
    public PageResponse<ChatGroupHistoryResponse> getHistory(Long groupId, int page, int size) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }
        return chatGroupHistoryService.getHistory(groupId, page, size);
    }

    @Transactional
    @Override
    public void markMessagesAsRead(Long groupId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        var lastMessageId = chatMessageReadService.markAllAsRead(groupId, currentUser.getId());
        if (lastMessageId.isEmpty()) return;

        ReadReceiptEvent event = ReadReceiptEvent.builder()
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .fullName(currentUser.getFullName())
                .avatar(currentUser.getAvatar())
                .lastReadMessageId(lastMessageId.get())
                .readAt(System.currentTimeMillis())
                .build();

        messagingTemplate.convertAndSend(String.format(READ_RECEIPT_TOPIC, groupId), event);
    }

    @Override
    public List<ReaderResponse> getMessageReaders(Long groupId, Long messageId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }
        ChatMessage message = chatMessageService.getById(messageId);
        if (!message.getGroupId().equals(groupId)) {
            throw new NotGroupMemberException();
        }
        return chatMessageReadService.getReaders(messageId);
    }

    @Transactional
    @Override
    public List<ReactionResponse> addReaction(Long groupId, Long messageId, AddReactionRequest request) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        ChatMessage message = chatMessageService.getById(messageId);
        if (!message.getGroupId().equals(groupId)) {
            throw new NotGroupMemberException();
        }

        List<ReactionResponse> reactions = chatMessageReactionService.addReaction(
                messageId, currentUser.getId(), request.getEmoji());

        ReactionEvent event = ReactionEvent.builder()
                .type(REACTION_ADD)
                .messageId(messageId)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .avatar(currentUser.getAvatar())
                .emoji(request.getEmoji())
                .reactions(reactions)
                .build();

        messagingTemplate.convertAndSend(String.format(REACTION_TOPIC, groupId), event);
        return reactions;
    }

    @Transactional
    @Override
    public List<ReactionResponse> removeReaction(Long groupId, Long messageId, String emoji) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        ChatMessage message = chatMessageService.getById(messageId);
        if (!message.getGroupId().equals(groupId)) {
            throw new NotGroupMemberException();
        }

        List<ReactionResponse> reactions = chatMessageReactionService.removeReaction(
                messageId, currentUser.getId(), emoji);

        ReactionEvent event = ReactionEvent.builder()
                .type(REACTION_REMOVE)
                .messageId(messageId)
                .userId(currentUser.getId())
                .username(currentUser.getUsername())
                .avatar(currentUser.getAvatar())
                .emoji(emoji)
                .reactions(reactions)
                .build();

        messagingTemplate.convertAndSend(String.format(REACTION_TOPIC, groupId), event);
        return reactions;
    }

    /**
     * Tối ưu: batch load users và batch check online status.
     */
    @Override
    public List<GroupMemberResponse> getOnlineMembers(Long groupId) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!chatGroupMemberService.isMember(groupId, currentUser.getId())) {
            throw new NotGroupMemberException();
        }

        List<ChatGroupMember> members = chatGroupMemberService.getMembersByGroupId(groupId);
        List<Long> memberUserIds = members.stream().map(ChatGroupMember::getUserId).toList();

        // Batch load users
        Map<Long, User> userMap = userService.getByIds(new HashSet<>(memberUserIds)).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // Batch check online status — 1 Redis pipeline
        Set<Long> onlineIds = presenceService.getOnlineUserIds(memberUserIds);

        return members.stream()
                .map(m -> mapToMemberResponse(m, userMap.get(m.getUserId()), onlineIds.contains(m.getUserId())))
                .toList();
    }

    @Override
    public void heartbeat() {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        presenceService.setOnline(currentUser.getId());
        log.debug("(heartbeat) userId: {}", currentUser.getId());
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private GroupResponse mapToGroupResponse(ChatGroup group, String myRole, int memberCount) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .avatar(group.getAvatar())
                .myRole(myRole)
                .memberCount(memberCount)
                .isDirect(group.getIsDirect())
                .createdAt(group.getCreatedAt())
                .build();
    }

    private GroupMemberResponse mapToMemberResponse(ChatGroupMember member, User user, boolean online) {
        return GroupMemberResponse.builder()
                .memberId(member.getId())
                .userId(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .avatar(user.getAvatar())
                .role(member.getRole())
                .online(online)
                .lastSeen(online ? null : presenceService.getLastSeen(user.getId()))
                .build();
    }

    private ChatMessageResponse buildMessageResponse(ChatMessage message, User sender,
                                                      List<ReaderResponse> readers,
                                                      List<ReactionResponse> reactions) {
        boolean recalled = Boolean.TRUE.equals(message.getRecalled());
        return ChatMessageResponse.builder()
                .id(message.getId())
                .groupId(message.getGroupId())
                .senderId(sender.getId())
                .senderUsername(sender.getUsername())
                .senderFullName(sender.getFullName())
                .senderAvatar(sender.getAvatar())
                .content(recalled ? "Tin nhắn đã bị thu hồi" : message.getContent())
                .messageType(recalled ? null : message.getMessageType())
                .recalled(recalled)
                .createdAt(message.getCreatedAt())
                .readers(recalled || readers.isEmpty() ? null : readers)
                .reactions(recalled || reactions.isEmpty() ? null : reactions)
                .build();
    }
}
