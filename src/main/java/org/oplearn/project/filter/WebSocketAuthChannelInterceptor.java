package org.oplearn.project.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.user.enums.TokenType;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.authenticate.TokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.BEARER_TOKEN_TYPE_START;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.NEWS_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String authHeader = accessor.getFirstNativeHeader("Authorization");
        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER_TOKEN_TYPE_START)) {
            log.warn("WebSocket CONNECT without valid Authorization header");
            return message;
        }

        String accessToken = authHeader.substring(BEARER_TOKEN_TYPE_START.length());

        try {
            Long userId = Long.parseLong(tokenService.getTokenSubject(accessToken, TokenType.ACCESS_TOKEN));

            String redisKey = NEWS_PREFIX + "_" + userId + "_" + TokenType.ACCESS_TOKEN.name();
            String tokenOnRedis = redisTemplate.opsForValue().get(redisKey);

            if (!Objects.equals(tokenOnRedis, accessToken)) {
                log.warn("WebSocket token not found in Redis for userId: {}", userId);
                return message;
            }

            var user = userService.getById(userId);
            if (Objects.isNull(user)) {
                log.warn("WebSocket user not found with id: {}", userId);
                return message;
            }

            var authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            accessor.setUser(authentication);
            log.debug("WebSocket authenticated user: {}", user.getUsername());

        } catch (Exception e) {
            log.error("WebSocket authentication failed: {}", e.getMessage());
        }

        return message;
    }
}
