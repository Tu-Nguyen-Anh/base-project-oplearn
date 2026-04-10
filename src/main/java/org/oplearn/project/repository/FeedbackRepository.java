package org.oplearn.project.repository;

import org.oplearn.project.entity.feedback.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends BaseRepository<Feedback> {

    Optional<Feedback> findByIdAndDeletedFalse(Long id);

    Page<Feedback> findAllByUserIdAndDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    Page<Feedback> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<Feedback> findAllByStatusAndDeletedFalseOrderByCreatedAtDesc(Integer status, Pageable pageable);
}
