package org.oplearn.project.controller.advice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.feedback.CreateFeedbackRequest;
import org.oplearn.project.dto.request.feedback.UpdateFeedbackStatusRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.feedback.FeedbackResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.FeedbackService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;
import static org.oplearn.project.constanst.OpLearnConstants.VariableConstant.PAGE_DEFAULT;
import static org.oplearn.project.constanst.OpLearnConstants.VariableConstant.SIZE_DEFAULT;

@Tag(name = "Feedback", description = "API góp ý / đóng góp ý kiến của người dùng")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final MessageService messageService;

    // ═══════════════════════════════════════════════════════════════════
    // User APIs  (/api/v1/feedbacks)
    // ═══════════════════════════════════════════════════════════════════

    @Operation(
            summary = "Gửi góp ý",
            description = "Tạo mới một góp ý. Ảnh đã upload qua /api/v1/upload/images, " +
                    "truyền URL vào trường image_urls.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tạo thành công"),
                    @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
                    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/feedbacks")
    public ResponseGeneral<FeedbackResponse> createFeedback(
            @RequestBody @Valid CreateFeedbackRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        log.info("(createFeedback) userId: {}", currentUser.getId());
        return ResponseGeneral.ofCreated(
                messageService.getMessage(SUCCESS, language),
                feedbackService.create(request, currentUser.getId())
        );
    }

    @Operation(
            summary = "Danh sách góp ý của tôi",
            description = "Lấy danh sách tất cả góp ý do người dùng hiện tại tạo (phân trang).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
            }
    )
    @GetMapping("/api/v1/feedbacks")
    public ResponseGeneral<PageResponse<FeedbackResponse>> getMyFeedbacks(
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        log.info("(getMyFeedbacks) userId: {}, page: {}, size: {}", currentUser.getId(), page, size);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                feedbackService.getMyFeedbacks(currentUser.getId(), page, size)
        );
    }

    @Operation(
            summary = "Chi tiết góp ý",
            description = "Lấy chi tiết một góp ý. Chỉ chủ sở hữu mới xem được.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "403", description = "Không có quyền"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy góp ý")
            }
    )
    @GetMapping("/api/v1/feedbacks/{feedbackId}")
    public ResponseGeneral<FeedbackResponse> getFeedbackById(
            @PathVariable Long feedbackId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        log.info("(getFeedbackById) feedbackId: {}, userId: {}", feedbackId, currentUser.getId());
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                feedbackService.getById(feedbackId, currentUser.getId())
        );
    }

    @Operation(
            summary = "Xóa góp ý",
            description = "Xóa mềm một góp ý (chỉ chủ sở hữu). " +
                    "Ảnh đính kèm cũng sẽ bị xóa khỏi storage.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Xóa thành công"),
                    @ApiResponse(responseCode = "403", description = "Không có quyền"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy góp ý")
            }
    )
    @DeleteMapping("/api/v1/feedbacks/{feedbackId}")
    public ResponseGeneral<Void> deleteFeedback(
            @PathVariable Long feedbackId,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        log.info("(deleteFeedback) feedbackId: {}, userId: {}", feedbackId, currentUser.getId());
        feedbackService.delete(feedbackId, currentUser.getId());
        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Admin APIs  (/api/v1/admin/feedbacks)
    // ═══════════════════════════════════════════════════════════════════

    @Operation(
            summary = "[Admin] Tất cả góp ý",
            description = "Lấy danh sách tất cả góp ý của hệ thống. " +
                    "Có thể lọc theo status: 0=PENDING, 1=IN_REVIEW, 2=RESOLVED, 3=REJECTED",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Thành công"),
                    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/v1/admin/feedbacks")
    public ResponseGeneral<PageResponse<FeedbackResponse>> getAllFeedbacks(
            @Parameter(description = "Lọc theo trạng thái (0-3), bỏ trống để lấy tất cả")
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = PAGE_DEFAULT) int page,
            @RequestParam(defaultValue = SIZE_DEFAULT) int size,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getAllFeedbacks) status: {}, page: {}, size: {}", status, page, size);
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                feedbackService.getAllFeedbacks(status, page, size)
        );
    }

    @Operation(
            summary = "[Admin] Cập nhật trạng thái góp ý",
            description = "Thay đổi trạng thái xử lý: 0=PENDING, 1=IN_REVIEW, 2=RESOLVED, 3=REJECTED",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
                    @ApiResponse(responseCode = "404", description = "Không tìm thấy góp ý")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/v1/admin/feedbacks/{feedbackId}/status")
    public ResponseGeneral<FeedbackResponse> updateFeedbackStatus(
            @PathVariable Long feedbackId,
            @RequestBody @Valid UpdateFeedbackStatusRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(updateFeedbackStatus) feedbackId: {}, status: {}", feedbackId, request.getStatus());
        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                feedbackService.updateStatus(feedbackId, request)
        );
    }
}
