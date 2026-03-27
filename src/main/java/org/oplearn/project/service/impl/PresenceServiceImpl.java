package org.oplearn.project.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.service.PresenceService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.oplearn.project.constanst.OpLearnConstants.ChatPresence.ONLINE_TTL_MINUTES;
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
        redisTemplate.delete(USER_ONLINE_KEY_PREFIX + userId);
        log.debug("(setOffline) userId: {}", userId);
    }

    @Override
    public boolean isOnline(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(USER_ONLINE_KEY_PREFIX + userId));
    }

    @Override
    public Set<Long> getOnlineUserIds(List<Long> userIds) {
        return userIds.stream()
                .filter(this::isOnline)
                .collect(Collectors.toSet());
    }
}
