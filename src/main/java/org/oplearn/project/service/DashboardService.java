package org.oplearn.project.service;

import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;

public interface DashboardService {

    ArticleGrowthResponse getArticleGrowthByMonth(int year);

    ArticleBySourceResponse getArticleCountBySource(int year);
}
