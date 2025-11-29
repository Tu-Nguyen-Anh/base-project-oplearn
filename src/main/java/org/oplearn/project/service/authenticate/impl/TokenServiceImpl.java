package org.oplearn.project.service.authenticate.impl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.configuration.PropertiesConfiguration;
import org.oplearn.project.dto.request.authenticate.CreateTokenRequest;
import org.oplearn.project.entity.user.enums.TokenType;
import org.oplearn.project.exception.base.InternalServerError;
import org.oplearn.project.exception.base.auth.TokenInvalidException;
import org.oplearn.project.service.authenticate.TokenService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final PropertiesConfiguration propertiesConfiguration;

  @Override
  public String createToken(CreateTokenRequest request) {
    log.info("=== Start create token with request: {}", request);

    var now = new Date();
    var expired = new Date(now.getTime() + request.expiredSeconds());

    return Jwts.builder()
          .setClaims(request.data())
          .setSubject(String.valueOf(request.subject()))
          .setIssuedAt(now)
          .setExpiration(expired)
          .signWith(getSecretKey(request.tokenType()), SignatureAlgorithm.HS256).compact();
  }

  @Override
  public String getTokenSubject(String token, TokenType tokenType) {
    log.debug("=== Start get token: {} subject with type: {}", token, tokenType);
    try {
      return this.extractUserId(token, tokenType);
    } catch (ExpiredJwtException | MalformedJwtException e) {
      log.error("=== (getTokenSubject) message: {}", e.getMessage());
      throw new TokenInvalidException();
    } catch (Exception e) {
      log.error("=== (getTokenSubject) message: {}", e.getMessage());
      throw new InternalServerError();
    }
  }

  @Override
  public Boolean validateToken(String token, String userId, TokenType tokenType) {
    final String username = extractUserId(token, tokenType);
    return (Objects.equals(username, userId) && !isTokenExpired(token, tokenType));
  }

  private Key getSecretKey(TokenType tokenType) {
    switch (tokenType) {
      case ACCESS_TOKEN -> {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(propertiesConfiguration.getAccessTokenSecretKey()));
      }
      case REFRESH_TOKEN -> {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(propertiesConfiguration.getRefreshTokenSecretKey()));
      }
      default -> {
        log.info("TokenType: {} is not supported", tokenType);
        throw new InternalServerError();
      }
    }
  }

  private String extractUserId(String token, TokenType tokenType) {
    return extractClaim(token, Claims::getSubject, tokenType);
  }

  private Date extractExpiration(String token, TokenType tokenType) {
    return extractClaim(token, Claims::getExpiration, tokenType);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver, TokenType tokenType) {
    final Claims claims = extractAllClaims(token, tokenType);
    return claimsResolver.apply(claims);
  }

  public Claims extractAllClaims(String token, TokenType tokenType) {
    return Jwts
          .parserBuilder()
          .setSigningKey(getSecretKey(tokenType))
          .build()
          .parseClaimsJws(token)
          .getBody();
  }

  private Boolean isTokenExpired(String token, TokenType tokenType) {
    return extractExpiration(token, tokenType).before(new Date());
  }

}
