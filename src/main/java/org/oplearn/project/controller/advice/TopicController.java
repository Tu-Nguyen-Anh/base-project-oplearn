package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.TopicFilterRequest;
import org.oplearn.project.dto.request.TopicRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.topic.FollowedTopicResponse;
import org.oplearn.project.dto.response.topic.TopicFilterResponse;
import org.oplearn.project.dto.response.topic.TopicResponse;
import org.oplearn.project.facade.TopicFacadeService;
import org.oplearn.project.service.TopicService;
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
@RequestMapping("/api/v1/topics")
public class TopicController {
    private final TopicService topicService;
    private final TopicFacadeService topicFacadeService;
    private final MessageService messageService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseGeneral<TopicResponse> create(
            @RequestBody @Validated TopicRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(create) request: {}", request);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                topicFacadeService.create(request)
        );
    }

    @PutMapping("{id}")
    public ResponseGeneral<TopicResponse> update(
            @RequestBody @Validated TopicRequest request,
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(update) request: {}", request);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                topicFacadeService.update(request, id)
        );
    }

    @DeleteMapping("{id}")
    public ResponseGeneral<Void> delete(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(delete) id: {}", id);

        topicFacadeService.delete(id);

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

        topicService.checkNameExists(name);

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

        topicService.checkUrlExists(url);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("{id}")
    public ResponseGeneral<TopicResponse> detail(
            @PathVariable Long id,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(detail) id: {}", id);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                topicService.detail(id)
        );
    }

    @PostMapping("/filter")
    public ResponseGeneral<PageResponse<TopicFilterResponse>> filter(
            @RequestBody TopicFilterRequest topicFilterRequest,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("Start filter topic: {}", topicFilterRequest);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                topicFacadeService.filter(topicFilterRequest));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{topicId}/follow")
    public ResponseGeneral<Void> followTopic(
            @PathVariable Long topicId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(followTopic) topicId: {}", topicId);

        topicFacadeService.followTopic(topicId);

        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @DeleteMapping("/{topicId}/follow")
    public ResponseGeneral<Void> unfollowTopic(
            @PathVariable Long topicId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(unfollowTopic) topicId: {}", topicId);

        topicFacadeService.unfollowTopic(topicId);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language)
        );
    }

    @GetMapping("/following")
    public ResponseGeneral<PageResponse<FollowedTopicResponse>> getFollowedTopics(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getFollowedTopics) page: {}, size: {}", page, size);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                topicFacadeService.getFollowedTopics(page, size)
        );
    }
}



