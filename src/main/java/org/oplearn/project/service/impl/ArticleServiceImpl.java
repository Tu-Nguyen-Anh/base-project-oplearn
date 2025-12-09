package org.oplearn.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ArticleFilterRequest;
import org.oplearn.project.dto.request.ArticleRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.article.ArticleFilterResponse;
import org.oplearn.project.dto.response.article.ArticleResponse;
import org.oplearn.project.entity.article.Article;
import org.oplearn.project.entity.topic.Topic;
import org.oplearn.project.exception.base.ArticleNotFoundException;
import org.oplearn.project.exception.base.TopicNotFoundException;
import org.oplearn.project.exception.base.article.LinkAlreadyExistedException;
import org.oplearn.project.repository.ArticleRepository;
import org.oplearn.project.repository.TopicRepository;
import org.oplearn.project.service.ArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.oplearn.project.utils.DateTimeUtils.*;

@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository repository;
    private final TopicRepository topicRepository;

    public ArticleServiceImpl(ArticleRepository repository, TopicRepository topicRepository) {
        this.repository = repository;
        this.topicRepository = topicRepository;
    }

    @Override
    public Article getById(Long articleId) {
        return repository.getByIdAndDeletedFalse(articleId);
    }

    @Transactional
    @Override
    public ArticleResponse create(ArticleRequest request) {
        log.info("create");
        log.debug("Request: {}", request);

        Article article = this.mapRequestToEntity(request);

        article = repository.save(article);

        return this.mapEntityToResponse(article);
    }

    private Article mapRequestToEntity(ArticleRequest articleRequest) {
        Topic topic = topicRepository.getByIdAndDeletedFalse(articleRequest.getTopicId());
        if (topic == null || topic.getDeleted()) {
            throw new TopicNotFoundException();
        }

        return new Article(
                articleRequest.getTitle(),
                articleRequest.getLink(),
                articleRequest.getGuid(),
                articleRequest.getDescription(),
                articleRequest.getImageLink()
        );
    }

    private ArticleResponse mapEntityToResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getLink(),
                article.getGuid(),
                article.getDescription(),
                article.getPubDate(),
                article.getImageLink()
        );
    }

    @Transactional
    @Override
    public ArticleResponse update(ArticleRequest request, Long id) {
        log.info("update");
        log.debug("request: {}, id: {}", request, id);

        Article existingArticle = this.checkExistById(id);
        this.checkValidForUpdate(existingArticle, request);
        this.setValueForUpdate(existingArticle, request);

        existingArticle = repository.save(existingArticle);
        return this.mapEntityToResponse(existingArticle);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Deleting");
        log.debug("id: {}", id);

        Article article = this.checkExistById(id);
        article.setDeleted(true);
        repository.save(article);
    }

    @Override
    public void checkLinkExists(String link) {
        log.info("=== Start checkLinkExists");
        log.debug("(checkLinkExists) link: {}", link);

        if (isLinkExisted(link)) {
            throw new LinkAlreadyExistedException();
        }
    }

    @Override
    public void save(Article article) {
        repository.save(article);
    }

    @Override
    public PageResponse<ArticleFilterResponse> filter(ArticleFilterRequest request) {
        log.info("Filtering");
        log.info("Request: {}", request);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        String keyword = request.getKeyword() == null ? "" : request.getKeyword();

        // Convert DD/MM/YYYY to timestamp (milliseconds)
        Long fromPubDateTimestamp = null;
        Long toPubDateTimestamp = null;
        log.info("fromPubDateTimestamp: {}", fromPubDateTimestamp);
        log.info("toPubDateTimestamp: {}", toPubDateTimestamp);

        if (request.getFromPubDate() != null && !request.getFromPubDate().isEmpty()) {
            LocalDate fromDate = toLocalDate(request.getFromPubDate(), DEFAULT_DATE_PATTERN);
            if (fromDate != null) {
                fromPubDateTimestamp = toMilli(fromDate);
                log.info("fromPubDateTimestamp: {}", fromPubDateTimestamp);
            }
        }

        if (request.getToPubDate() != null && !request.getToPubDate().isEmpty()) {
            LocalDate toDate = toLocalDate(request.getToPubDate(), DEFAULT_DATE_PATTERN);
            if (toDate != null) {
                // Set to end of day for inclusive range
                ZonedDateTime endOfDay = ZonedDateTime.of(toDate, LocalTime.MAX, ZoneId.systemDefault());
                toPubDateTimestamp = endOfDay.toInstant().toEpochMilli();
                log.info("toPubDateTimestamp: {}", toPubDateTimestamp);
            }
        }

        Page<ArticleFilterResponse> articles = repository.filter(
                keyword,
                request.getTopicId(),
                request.getSourceId(),
                fromPubDateTimestamp,
                toPubDateTimestamp,
                pageable
        );

        return PageResponse.of(articles.getContent(), (int) articles.getTotalElements());
    }

    @Override
    public ArticleResponse detail(Long id) {
        log.info("=== Start detail");
        log.debug("id: {}", id);
        Article article = repository.getByIdAndDeletedFalse(id);
        if (Objects.isNull(article) || article.getDeleted() == true) {
            log.error("Article not found exception");
            throw new ArticleNotFoundException();
        }
        return mapEntityToResponse(article);
    }

    @Override
    public Article checkExistById(Long id) {
        log.info("(checkExistById");
        log.debug("checkExistById: {}", id);

        Article article = repository.findById(id).orElseThrow(ArticleNotFoundException::new);
        if (article == null || article.getDeleted()) {
            throw new ArticleNotFoundException();
        }
        return article;
    }

    private void checkValidForUpdate(Article article, ArticleRequest request) {
        if (Objects.nonNull(article.getLink()) && !article.getLink().equals(request.getLink()) &&
                isLinkExisted(request.getLink())) {
            throw new LinkAlreadyExistedException();
        }
    }

    private boolean objectNotNullAndNotEmpty(String object) {
        return object != null && !object.isEmpty();
    }

    private boolean isLinkExisted(String link) {
        return objectNotNullAndNotEmpty(link)
                && repository.existsByLinkAndDeletedIsFalse(link);
    }

    private void setValueForUpdate(Article article, ArticleRequest request) {
        if (request.getTitle() != null) {
            article.setTitle(request.getTitle());
        }
        if (request.getLink() != null) {
            article.setLink(request.getLink());
        }
        if (request.getGuid() != null) {
            article.setGuid(request.getGuid());
        }
        if (request.getDescription() != null) {
            article.setDescription(request.getDescription());
        }
        if (request.getPubDate() != null) {
            article.setPubDate(request.getPubDate());
        }
        if (request.getImageLink() != null) {
            article.setImageLink(request.getImageLink());
        }
        if (request.getTopicId() != null) {
            article.setTopicId(request.getTopicId());
        }
    }
}

