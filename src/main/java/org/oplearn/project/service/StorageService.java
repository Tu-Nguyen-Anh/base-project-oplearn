package org.oplearn.project.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Upload file lên RustFS bucket.
     *
     * @param file   file cần upload
     * @param folder thư mục lưu trong bucket (vd: "feedbacks", "avatars")
     * @return public URL của file đã upload
     */
    String upload(MultipartFile file, String folder);

    /**
     * Xóa file khỏi RustFS bucket theo object key.
     *
     * @param objectKey key trong bucket (vd: "feedbacks/uuid.jpg")
     */
    void delete(String objectKey);

    /**
     * Trích xuất object key từ public URL.
     * URL format: {publicUrl}/{bucket}/{key}
     */
    String extractKey(String publicUrl);

    /** Kiểm tra content type có phải image hợp lệ. */
    boolean isValidImageType(MultipartFile file);
}
