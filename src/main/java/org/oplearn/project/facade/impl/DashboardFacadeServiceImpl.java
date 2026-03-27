package org.oplearn.project.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
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
}
