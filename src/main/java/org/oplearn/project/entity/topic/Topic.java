package org.oplearn.project.entity.topic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.AuditEntity;
import org.oplearn.project.entity.source.Source;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "topics")
@Data
public class Topic extends AuditEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "rss_url")
    private String rssUrl;

    @Column(name = "description")
    private String description;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "active")
    private Boolean active;

    public Topic(
            String name,
            String url,
            String rssUrl,
            String description,
            Long sourceId
    ) {
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        this.description = description;
        this.sourceId = sourceId;
        this.deleted = false;
        this.active = true;
    }

    public Topic(String name, String url) {
        this.name = name;
        this.url = url;
        this.deleted = false;
        this.active = true;
    }

    public Topic(String name) {
        this.name = name;
        this.deleted = false;
        this.active = true;
    }

    public Topic(
            String name,
            String url,
            String rssUrl,
            String description
    ) {
        this.name = name;
        this.url = url;
        this.rssUrl = rssUrl;
        this.description = description;
        this.deleted = false;
        this.active = true;
    }

}



