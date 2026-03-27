package org.oplearn.project.facade;

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
import org.oplearn.project.dto.response.chat.ReactionResponse;
import org.oplearn.project.dto.response.chat.ReaderResponse;

import java.util.List;

public interface ChatGroupFacadeService {
    GroupResponse createGroup(CreateGroupRequest request);

    GroupResponse getOrCreateDirectMessage(Long targetUserId);

    void leaveGroup(Long groupId);

    List<GroupResponse> getMyGroups();

    GroupDetailResponse getGroupDetail(Long groupId);

    GroupResponse renameGroup(Long groupId, RenameGroupRequest request);

    GroupMemberResponse addMember(Long groupId, AddMemberRequest request);

    void removeMember(Long groupId, Long userId);

    void makeAdmin(Long groupId, Long userId);

    void deleteGroup(Long groupId);

    ChatMessageResponse sendMessage(Long groupId, SendMessageRequest request);

    ChatMessageResponse recallMessage(Long groupId, Long messageId);

    PageResponse<ChatMessageResponse> getMessages(Long groupId, int page, int size);

    PageResponse<ChatGroupHistoryResponse> getHistory(Long groupId, int page, int size);

    void markMessagesAsRead(Long groupId);

    List<ReaderResponse> getMessageReaders(Long groupId, Long messageId);

    List<ReactionResponse> addReaction(Long groupId, Long messageId, AddReactionRequest request);

    List<ReactionResponse> removeReaction(Long groupId, Long messageId, String emoji);

    List<GroupMemberResponse> getOnlineMembers(Long groupId);

    void heartbeat();
}
