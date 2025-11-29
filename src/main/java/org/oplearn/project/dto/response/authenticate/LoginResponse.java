package org.oplearn.project.dto.response.authenticate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import static org.oplearn.project.constanst.OpLearnConstants.CommonConstants.TOKEN_TYPE;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LoginResponse(Long id,
                            String accessToken,
                            String refreshToken,
                            long tokenExpiredSeconds,
                            long refreshExpiredSeconds,
                            String tokenType
) {
  public LoginResponse(Long id,
                       String accessToken,
                       String refreshToken,
                       long tokenExpiredSeconds,
                       long refreshExpiredSeconds
  ) {
    this(id, accessToken, refreshToken, tokenExpiredSeconds, refreshExpiredSeconds, TOKEN_TYPE);
  }
}


