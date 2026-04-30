package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleByTopicResponse;
import org.oplearn.project.dto.response.dashboard.ArticleDailyResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.dto.response.dashboard.ArticleTopicDailyResponse;
import org.oplearn.project.facade.DashboardFacadeService;
import org.oplearn.project.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardFacadeServiceImpl implements DashboardFacadeService {

    private final DashboardService dashboardService;

    @Override
    public ArticleGrowthResponse getArticleGrowthByMonth(Integer year) {
        log.info("=== Start getArticleGrowthByMonth");
        int resolvedYear = (year != null) ? year : LocalDate.now().getYear();
        log.debug("(getArticleGrowthByMonth) year: {}", resolvedYear);
        return dashboardService.getArticleGrowthByMonth(resolvedYear);
    }

    @Override
    public ArticleBySourceResponse getArticleCountBySource(Integer year) {
        log.info("=== Start getArticleCountBySource");
        int resolvedYear = (year != null) ? year : LocalDate.now().getYear();
        log.debug("(getArticleCountBySource) year: {}", resolvedYear);
        return dashboardService.getArticleCountBySource(resolvedYear);
    }

    @Override
    public ArticleDailyResponse getArticleDailyCount(Integer year, Integer month) {
        log.info("=== Start getArticleDailyCount");
        LocalDate now = LocalDate.now();
        int resolvedYear = (year != null) ? year : now.getYear();
        int resolvedMonth = (month != null) ? month : now.getMonthValue();
        log.debug("(getArticleDailyCount) year: {}, month: {}", resolvedYear, resolvedMonth);
        return dashboardService.getArticleDailyCount(resolvedYear, resolvedMonth);
    }

    @Override
    public ArticleByTopicResponse getArticleCountByTopic(Integer year) {
        log.info("=== Start getArticleCountByTopic");
        int resolvedYear = (year != null) ? year : LocalDate.now().getYear();
        log.debug("(getArticleCountByTopic) year: {}", resolvedYear);
        return dashboardService.getArticleCountByTopic(resolvedYear);
    }

    @Override
    public ArticleTopicDailyResponse getArticleCountByTopicAndDay(Integer year, Integer month, Long topicId) {
        log.info("=== Start getArticleCountByTopicAndDay");
        LocalDate now = LocalDate.now();
        int resolvedYear  = (year  != null) ? year  : now.getYear();
        int resolvedMonth = (month != null) ? month : now.getMonthValue();
        log.debug("(getArticleCountByTopicAndDay) year: {}, month: {}, topicId: {}", resolvedYear, resolvedMonth, topicId);
        return dashboardService.getArticleCountByTopicAndDay(resolvedYear, resolvedMonth, topicId);
    }
}
