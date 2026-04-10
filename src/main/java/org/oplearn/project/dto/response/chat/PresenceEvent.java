package org.oplearn.project.dto.response.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PresenceEvent {
    private Long userId;
    private String username;
    private String fullName;
    private String avatar;
    private boolean online;
    private Long timestamp;
    /** Thời điểm offline lần cuối (epoch ms). Chỉ có mặt khi online = false. */
    private Long lastSeen;

    public static PresenceEvent online(Long userId, String username, String fullName, String avatar) {
        return PresenceEvent.builder()
                .userId(userId)
                .username(username)
                .fullName(fullName)
                .avatar(avatar)
                .online(true)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static PresenceEvent offline(Long userId, String username, String fullName, String avatar, Long lastSeen) {
        return PresenceEvent.builder()
                .userId(userId)
                .username(username)
                .fullName(fullName)
                .avatar(avatar)
                .online(false)
                .timestamp(System.currentTimeMillis())
                .lastSeen(lastSeen)
                .build();
    }
}
