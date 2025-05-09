package org.oplearn.project.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.oplearn.project.exception.redis.RedisSerializationException;

public class JsonUtils {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> String objectToString(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RedisSerializationException("Failed to deserialize object to json" + ": " + e.getMessage(), e);
    }
  }

  public static <T> T stringToObject(String str, Class<T> clazz) {
    if (!isValidJson(str)) {
      throw new RedisSerializationException("Invalid JSON string: " + str);
    }
    try {
      return objectMapper.readValue(str, clazz);
    } catch (JsonProcessingException e) {
      throw new RedisSerializationException("Failed to deserialize JSON to " + clazz.getName() + ": " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      throw new RedisSerializationException("Invalid argument during deserialization to " + clazz.getName() + ": " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RedisSerializationException("Unexpected error during deserialization to " + clazz.getName() + ": " + e.getMessage(), e);
    }
  }

  public static boolean isValidJson(String str) {
    if (str == null || str.trim().isEmpty()) {
      return false;
    }
    try {
      objectMapper.readTree(str);
      return true;
    } catch (JsonProcessingException e) {
      return false;
    }
  }
}
