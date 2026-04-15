package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.SourceFilterRequest;
import org.oplearn.project.dto.request.SourceRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.source.SourceFilterResponse;
import org.oplearn.project.dto.response.source.SourceResponse;
import org.oplearn.project.dto.response.source.SourceWithTopicsResponse;
import org.oplearn.project.facade.SourceFacadeService;
import org.oplearn.project.service.SourceService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.oplearn.project.entity.user.User;
import org.oplearn.project.security.UserAuthenticated;

import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/sources")
public class SourceController {
    private final SourceService sourceService;
    private final SourceFacadeService sourceFacadeService;
    private final MessageService messageService;

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<SourceResponse> create(
            @RequestBody @Validated SourceRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create) request: {}", request);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                sourceFacadeService.create(request)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseGeneral<SourceResponse> update(
            @RequestBody @Validated SourceRequest request,
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(update) request: {}", request);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                sourceFacadeService.update(request, id)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete) id: {}", id);

        sourceFacadeService.delete(id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/exist-name")
    public ResponseGeneral<Void> checkNameExist(
            @RequestParam String name,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkNameExist) name: {}", name);

        sourceService.checkNameExists(name);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/exist-url")
    public ResponseGeneral<Void> checkUrlExist(
            @RequestParam String url,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(checkUrlExist) url: {}", url);

        sourceService.checkUrlExists(url);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("{id}")
    public ResponseGeneral<SourceResponse> detail(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(detail) id: {}", id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                sourceService.detail(id)
        );
    }

    @PostMapping("/filter")
    public ResponseGeneral<PageResponse<SourceFilterResponse>> filter(
            @RequestBody SourceFilterRequest sourceFilterRequest,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("Start filter source: {}", sourceFilterRequest);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                sourceFacadeService.filter(sourceFilterRequest));
    }

    @GetMapping("/all-with-topics")
    public ResponseGeneral<List<SourceWithTopicsResponse>> getAllWithTopics(
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getAllWithTopics)");

        Long userId = null;
        try {
            User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
            userId = currentUser.getId();
        } catch (Exception ignored) {
        }

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                sourceService.getAllWithTopics(userId));
    }
}

