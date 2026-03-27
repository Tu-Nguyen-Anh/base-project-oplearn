package org.oplearn.project.controller.advice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ChangePasswordRequest;
import org.oplearn.project.dto.request.UserFilterRequest;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.dto.response.user.UserHistoryResponse;
import org.oplearn.project.dto.response.user.UserMentionResponse;
import org.oplearn.project.dto.response.user.UserResponse;
import org.oplearn.project.facade.UserFacadeService;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserFacadeService userFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<UserResponse> create(
            @RequestBody @Validated UserRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create) request: {}", request);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                userFacadeService.create(request)
        );
    }

    @PutMapping("{id}")
    public ResponseGeneral<UserResponse> update(
            @RequestBody @Validated UserRequest request,
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(update) request: {}", request);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                userFacadeService.update(request, id)
        );
    }

    @DeleteMapping("{id}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete) id: {}", id);

        userFacadeService.delete(id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }


    @GetMapping("/exist-email")
    public ResponseGeneral<Void> checkEmailExist(
            @RequestParam String email,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkEmailExist) email: {}", email);

        userService.checkEmailExists(email);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/check-username")
    public ResponseGeneral<Void> checkUsernameExist(
            @RequestParam String username,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkUsernameExist) username: {}", username);

        userService.checkUsernameExists(username);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/exist-phone")
    public ResponseGeneral<Void> checkPhoneNumberExist(
            @RequestParam(name = "phone_number") String phoneNumber,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkPhoneNumberExist) phoneNumber: {}", phoneNumber);

        userService.checkPhoneNumberExists(phoneNumber);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("{id}")
    public ResponseGeneral<UserResponse> detail(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(detail) id: {}", id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                userService.detail(id)
        );
    }

    @GetMapping("{id}/histories")
    public ResponseGeneral<PageResponse<UserHistoryResponse>> histories(
            @PathVariable Long id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(histories) userId: {}, page: {}, size: {}", id, page, size);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                userFacadeService.getHistories(id, page, size));
    }

    @PutMapping("reset-password/{id}")
    public ResponseGeneral<Void> resetPassword(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(change password) id: {}", id);

        userFacadeService.resetPassword(id);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }
    @PutMapping("/{id}/password")
    public ResponseGeneral<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(changePassword) id: {}", id);

        userFacadeService.changePassword(id, request);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/mention-search")
    public ResponseGeneral<PageResponse<UserMentionResponse>> mentionSearch(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(mentionSearch) keyword: {}", keyword);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                userService.searchForMention(keyword, page, size)
        );
    }

    @PostMapping("/filter")
    public ResponseGeneral<PageResponse<UserFilterResponse>> histories(
            @RequestBody UserFilterRequest userFilterRequest,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {

        log.info("Start filter user: {}", userFilterRequest);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                userFacadeService.filter(userFilterRequest));
    }

}
