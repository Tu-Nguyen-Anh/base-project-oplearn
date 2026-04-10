package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.service.PresenceService;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.LAST_SEEN_TTL_DAYS;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.ONLINE_TTL_MINUTES;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.USER_LAST_SEEN_KEY_PREFIX;
import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.USER_ONLINE_KEY_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceServiceImpl implements PresenceService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setOnline(Long userId) {
        redisTemplate.opsForValue().set(USER_ONLINE_KEY_PREFIX + userId, "1", ONLINE_TTL_MINUTES, TimeUnit.MINUTES);
        log.debug("(setOnline) userId: {}", userId);
    }

    @Override
    public void setOffline(Long userId) {
        // Lưu lastSeen trước khi xóa key online
        long now = System.currentTimeMillis();
        redisTemplate.opsForValue().set(
                USER_LAST_SEEN_KEY_PREFIX + userId,
                String.valueOf(now),
                LAST_SEEN_TTL_DAYS, TimeUnit.DAYS);
        redisTemplate.delete(USER_ONLINE_KEY_PREFIX + userId);
        log.debug("(setOffline) userId: {}, lastSeen: {}", userId, now);
    }

    @Override
    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(USER_ONLINE_KEY_PREFIX + userId));
    }

    /**
     * Batch check online status sử dụng Redis pipeline (1 round-trip thay vì N).
     */
    @Override
    public Set<Long> getOnlineUserIds(List<Long> userIds) {
        if (userIds.isEmpty()) return Set.of();

        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Long userId : userIds) {
                byte[] key = (USER_ONLINE_KEY_PREFIX + userId).getBytes(StandardCharsets.UTF_8);
                connection.keyCommands().exists(key);
            }
            return null;
        });

        Set<Long> onlineIds = new HashSet<>();
        for (int i = 0; i < userIds.size(); i++) {
            Object result = results.get(i);
            if (Boolean.TRUE.equals(result) || (result instanceof Long l && l > 0)) {
                onlineIds.add(userIds.get(i));
            }
        }
        return onlineIds;
    }

    @Override
    public Long getLastSeen(Long userId) {
        String value = redisTemplate.opsForValue().get(USER_LAST_SEEN_KEY_PREFIX + userId);
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            log.warn("(getLastSeen) invalid value for userId: {}", userId);
            return null;
        }
    }
}
