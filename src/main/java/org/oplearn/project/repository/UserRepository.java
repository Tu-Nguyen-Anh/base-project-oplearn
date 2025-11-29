package org.oplearn.project.repository;

import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<User> {
    User getByIdAndDeletedFalse(Long id);

    Optional<User> findByUsernameAndDeletedFalse(String userName);

    boolean existsByEmailAndDeletedIsFalse(String email);

    boolean existsByPhoneNumberAndDeletedIsFalse(String phone);

    boolean existsByUsernameIgnoreCaseAndDeletedIsFalse(String username);
    @Query("""
        SELECT distinct new org.oplearn.project.dto.response.user.UserFilterResponse(
                u.id,
                u.username,
                u.email,
                u.fullName,
                u.phoneNumber,
                u.status,
                u.createdBy,
                u.createdAt
                )
              FROM User u
              WHERE
                ( :keyword = '' OR
                LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                )
                AND u.deleted = false
                AND (:status IS NULL OR u.status IN :status)
              ORDER BY u.status ASC, u.createdAt DESC
        """)
    Page<UserFilterResponse> filter(
            @Param("keyword") String keyword,
            @Param("status") List<Integer> status,
            Pageable pageable
    );
}
