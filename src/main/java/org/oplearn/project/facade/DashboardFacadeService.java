package org.oplearn.project.facade;

import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;

public interface DashboardFacadeService {

    ArticleGrowthResponse getArticleGrowthByMonth(Integer year);

    ArticleBySourceResponse getArticleCountBySource(Integer year);
}
