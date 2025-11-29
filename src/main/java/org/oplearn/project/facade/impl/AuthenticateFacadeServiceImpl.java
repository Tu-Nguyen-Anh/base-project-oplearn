package org.oplearn.project.facade.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.configuration.PropertiesConfiguration;
import org.oplearn.project.dto.request.authenticate.CreateTokenRequest;
import org.oplearn.project.dto.request.authenticate.LoginRequest;
import org.oplearn.project.dto.request.authenticate.RefreshTokenRequest;
import org.oplearn.project.dto.response.authenticate.LoginResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.entity.user.enums.TokenType;
import org.oplearn.project.exception.base.UnauthorizedException;
import org.oplearn.project.exception.base.auth.TokenInvalidException;
import org.oplearn.project.exception.base.auth.UsernameOrEmailInvalidException;
import org.oplearn.project.exception.base.user.PasswordIncorrectException;
import org.oplearn.project.exception.base.user.UserNotActiveException;
import org.oplearn.project.facade.AuthenticateFacadeService;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.authenticate.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.oplearn.project.constanst.OpLearnConstants.ActiveStatus.INACTIVE;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.NEWS_PREFIX;
import static org.oplearn.project.security.UserAuthenticated.getCurrentUser;
import static org.oplearn.project.utils.PasswordEncoderUtils.getPasswordEncoder;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateFacadeServiceImpl implements AuthenticateFacadeService {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenService tokenService;
    private final PropertiesConfiguration propertiesConfiguration;
    @Override
    public LoginResponse authenticate(LoginRequest request) {
        log.info("=== Start authenticate user: {}", request);
        User user;
        try {
            user = userService.findByUsername(request.username());
        } catch (Exception e) {
            throw new PasswordIncorrectException();
        }


        if (user.getStatus() == INACTIVE) {
            log.error("UserNotActiveException");

            throw new UserNotActiveException();
        }
        this.equalPassword(request.password(), user.getPassword());

        var tokenData = buildTokenData(user);

        final var accessToken = createToken(user.getId(), tokenData, TokenType.ACCESS_TOKEN);
        final var refreshToken = createToken(user.getId(), tokenData, TokenType.REFRESH_TOKEN);


        return new LoginResponse(
                user.getId(),
                accessToken,
                refreshToken,
                propertiesConfiguration.getAccessTokenTtl(),
                propertiesConfiguration.getRefreshTokenTtl()
        );
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        log.info("=== Start refresh token with request: {}", request);

        final var refreshTokenRequest = request.refreshToken();

        if (Objects.isNull(refreshTokenRequest) || refreshTokenRequest.isBlank()) {
            log.error("Refresh token is null or blank");

            throw new TokenInvalidException();
        }

        final var userId = Long.parseLong(tokenService.getTokenSubject(refreshTokenRequest, TokenType.REFRESH_TOKEN));
        final var user = userService.getById(userId);

        final var refreshTokenRefreshKey = this.createTokenRedisKey(userId, TokenType.REFRESH_TOKEN);
        final var refreshTokenOnRedis = redisTemplate.opsForValue().get(refreshTokenRefreshKey);

        if (!Objects.equals(refreshTokenOnRedis, refreshTokenRequest)) {
            log.error("Refresh token is invalid");

            throw new TokenInvalidException();
        }

        final var tokenData = buildTokenData(user);

        final var accessToken = createToken(user.getId(), tokenData, TokenType.ACCESS_TOKEN);
        final var refreshToken = createToken(user.getId(), tokenData, TokenType.REFRESH_TOKEN);

        return new LoginResponse(
                user.getId(),
                accessToken,
                refreshToken,
                propertiesConfiguration.getAccessTokenTtl(),
                propertiesConfiguration.getRefreshTokenTtl()
        );
    }


    @Override
    public void logout() {
        User user = getCurrentUser().orElseThrow(
                () -> {
                    log.error("User not found");
                    return new UnauthorizedException();
                });
        log.info("=== Start logout with userId: {}", user.getId());


        redisTemplate.delete(createTokenRedisKey(user.getId(), TokenType.ACCESS_TOKEN));
        redisTemplate.delete(createTokenRedisKey(user.getId(), TokenType.REFRESH_TOKEN));
        SecurityContextHolder.clearContext();
    }

    private Map<String, String> buildTokenData(User user) {
        final var tokenData = new HashMap<String, String>();
        tokenData.put("username", user.getUsername());
        tokenData.put("full_name", user.getFullName());
        tokenData.put("user_id", String.valueOf(user.getId()));

        return tokenData;
    }

    private String createToken(Long subject, Map<String, String> data, TokenType tokenType) {
        log.debug("=== Start create token for subject: {}", subject);

        Long expired = null;
        if (Objects.equals(tokenType, TokenType.ACCESS_TOKEN)) {
            expired = propertiesConfiguration.getAccessTokenTtl();
        } else if (Objects.equals(tokenType, TokenType.REFRESH_TOKEN)) {
            expired = propertiesConfiguration.getRefreshTokenTtl();
        }

        final var createTokenRequest = new CreateTokenRequest(subject, tokenType, expired, data);
        final String token = tokenService.createToken(createTokenRequest);
        final Long ttl = tokenType.equals(TokenType.ACCESS_TOKEN) ?
                propertiesConfiguration.getAccessTokenTtl() :
                propertiesConfiguration.getRefreshTokenTtl();

        redisTemplate.opsForValue().set(
                createTokenRedisKey(subject, tokenType),
                token,
                ttl,
                TimeUnit.MILLISECONDS
        );

        return token;
    }


    private void equalPassword(String passwordRaw, String passwordEncrypted) {
        if (!getPasswordEncoder().matches(passwordRaw, passwordEncrypted)) {
            throw new PasswordIncorrectException();
        }
    }

    private String createTokenRedisKey(Long userId, TokenType tokenType) {
        return NEWS_PREFIX + "_" + userId + "_" + tokenType.name();
    }

    @Override
    public void setUserToSecurityContext(User user) {
        log.debug("=== Setting user {} to SecurityContextHolder", user.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void checkUserValidWhenForgotPassword(User user) {
        log.debug("(checkUserValidWhenForgotPassword) user: {}", user);

        if (user == null) {
            throw new UsernameOrEmailInvalidException();
        }

        if (user.getStatus() == INACTIVE) {
            throw new UserNotActiveException();
        }
    }

}
