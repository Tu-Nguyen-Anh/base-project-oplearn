package org.oplearn.project.repository;

import org.oplearn.project.dto.response.topic.FollowedTopicResponse;
import org.oplearn.project.entity.topic.UserFollowTopic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFollowTopicRepository extends BaseRepository<UserFollowTopic> {

    boolean existsByUserIdAndTopicIdAndDeletedFalse(Long userId, Long topicId);

    @Query("""
            SELECT f FROM UserFollowTopic f
            WHERE f.userId = :userId
              AND f.topicId = :topicId
              AND f.deleted = false
            """)
    Optional<UserFollowTopic> findByUserIdAndTopicId(@Param("userId") Long userId,
                                                      @Param("topicId") Long topicId);

    @Query("""
            SELECT new org.oplearn.project.dto.response.topic.FollowedTopicResponse(
                f.id,
                t.id,
                t.name,
                t.url,
                t.rssUrl,
                s.id,
                s.name,
                f.createdAt
            )
            FROM UserFollowTopic f
            LEFT JOIN Topic t ON f.topicId = t.id AND t.deleted = false
            LEFT JOIN Source s ON t.sourceId = s.id AND s.deleted = false
            WHERE f.userId = :userId
              AND f.deleted = false
            ORDER BY f.createdAt DESC
            """)
    Page<FollowedTopicResponse> findFollowedTopicsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
            SELECT f.topicId
            FROM UserFollowTopic f
            WHERE f.userId = :userId
              AND f.deleted = false
            """)
    List<Long> findFollowedTopicIdsByUserId(@Param("userId") Long userId);
}
