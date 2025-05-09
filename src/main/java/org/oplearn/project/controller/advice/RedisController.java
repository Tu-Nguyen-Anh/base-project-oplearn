package org.oplearn.project.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.service.base.BaseRedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@Slf4j
public class RedisController<K, V, T> {

  private final BaseRedisService<K, V, T> redisService;

  public RedisController(BaseRedisService<K, V, T> redisService) {
    this.redisService = redisService;
  }
}
