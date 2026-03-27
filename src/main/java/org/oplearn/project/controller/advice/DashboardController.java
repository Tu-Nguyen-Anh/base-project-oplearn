package org.oplearn.project.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.facade.DashboardFacadeService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardFacadeService dashboardFacadeService;
    private final MessageService messageService;

    @GetMapping("/articles/growth")
    public ResponseGeneral<ArticleGrowthResponse> getArticleGrowth(
            @RequestParam(required = false) Integer year,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleGrowth) year: {}", year);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleGrowthByMonth(year)
        );
    }

    @GetMapping("/articles/by-source")
    public ResponseGeneral<ArticleBySourceResponse> getArticleCountBySource(
            @RequestParam(required = false) Integer year,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleCountBySource) year: {}", year);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleCountBySource(year)
        );
    }
}
