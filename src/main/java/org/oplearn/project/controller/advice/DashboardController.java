package org.oplearn.project.controller.advice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.dashboard.ArticleBySourceResponse;
import org.oplearn.project.dto.response.dashboard.ArticleByTopicResponse;
import org.oplearn.project.dto.response.dashboard.ArticleDailyResponse;
import org.oplearn.project.dto.response.dashboard.ArticleGrowthResponse;
import org.oplearn.project.dto.response.dashboard.ArticleTopicDailyResponse;
import org.oplearn.project.facade.DashboardFacadeService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;

@Tag(name = "Dashboard", description = "Thống kê và báo cáo dành cho Admin. Tất cả API đều yêu cầu role ADMIN và Bearer JWT token.")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardFacadeService dashboardFacadeService;
    private final MessageService messageService;

    @Operation(
            summary = "Thống kê bài viết theo tháng",
            description = """
                    Trả về số lượng bài viết được tạo trong từng tháng của một năm.
                    Dữ liệu trả về luôn có đủ 12 tháng, tháng nào không có bài viết sẽ trả về count = 0.
                    Nếu không truyền `year`, mặc định lấy năm hiện tại.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 200,
                                      "message": "Success",
                                      "data": {
                                        "year": 2026,
                                        "months": [
                                          { "month": 1, "month_name": "January", "count": 45 },
                                          { "month": 2, "month_name": "February", "count": 62 },
                                          { "month": 3, "month_name": "March", "count": 0 }
                                        ],
                                        "total": 107
                                      },
                                      "timestamp": "2026-04-30T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực (thiếu hoặc sai token)"),
            @ApiResponse(responseCode = "403", description = "Không có quyền ADMIN")
    })
    @GetMapping("/articles/growth")
    public ResponseGeneral<ArticleGrowthResponse> getArticleGrowth(
            @Parameter(description = "Năm cần thống kê (ví dụ: 2026). Mặc định là năm hiện tại nếu bỏ trống.")
            @RequestParam(required = false) Integer year,
            @Parameter(hidden = true)
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleGrowth) year: {}", year);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleGrowthByMonth(year)
        );
    }

    @Operation(
            summary = "Thống kê bài viết theo ngày trong tháng",
            description = """
                    Trả về số lượng bài viết được tạo trong từng ngày của một tháng cụ thể.
                    Dữ liệu trả về đủ số ngày trong tháng, ngày không có bài viết trả về count = 0.
                    Nếu không truyền `year` hoặc `month`, mặc định là tháng/năm hiện tại.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 200,
                                      "message": "Success",
                                      "data": {
                                        "year": 2026,
                                        "month": 4,
                                        "total_days": 30,
                                        "days": [
                                          { "day": 1, "count": 5 },
                                          { "day": 2, "count": 0 },
                                          { "day": 3, "count": 12 }
                                        ],
                                        "total": 17
                                      },
                                      "timestamp": "2026-04-30T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền ADMIN")
    })
    @GetMapping("/articles/daily")
    public ResponseGeneral<ArticleDailyResponse> getArticleDailyCount(
            @Parameter(description = "Năm cần thống kê (ví dụ: 2026). Mặc định là năm hiện tại.")
            @RequestParam(required = false) Integer year,
            @Parameter(description = "Tháng cần thống kê, từ 1–12 (ví dụ: 4). Mặc định là tháng hiện tại.")
            @RequestParam(required = false) Integer month,
            @Parameter(hidden = true)
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleDailyCount) year: {}, month: {}", year, month);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleDailyCount(year, month)
        );
    }

    @Operation(
            summary = "Thống kê bài viết theo nguồn tin",
            description = """
                    Trả về số lượng bài viết theo từng nguồn tin (source), phân theo tháng trong năm.
                    Mỗi source có danh sách 12 tháng, tháng nào không có bài viết trả về count = 0.
                    Nếu không truyền `year`, mặc định là năm hiện tại.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 200,
                                      "message": "Success",
                                      "data": {
                                        "year": 2026,
                                        "sources": [
                                          {
                                            "source_id": 1,
                                            "source_name": "VnExpress",
                                            "monthly_data": [
                                              { "month": 1, "count": 30 },
                                              { "month": 2, "count": 0 },
                                              { "month": 3, "count": 15 }
                                            ],
                                            "total": 45
                                          }
                                        ]
                                      },
                                      "timestamp": "2026-04-30T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền ADMIN")
    })
    @GetMapping("/articles/by-source")
    public ResponseGeneral<ArticleBySourceResponse> getArticleCountBySource(
            @Parameter(description = "Năm cần thống kê (ví dụ: 2026). Mặc định là năm hiện tại.")
            @RequestParam(required = false) Integer year,
            @Parameter(hidden = true)
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleCountBySource) year: {}", year);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleCountBySource(year)
        );
    }

    @Operation(
            summary = "Thống kê bài viết theo chủ đề (topic)",
            description = """
                    Trả về số lượng bài viết theo từng chủ đề (topic), phân theo tháng trong năm.
                    Mỗi topic có danh sách 12 tháng, tháng nào không có bài viết trả về count = 0.
                    Chỉ tính các topic và bài viết chưa bị xoá (deleted = false).
                    Nếu không truyền `year`, mặc định là năm hiện tại.
                    `grand_total` là tổng bài viết của tất cả topic trong năm.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 200,
                                      "message": "Success",
                                      "data": {
                                        "year": 2026,
                                        "topics": [
                                          {
                                            "topic_id": 1,
                                            "topic_name": "Thời sự",
                                            "monthly_data": [
                                              { "month": 1, "month_name": "Jan", "count": 20 },
                                              { "month": 2, "month_name": "Feb", "count": 35 },
                                              { "month": 3, "month_name": "Mar", "count": 0 }
                                            ],
                                            "total": 55
                                          }
                                        ],
                                        "grand_total": 55
                                      },
                                      "timestamp": "2026-04-30T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực (thiếu hoặc sai token)"),
            @ApiResponse(responseCode = "403", description = "Không có quyền ADMIN")
    })
    @GetMapping("/articles/by-topic")
    public ResponseGeneral<ArticleByTopicResponse> getArticleCountByTopic(
            @Parameter(description = "Năm cần thống kê (ví dụ: 2026). Mặc định là năm hiện tại nếu bỏ trống.")
            @RequestParam(required = false) Integer year,
            @Parameter(hidden = true)
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleCountByTopic) year: {}", year);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleCountByTopic(year)
        );
    }

    @Operation(
            summary = "Thống kê bài viết theo ngày và theo chủ đề",
            description = """
                    Trả về số lượng bài viết trong **từng ngày** của một tháng, phân theo **từng chủ đề**.
                    - Nếu không truyền `year` hoặc `month`, mặc định là tháng/năm hiện tại.
                    - Nếu không truyền `topic_id`, trả về **tất cả** chủ đề có bài viết trong tháng đó.
                    - Nếu truyền `topic_id`, chỉ trả về dữ liệu của chủ đề đó.
                    - Mỗi chủ đề có đủ số ngày trong tháng; ngày không có bài viết trả về `count = 0`.
                    - `grand_total` là tổng bài viết của tất cả chủ đề trong kết quả.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 200,
                                      "message": "Success",
                                      "data": {
                                        "year": 2026,
                                        "month": 4,
                                        "total_days": 30,
                                        "topics": [
                                          {
                                            "topic_id": 1,
                                            "topic_name": "Thời sự",
                                            "days": [
                                              { "day": 1, "count": 3 },
                                              { "day": 2, "count": 0 },
                                              { "day": 3, "count": 7 }
                                            ],
                                            "total": 10
                                          },
                                          {
                                            "topic_id": 2,
                                            "topic_name": "Kinh doanh",
                                            "days": [
                                              { "day": 1, "count": 1 },
                                              { "day": 2, "count": 4 },
                                              { "day": 3, "count": 0 }
                                            ],
                                            "total": 5
                                          }
                                        ],
                                        "grand_total": 15
                                      },
                                      "timestamp": "2026-04-30T10:00:00"
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Chưa xác thực"),
            @ApiResponse(responseCode = "403", description = "Không có quyền ADMIN")
    })
    @GetMapping("/articles/by-topic/daily")
    public ResponseGeneral<ArticleTopicDailyResponse> getArticleCountByTopicAndDay(
            @Parameter(description = "Năm cần thống kê (ví dụ: 2026). Mặc định là năm hiện tại.")
            @RequestParam(required = false) Integer year,
            @Parameter(description = "Tháng cần thống kê, từ 1–12 (ví dụ: 4). Mặc định là tháng hiện tại.")
            @RequestParam(required = false) Integer month,
            @Parameter(description = "ID chủ đề cần lọc. Nếu bỏ trống, trả về tất cả chủ đề.")
            @RequestParam(name = "topic_id", required = false) Long topicId,
            @Parameter(hidden = true)
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        log.info("(getArticleCountByTopicAndDay) year: {}, month: {}, topicId: {}", year, month, topicId);

        return ResponseGeneral.ofSuccess(
                messageService.getMessage(SUCCESS, language),
                dashboardFacadeService.getArticleCountByTopicAndDay(year, month, topicId)
        );
    }
}
