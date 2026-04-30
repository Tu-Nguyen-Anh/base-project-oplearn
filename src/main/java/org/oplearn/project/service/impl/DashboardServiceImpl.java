package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleByTopicResponse;
import org.oplearn.project.dto.response.dashboard.ArticleDailyResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.dto.response.dashboard.ArticleTopicDailyResponse;
import org.oplearn.project.repository.ArticleRepository;
import org.oplearn.project.repository.projection.DailyCountProjection;
import org.oplearn.project.repository.projection.MonthlyCountProjection;
import org.oplearn.project.repository.projection.TopicDailyCountProjection;
import org.oplearn.project.repository.projection.TopicMonthlyCountProjection;
import org.oplearn.project.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ArticleRepository articleRepository;

    @Override
    public ArticleGrowthResponse getArticleGrowthByMonth(int year) {
        log.debug("(getArticleGrowthByMonth) year: {}", year);

        List<MonthlyCountProjection> rawData = articleRepository.countArticlesByMonth(year);
        Map<Integer, Long> monthCountMap = rawData.stream()
                .collect(Collectors.toMap(MonthlyCountProjection::getMonth, MonthlyCountProjection::getCount));

        List<ArticleGrowthResponse.MonthlyGrowth> months = IntStream.rangeClosed(1, 12)
                .mapToObj(m -> ArticleGrowthResponse.MonthlyGrowth.builder()
                        .month(m)
                        .monthName(Month.of(m).getDisplayName(TextStyle.FULL, Locale.ENGLISH))
                        .count(monthCountMap.getOrDefault(m, 0L))
                        .build())
                .collect(Collectors.toList());

        long total = months.stream().mapToLong(ArticleGrowthResponse.MonthlyGrowth::getCount).sum();

        return ArticleGrowthResponse.builder()
                .year(year)
                .months(months)
                .total(total)
                .build();
    }

    @Override
    public ArticleBySourceResponse getArticleCountBySource(int year) {
        log.debug("(getArticleCountBySource) year: {}", year);

        List<Object[]> rawData = articleRepository.countArticlesBySourceAndMonth(year);

        // Object[] order: [0]=source_id, [1]=source_name, [2]=month, [3]=count
        Map<Long, String> sourceNames = new LinkedHashMap<>();
        Map<Long, Map<Integer, Long>> sourceMonthData = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            Long sourceId = ((Number) row[0]).longValue();
            String sourceName = (String) row[1];
            int month = ((Number) row[2]).intValue();
            long count = ((Number) row[3]).longValue();

            sourceNames.putIfAbsent(sourceId, sourceName);
            sourceMonthData.computeIfAbsent(sourceId, k -> new HashMap<>()).put(month, count);
        }

        List<ArticleBySourceResponse.SourceData> sources = sourceNames.entrySet().stream()
                .map(entry -> {
                    Long sourceId = entry.getKey();
                    Map<Integer, Long> monthMap = sourceMonthData.getOrDefault(sourceId, Collections.emptyMap());

                    List<ArticleBySourceResponse.MonthlyCount> monthlyData = IntStream.rangeClosed(1, 12)
                            .mapToObj(m -> ArticleBySourceResponse.MonthlyCount.builder()
                                    .month(m)
                                    .count(monthMap.getOrDefault(m, 0L))
                                    .build())
                            .collect(Collectors.toList());

                    long sourceTotal = monthlyData.stream().mapToLong(ArticleBySourceResponse.MonthlyCount::getCount).sum();

                    return ArticleBySourceResponse.SourceData.builder()
                            .sourceId(sourceId)
                            .sourceName(entry.getValue())
                            .monthlyData(monthlyData)
                            .total(sourceTotal)
                            .build();
                })
                .sorted(Comparator.comparing(ArticleBySourceResponse.SourceData::getSourceId))
                .collect(Collectors.toList());

        return ArticleBySourceResponse.builder()
                .year(year)
                .sources(sources)
                .build();
    }

    @Override
    public ArticleDailyResponse getArticleDailyCount(int year, int month) {
        log.debug("(getArticleDailyCount) year: {}, month: {}", year, month);

        List<DailyCountProjection> rawData = articleRepository.countArticlesByDay(year, month);
        Map<Integer, Long> dayCountMap = rawData.stream()
                .collect(Collectors.toMap(DailyCountProjection::getDay, DailyCountProjection::getCount));

        int totalDays = YearMonth.of(year, month).lengthOfMonth();

        List<ArticleDailyResponse.DailyCount> days = IntStream.rangeClosed(1, totalDays)
                .mapToObj(d -> ArticleDailyResponse.DailyCount.builder()
                        .day(d)
                        .count(dayCountMap.getOrDefault(d, 0L))
                        .build())
                .collect(Collectors.toList());

        long total = days.stream().mapToLong(ArticleDailyResponse.DailyCount::getCount).sum();

        return ArticleDailyResponse.builder()
                .year(year)
                .month(month)
                .totalDays(totalDays)
                .days(days)
                .total(total)
                .build();
    }

    @Override
    public ArticleByTopicResponse getArticleCountByTopic(int year) {
        log.debug("(getArticleCountByTopic) year: {}", year);

        List<TopicMonthlyCountProjection> rawData = articleRepository.countArticlesByTopicAndMonth(year);

        Map<Long, String> topicNames = new LinkedHashMap<>();
        Map<Long, Map<Integer, Long>> topicMonthData = new LinkedHashMap<>();

        for (TopicMonthlyCountProjection row : rawData) {
            topicNames.putIfAbsent(row.getTopicId(), row.getTopicName());
            topicMonthData
                    .computeIfAbsent(row.getTopicId(), k -> new HashMap<>())
                    .put(row.getMonth(), row.getCount());
        }

        List<ArticleByTopicResponse.TopicData> topics = topicNames.entrySet().stream()
                .map(entry -> {
                    Long topicId = entry.getKey();
                    Map<Integer, Long> monthMap = topicMonthData.getOrDefault(topicId, Collections.emptyMap());

                    List<ArticleByTopicResponse.MonthlyCount> monthlyData = IntStream.rangeClosed(1, 12)
                            .mapToObj(m -> ArticleByTopicResponse.MonthlyCount.builder()
                                    .month(m)
                                    .monthName(Month.of(m).getDisplayName(TextStyle.SHORT, Locale.ENGLISH))
                                    .count(monthMap.getOrDefault(m, 0L))
                                    .build())
                            .collect(Collectors.toList());

                    long topicTotal = monthlyData.stream().mapToLong(ArticleByTopicResponse.MonthlyCount::getCount).sum();

                    return ArticleByTopicResponse.TopicData.builder()
                            .topicId(topicId)
                            .topicName(entry.getValue())
                            .monthlyData(monthlyData)
                            .total(topicTotal)
                            .build();
                })
                .sorted(Comparator.comparing(ArticleByTopicResponse.TopicData::getTopicId))
                .collect(Collectors.toList());

        long grandTotal = topics.stream().mapToLong(ArticleByTopicResponse.TopicData::getTotal).sum();

        return ArticleByTopicResponse.builder()
                .year(year)
                .topics(topics)
                .grandTotal(grandTotal)
                .build();
    }

    @Override
    public ArticleTopicDailyResponse getArticleCountByTopicAndDay(int year, int month, Long topicId) {
        log.debug("(getArticleCountByTopicAndDay) year: {}, month: {}, topicId: {}", year, month, topicId);

        List<TopicDailyCountProjection> rawData = articleRepository.countArticlesByTopicAndDay(year, month, topicId);

        int totalDays = YearMonth.of(year, month).lengthOfMonth();

        // group raw rows by topicId
        Map<Long, String> topicNames = new LinkedHashMap<>();
        Map<Long, Map<Integer, Long>> topicDayData = new LinkedHashMap<>();

        for (TopicDailyCountProjection row : rawData) {
            topicNames.putIfAbsent(row.getTopicId(), row.getTopicName());
            topicDayData
                    .computeIfAbsent(row.getTopicId(), k -> new HashMap<>())
                    .put(row.getDay(), row.getCount());
        }

        List<ArticleTopicDailyResponse.TopicDailyData> topics = topicNames.entrySet().stream()
                .map(entry -> {
                    Long tid = entry.getKey();
                    Map<Integer, Long> dayMap = topicDayData.getOrDefault(tid, Collections.emptyMap());

                    List<ArticleTopicDailyResponse.DailyCount> days = IntStream.rangeClosed(1, totalDays)
                            .mapToObj(d -> ArticleTopicDailyResponse.DailyCount.builder()
                                    .day(d)
                                    .count(dayMap.getOrDefault(d, 0L))
                                    .build())
                            .collect(Collectors.toList());

                    long topicTotal = days.stream().mapToLong(ArticleTopicDailyResponse.DailyCount::getCount).sum();

                    return ArticleTopicDailyResponse.TopicDailyData.builder()
                            .topicId(tid)
                            .topicName(entry.getValue())
                            .days(days)
                            .total(topicTotal)
                            .build();
                })
                .sorted(Comparator.comparing(ArticleTopicDailyResponse.TopicDailyData::getTopicId))
                .collect(Collectors.toList());

        long grandTotal = topics.stream().mapToLong(ArticleTopicDailyResponse.TopicDailyData::getTotal).sum();

        return ArticleTopicDailyResponse.builder()
                .year(year)
                .month(month)
                .totalDays(totalDays)
                .topics(topics)
                .grandTotal(grandTotal)
                .build();
    }
}
