package org.oplearn.project.entity.source;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.oplearn.project.entity.base.AuditEntity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sources")
@Data
public class Source extends AuditEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "type")
    private Integer type;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "active")
    private Boolean active;

    public Source(
            String name,
            String url,
            String avatar,
            Integer type,
            String description
    ) {
        this.name = name;
        this.url = url;
        this.avatar = avatar;
        this.type = type;
        this.description = description;
        this.deleted = false;
        this.active = true;
    }

    public Source(String name, String url) {
        this.name = name;
        this.url = url;
        this.deleted = false;
        this.active = true;
    }

    public Source(String name) {
        this.name = name;
        this.deleted = false;
        this.active = true;
    }
}


