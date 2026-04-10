package org.oplearn.project.service;

import org.oplearn.project.dto.request.feedback.CreateFeedbackRequest;
import org.oplearn.project.dto.request.feedback.UpdateFeedbackStatusRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.feedback.FeedbackResponse;

public interface FeedbackService {

    /** Tạo mới góp ý (user). */
    FeedbackResponse create(CreateFeedbackRequest request, Long userId);

    /** Lấy chi tiết góp ý — user chỉ xem được của mình. */
    FeedbackResponse getById(Long feedbackId, Long userId);

    /** Danh sách góp ý của user hiện tại (phân trang). */
    PageResponse<FeedbackResponse> getMyFeedbacks(Long userId, int page, int size);

    /** Xóa mềm góp ý — chỉ chủ sở hữu. */
    void delete(Long feedbackId, Long userId);

    // ─── Admin ───────────────────────────────────────────────────────────────

    /** Danh sách tất cả góp ý (admin), có thể lọc theo status. */
    PageResponse<FeedbackResponse> getAllFeedbacks(Integer status, int page, int size);

    /** Cập nhật trạng thái góp ý (admin). */
    FeedbackResponse updateStatus(Long feedbackId, UpdateFeedbackStatusRequest request);
}
