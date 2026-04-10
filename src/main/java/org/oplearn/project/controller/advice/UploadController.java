package org.oplearn.project.controller.advice;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.response.ResponseGeneral;
import org.oplearn.project.dto.response.UploadResponse;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.service.StorageService;
import org.oplearn.project.service.base.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.DEFAULT_LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.LANGUAGE;
import static org.oplearn.project.constanst.OpLearnConstants.Message.SUCCESS;
import static org.oplearn.project.constanst.OpLearnConstants.StorageConstants.FEEDBACK_IMAGE_FOLDER;
import static org.oplearn.project.constanst.OpLearnConstants.StorageConstants.MAX_IMAGE_SIZE_BYTES;
import static org.oplearn.project.constanst.OpLearnConstants.StorageConstants.MAX_IMAGES_PER_REQUEST;
import static org.oplearn.project.constanst.OpLearnConstants.StorageException.FILE_TOO_LARGE;
import static org.oplearn.project.constanst.OpLearnConstants.StorageException.INVALID_FILE_TYPE;

@Tag(name = "Upload", description = "API upload file (ảnh) lên RustFS storage")
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final StorageService storageService;
    private final MessageService messageService;

    @Operation(
            summary = "Upload ảnh",
            description = "Upload tối đa 10 ảnh (JPEG/PNG/GIF/WebP), mỗi ảnh tối đa 10MB. " +
                    "Trả về danh sách URL để dùng khi tạo góp ý.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Upload thành công"),
                    @ApiResponse(responseCode = "400", description = "File không hợp lệ hoặc quá dung lượng"),
                    @ApiResponse(responseCode = "401", description = "Chưa xác thực")
            }
    )
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseGeneral<List<UploadResponse>> uploadImages(
            @RequestParam("files") MultipartFile[] files,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        if (files.length == 0) {
            throw new BadRequestException(INVALID_FILE_TYPE);
        }
        if (files.length > MAX_IMAGES_PER_REQUEST) {
            throw new BadRequestException(INVALID_FILE_TYPE);
        }

        List<UploadResponse> responses = Arrays.stream(files).map(file -> {
            if (!storageService.isValidImageType(file)) {
                log.warn("(uploadImages) Invalid type: {}", file.getContentType());
                throw new BadRequestException(INVALID_FILE_TYPE);
            }
            if (file.getSize() > MAX_IMAGE_SIZE_BYTES) {
                log.warn("(uploadImages) File too large: {} bytes", file.getSize());
                throw new BadRequestException(FILE_TOO_LARGE);
            }

            String url = storageService.upload(file, FEEDBACK_IMAGE_FOLDER);
            log.info("(uploadImages) Uploaded: {} → {}", file.getOriginalFilename(), url);

            return UploadResponse.builder()
                    .url(url)
                    .originalName(file.getOriginalFilename())
                    .size(file.getSize())
                    .contentType(file.getContentType())
                    .build();
        }).toList();

        return ResponseGeneral.ofSuccess(messageService.getMessage(SUCCESS, language), responses);
    }
}
