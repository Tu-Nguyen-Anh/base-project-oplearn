package org.oplearn.project.controller.advice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.chat.AddMemberRequest;
import org.oplearn.project.dto.request.chat.AddReactionRequest;
import org.oplearn.project.dto.request.chat.CreateGroupRequest;
import org.oplearn.project.dto.request.chat.RenameGroupRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.chat.ChatGroupHistoryResponse;
import org.oplearn.project.dto.response.chat.ChatMessageResponse;
import org.oplearn.project.dto.response.chat.GroupDetailResponse;
import org.oplearn.project.dto.response.chat.GroupMemberResponse;
import org.oplearn.project.dto.response.chat.GroupResponse;
import org.oplearn.project.dto.response.chat.ReactionResponse;
import org.oplearn.project.dto.response.chat.ReaderResponse;
import org.oplearn.project.facade.ChatGroupFacadeService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;
import static org.oplearn.project.constanst.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constanst.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@RestController
@RequestMapping("/api/v1/chat/groups")
@RequiredArgsConstructor
@Slf4j
public class ChatGroupController {

    private final ChatGroupFacadeService chatGroupFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<GroupResponse> createGroup(
            @RequestBody @Valid CreateGroupRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(createGroup) request: {}", request.getName());
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.createGroup(request)
        );
    }

    @GetMapping
    public ResponseGeneral<List<GroupResponse>> getMyGroups(
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getMyGroups)");
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getMyGroups()
        );
    }

    @GetMapping("/{groupId}")
    public ResponseGeneral<GroupDetailResponse> getGroupDetail(
            @PathVariable Long groupId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getGroupDetail) groupId: {}", groupId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getGroupDetail(groupId)
        );
    }

    @PutMapping("/{groupId}/name")
    public ResponseGeneral<GroupResponse> renameGroup(
            @PathVariable Long groupId,
            @RequestBody @Valid RenameGroupRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(renameGroup) groupId: {}, newName: {}", groupId, request.getName());
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.renameGroup(groupId, request)
        );
    }

    @PostMapping("/{groupId}/members")
    public ResponseGeneral<GroupMemberResponse> addMember(
            @PathVariable Long groupId,
            @RequestBody @Valid AddMemberRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(addMember) groupId: {}, userId: {}", groupId, request.getUserId());
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.addMember(groupId, request)
        );
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseGeneral<Void> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(removeMember) groupId: {}, userId: {}", groupId, userId);
        chatGroupFacadeService.removeMember(groupId, userId);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @PutMapping("/{groupId}/members/{userId}/admin")
    public ResponseGeneral<Void> makeAdmin(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(makeAdmin) groupId: {}, userId: {}", groupId, userId);
        chatGroupFacadeService.makeAdmin(groupId, userId);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @DeleteMapping("/{groupId}")
    public ResponseGeneral<Void> deleteGroup(
            @PathVariable Long groupId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(deleteGroup) groupId: {}", groupId);
        chatGroupFacadeService.deleteGroup(groupId);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @PostMapping("/{groupId}/messages/read")
    public ResponseGeneral<Void> markAsRead(
            @PathVariable Long groupId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(markAsRead) groupId: {}", groupId);
        chatGroupFacadeService.markMessagesAsRead(groupId);
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @GetMapping("/{groupId}/messages/{messageId}/reads")
    public ResponseGeneral<List<ReaderResponse>> getMessageReaders(
            @PathVariable Long groupId,
            @PathVariable Long messageId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getMessageReaders) groupId: {}, messageId: {}", groupId, messageId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getMessageReaders(groupId, messageId)
        );
    }

    @PostMapping("/{groupId}/messages/{messageId}/reactions")
    public ResponseGeneral<List<ReactionResponse>> addReaction(
            @PathVariable Long groupId,
            @PathVariable Long messageId,
            @RequestBody @Valid AddReactionRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(addReaction) groupId: {}, messageId: {}, emoji: {}", groupId, messageId, request.getEmoji());
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.addReaction(groupId, messageId, request)
        );
    }

    @DeleteMapping("/{groupId}/messages/{messageId}/reactions/{emoji}")
    public ResponseGeneral<List<ReactionResponse>> removeReaction(
            @PathVariable Long groupId,
            @PathVariable Long messageId,
            @PathVariable String emoji,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(removeReaction) groupId: {}, messageId: {}, emoji: {}", groupId, messageId, emoji);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.removeReaction(groupId, messageId, emoji)
        );
    }

    @GetMapping("/{groupId}/presence")
    public ResponseGeneral<List<GroupMemberResponse>> getOnlineMembers(
            @PathVariable Long groupId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getOnlineMembers) groupId: {}", groupId);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getOnlineMembers(groupId)
        );
    }

    @GetMapping("/{groupId}/history")
    public ResponseGeneral<PageResponse<ChatGroupHistoryResponse>> getHistory(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getHistory) groupId: {}, page: {}, size: {}", groupId, page, size);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getHistory(groupId, page, size)
        );
    }

    @PostMapping("/heartbeat")
    public ResponseGeneral<Void> heartbeat(
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(heartbeat)");
        chatGroupFacadeService.heartbeat();
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    @GetMapping("/{groupId}/messages")
    public ResponseGeneral<PageResponse<ChatMessageResponse>> getMessages(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getMessages) groupId: {}, page: {}, size: {}", groupId, page, size);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                chatGroupFacadeService.getMessages(groupId, page, size)
        );
    }
}
