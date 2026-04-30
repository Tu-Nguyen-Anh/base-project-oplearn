package org.oplearn.project.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.topic.Topic;
import org.oplearn.project.repository.TopicRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TopicHealthCheckJob {

    private static final int TIMEOUT_SECONDS = 3;

    private final TopicRepository topicRepository;

    @Scheduled(fixedRate = 3_600_000)
    public void checkTopicRssUrls() {
        log.info("(checkTopicRssUrls) Start RSS health check");

        List<Topic> topics = topicRepository.findAllWithRssUrl();
        if (topics.isEmpty()) {
            log.info("(checkTopicRssUrls) No topics with rssUrl found");
            return;
        }

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        int activated = 0;
        int deactivated = 0;

        for (Topic topic : topics) {
            boolean reachable = isRssUrlReachable(client, topic.getRssUrl());
            boolean changed = false;

            if (reachable && !Boolean.TRUE.equals(topic.getActive())) {
                topic.setActive(true);
                changed = true;
                activated++;
                log.info("(checkTopicRssUrls) Topic id={} is now ACTIVE", topic.getId());
            } else if (!reachable && !Boolean.FALSE.equals(topic.getActive())) {
                topic.setActive(false);
                changed = true;
                deactivated++;
                log.warn("(checkTopicRssUrls) Topic id={} marked INACTIVE, url={}", topic.getId(), topic.getRssUrl());
            }

            if (changed) {
                topicRepository.save(topic);
            }
        }

        log.info("(checkTopicRssUrls) Completed: {} topics checked, {} activated, {} deactivated",
                topics.size(), activated, deactivated);
    }

    private boolean isRssUrlReachable(HttpClient client, String rssUrl) {
        try {
            URI uri = URI.create(rssUrl);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                    .GET()
                    .build();

            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() < 500;
        } catch (Exception e) {
            log.debug("(isRssUrlReachable) url={} error={}", rssUrl, e.getMessage());
            return false;
        }
    }
}
