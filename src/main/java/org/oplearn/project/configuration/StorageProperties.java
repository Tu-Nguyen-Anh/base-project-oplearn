package org.oplearn.project.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "storage.rustfs")
public class StorageProperties {
    /** S3 endpoint của RustFS, ví dụ: http://vps-ip:9008 */
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region = "us-east-1";
    /** URL public để client truy cập file, thường giống endpoint */
    private String publicUrl;
}
