package org.oplearn.project.entity.feedback;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.oplearn.project.entity.base.BaseEntity;

@Entity
@Table(name = "feedback_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedbackImage extends BaseEntity {

    @Column(name = "feedback_id", nullable = false)
    private Long feedbackId;

    @Column(name = "image_url", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    /** Object key trong RustFS bucket, dùng để xóa file khi cần. */
    @Column(name = "storage_key", columnDefinition = "TEXT")
    private String storageKey;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "uploaded_at")
    private Long uploadedAt;
}
