package org.oplearn.project.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.user.enums.TokenType;
import org.oplearn.project.exception.base.BaseException;
import org.oplearn.project.service.UserService;
import org.oplearn.project.service.authenticate.TokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.oplearn.project.entity.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.BEARER_TOKEN_TYPE_START;
import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.NEWS_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("=== Start filter jwt");
        log.debug("Request: {}", request.toString());

        final var accessTokenBearer = request.getHeader(AUTHORIZATION);

        if (Objects.isNull(accessTokenBearer) || !accessTokenBearer.startsWith(BEARER_TOKEN_TYPE_START)) {
            log.debug("Token null or invalid, token: {}", accessTokenBearer);
            filterChain.doFilter(request, response);
            return;
        }
        final var accessToken = accessTokenBearer.substring(BEARER_TOKEN_TYPE_START.length());
        log.debug("Access_token: {}", accessToken);

        try {
            final var userId = Long.parseLong(tokenService.getTokenSubject(accessToken, TokenType.ACCESS_TOKEN));

            log.debug("UserId: {}", userId);

            var accessTokenRedisKey = NEWS_PREFIX + "_" + userId + "_" + TokenType.ACCESS_TOKEN.name();

            var accessTokenOnRedis = redisTemplate.opsForValue().get(accessTokenRedisKey);

            if (!Objects.equals(accessTokenOnRedis, accessToken)) {
                log.debug("Token not found on system(redis)");
                filterChain.doFilter(request, response);
                return;
            }

            final var user = userService.getById(userId);
            log.debug("User: {}", user);
            if (Objects.isNull(user)) {
                log.debug("User not found with id: {}", userId);
                filterChain.doFilter(request, response);
                return;
            }

            UserRole userRole = user.getRole() != null ? user.getRole() : UserRole.USER;
            List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + userRole.name())
            );

            var authentication = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            handleException(e, response, HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
        } catch (SignatureException | MalformedJwtException e) {
            handleException(e, response, HttpServletResponse.SC_UNAUTHORIZED, "Token invalid");
        } catch (BaseException e) {
            handleException(e, response, e.getStatus(), e.getMessage());
        } catch (Exception e) {
            handleException(e, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void handleException(Exception e, HttpServletResponse response, int status, String message) throws IOException {
        log.error("(doFilterInternal): {}", e.getMessage());
        response.sendError(status, message);
    }
}
