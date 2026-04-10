package org.oplearn.project.repository;

import org.oplearn.project.entity.feedback.FeedbackImage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackImageRepository extends BaseRepository<FeedbackImage> {

    List<FeedbackImage> findAllByFeedbackId(Long feedbackId);

    List<FeedbackImage> findAllByFeedbackIdIn(List<Long> feedbackIds);

    void deleteAllByFeedbackId(Long feedbackId);
}
