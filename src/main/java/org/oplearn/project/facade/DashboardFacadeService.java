package org.oplearn.project.facade;

import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleByTopicResponse;
import org.oplearn.project.dto.response.dashboard.ArticleDailyResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.dto.response.dashboard.ArticleTopicDailyResponse;

public interface DashboardFacadeService {

    ArticleGrowthResponse getArticleGrowthByMonth(Integer year);

    ArticleBySourceResponse getArticleCountBySource(Integer year);

    ArticleDailyResponse getArticleDailyCount(Integer year, Integer month);

    ArticleByTopicResponse getArticleCountByTopic(Integer year);

    ArticleTopicDailyResponse getArticleCountByTopicAndDay(Integer year, Integer month, Long topicId);
}
