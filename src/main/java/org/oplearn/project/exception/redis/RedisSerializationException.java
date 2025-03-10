package org.oplearn.project.exception.redis;

import org.oplearn.project.exception.base.BadRequestException;

public class RedisSerializationException extends RuntimeException {

  public RedisSerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public RedisSerializationException(String message) {
    super(message);
  }}
