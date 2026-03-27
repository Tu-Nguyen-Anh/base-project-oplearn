package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.repository.ArticleRepository;
import org.oplearn.project.repository.projection.MonthlyCountProjection;
import org.oplearn.project.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
}
