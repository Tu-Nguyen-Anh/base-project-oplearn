package org.oplearn.project.service.authenticate;


import io.jsonwebtoken.Claims;
import org.oplearn.project.dto.request.authenticate.CreateTokenRequest;
import org.oplearn.project.entity.user.enums.TokenType;

public interface TokenService {
  String createToken(CreateTokenRequest request);

  String getTokenSubject(String token, TokenType tokenType);

  Boolean validateToken(String token, String usernameLogin, TokenType tokenType);

  Claims extractAllClaims(String token, TokenType tokenType);
}
