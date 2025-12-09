package org.oplearn.project.entity.article;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.AuditEntity;
import org.oplearn.project.entity.topic.Topic;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "articles")
@Data
public class Article extends AuditEntity {
    @Column(name = "title")
    private String title;

    @Column(name = "link")
    private String link;

    @Column(name = "guid")
    private String guid;

    @Column(name = "description")
    private String description;

    @Column(name = "pub_date")
    private Long pubDate;

    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "topic_id")
    private Long topicId;

    @Column(name = "deleted")
    private Boolean deleted;

    public Article(
            String title,
            String link,
            String guid,
            String description,
            Long pubDate,
            String imageLink,
            Long topicId
    ) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.pubDate = pubDate;
        this.imageLink = imageLink;
        this.topicId = topicId;
        this.deleted = false;
    }

    public Article(String title, String link) {
        this.title = title;
        this.link = link;
        this.deleted = false;
    }

    public Article(String title) {
        this.title = title;
        this.deleted = false;
    }
    public Article(
            String title,
            String link,
            String guid,
            String description,
            String imageLink,
            Long topicId
    ) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.imageLink = imageLink;
        this.topicId = topicId;
        this.deleted = false;
    }
    public Article(
            String title,
            String link,
            String guid,
            String description,
            String imageLink
    ) {
        this.title = title;
        this.link = link;
        this.guid = guid;
        this.description = description;
        this.imageLink = imageLink;
        this.deleted = false;
    }
}



