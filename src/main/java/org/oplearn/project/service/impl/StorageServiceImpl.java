package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.configuration.StorageProperties;
import org.oplearn.project.exception.base.BadRequestException;
import org.oplearn.project.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import static org.oplearn.project.constanst.OpLearnConstants.StorageConstants.ALLOWED_IMAGE_TYPES;
import static org.oplearn.project.constanst.OpLearnConstants.StorageException.INVALID_FILE_TYPE;
import static org.oplearn.project.constanst.OpLearnConstants.StorageException.UPLOAD_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final S3Client s3Client;
    private final StorageProperties storageProperties;

    @Override
    public String upload(MultipartFile file, String folder) {
        String key = generateKey(folder, file.getOriginalFilename());
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(storageProperties.getBucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            log.error("(upload) Failed to read file: {}", file.getOriginalFilename(), e);
            throw new BadRequestException(UPLOAD_FAILED);
        } catch (Exception e) {
            log.error("(upload) Failed to upload to RustFS, key: {}", key, e);
            throw new BadRequestException(UPLOAD_FAILED);
        }

        String publicUrl = buildPublicUrl(key);
        log.info("(upload) Uploaded key: {} → {}", key, publicUrl);
        return publicUrl;
    }

    @Override
    public void delete(String objectKey) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(storageProperties.getBucket())
                    .key(objectKey)
                    .build());
            log.info("(delete) Deleted object key: {}", objectKey);
        } catch (Exception e) {
            log.warn("(delete) Failed to delete object key: {}", objectKey, e);
        }
    }

    @Override
    public String extractKey(String publicUrl) {
        String prefix = storageProperties.getPublicUrl() + "/" + storageProperties.getBucket() + "/";
        if (!publicUrl.startsWith(prefix)) {
            throw new BadRequestException(INVALID_FILE_TYPE);
        }
        return publicUrl.substring(prefix.length());
    }

    @Override
    public boolean isValidImageType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && Set.of(ALLOWED_IMAGE_TYPES).contains(contentType);
    }

    private String generateKey(String folder, String originalFilename) {
        String ext = getExtension(originalFilename);
        return folder + "/" + UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private String buildPublicUrl(String key) {
        return storageProperties.getPublicUrl() + "/" + storageProperties.getBucket() + "/" + key;
    }
}
