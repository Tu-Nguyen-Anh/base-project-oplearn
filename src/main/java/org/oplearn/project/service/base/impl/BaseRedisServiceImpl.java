package org.oplearn.project.service.base.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.service.base.BaseRedisService;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;


@Slf4j
@Service
@RequiredArgsConstructor
public class BaseRedisServiceImpl<K, V, T> implements BaseRedisService<K, V, T> {
  private final RedisTemplate<K, V> redisTemplate;

  /**
   * Stores a value in Redis with the given key.
   * If the key already exists, the value will be overwritten.
   * @param key The key to store the value.
   * @param value The value to be stored in Redis.
   */
  @Override
  public void set(K key, V value) {
    redisTemplate.opsForValue().set(key, value);
  }

  /**
   * Sets the expiration time for a specific key in Redis.
   * @param key The key for which the TTL will be set.
   * @param timeToLive The time duration before the key expires.
   * @param timeUnit The time unit (e.g., seconds, minutes).
   */
  @Override
  public void setTimeToLive(K key, long timeToLive, TimeUnit timeUnit) {
    redisTemplate.expire(key, timeToLive, timeUnit);
  }


  /**
   * Retrieves a value from Redis based on the given key.
   * @param key The key whose associated value is to be returned.
   * @return The value stored in Redis, or null if the key does not exist.
   */
  @Override
  public V get(K key) {
    return redisTemplate.opsForValue().get(key);
  }


/**
 * Retrieves multiple values from Redis based on a list of keys.
 * @param keys A list of keys whose values need to be retrieved.
 * @return A list of values corresponding to the provided keys.
 */
  @Override
  public List<V> getAllByKeys(List<K> keys) {
    return redisTemplate.opsForValue().multiGet(keys);
  }

  /**
   * Retrieves a value from Redis. If the key does not exist,
   * the provided Supplier function will be used to generate a value,
   * which is then stored in Redis for future use.
   * @param key The key whose value is to be retrieved.
   * @param valueSupplier A Supplier function to provide a value if the key is not found.
   * @return The retrieved or newly generated value.
   */
  @Override
  public V get(K key, Supplier<V> valueSupplier) {
    V value = redisTemplate.opsForValue().get(key);

    if (value == null) {
      value = valueSupplier.get();
      this.set(key, value);
    }
    return value;
  }

  /**
   * Increments the numeric value stored at a given key by the specified amount.
   * If the key does not exist, it will be initialized to 0 before the increment operation.
   * @param key The key whose value needs to be incremented.
   * @param value The amount by which to increment the value.
   * @return The new value after incrementing.
   */
  @Override
  public Long increment(K key, long value) {
    return redisTemplate.opsForValue().increment(key, value);
  }

  /**
   * Deletes a specific key from Redis.
   * @param key The key to be deleted.
   */
  @Override
  public void delete(K key) {
    redisTemplate.delete(key);
  }

  /**
   * Deletes a key if it exists in Redis.
   * @param key The key to be removed from Redis.
   */
  @Override
  public void unset(K key) {
    if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
      redisTemplate.delete(key);
    }
  }


  /**
   * Deletes multiple keys from Redis in a single operation.
   * @param keys A set of keys to be deleted.
   */
  @Override
  public void deleteAllByKeys(Set<K> keys) {
    if (keys != null && !keys.isEmpty()) {
      redisTemplate.delete(keys);
    }
  }

/**
 * Deletes all keys from Redis.
 * WARNING: This will remove all data stored in Redis!
 */
  @Override
  public void deleteAll() {
    RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
    if (connectionFactory != null) {
      connectionFactory.getConnection().serverCommands().flushAll();
    }
  }

  /**
   * Inserts or updates a value in Redis.
   * If the key already exists, its value is updated.
   * If the key does not exist, it is created.
   * @param key The key to be updated or created.
   * @param newValue The new value to be stored in Redis.
   */
  @Override
  public void upsert(K key, V newValue) {
    redisTemplate.opsForValue().set(key, newValue);
  }

  /**
   * Saves a value in Redis but ensures that it does not overwrite an existing key.
   * If the key does not exist, it is created with a time-to-live (TTL).
   * If the key already exists, the operation fails.
   * @param key The key to store the value.
   * @param value The value to store in Redis.
   * @param timeout The duration for which the key should exist.
   * @param timeUnit The time unit of the TTL.
   */
  @Override
  public void saveAndLock(K key, V value, long timeout, TimeUnit timeUnit) {
    redisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
  }

  @Override
  public Set<String> scanKeys(String pattern) {
    ScanOptions options = ScanOptions.scanOptions().match(pattern).count(1000).build();
    Set<String> keys = new HashSet<>();

    RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
    if (connectionFactory != null) {
      try (Cursor<byte[]> cursor = connectionFactory.getConnection().scan(options)) {
        while (cursor.hasNext()) {
          keys.add(new String(cursor.next()));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return keys;
  }



}
