package org.oplearn.project.service.base;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface BaseRedisService<K, V, T> {
  void set(K key, V value);

  void setTimeToLive(K key, long timeToLive, TimeUnit timeUnit);

  V get(K key);

  V get(K key, Supplier<V> valueSupplier);

  Long increment(K key, long value);

  List<V> getAllByKeys(List<K> keys);

  void deleteAllByKeys(Set<K> keys);

  void deleteAll();

  void upsert(K key, V newValue);

  void unset(K key);

  void saveAndLock(K key, V value, long timeout, TimeUnit timeUnit);

  void setObject(String key, Object value);

  <T> T getObject(String key, Class<T> clazz);

  boolean exists(K key);

  Set<String> getAllKeys();

  Map<String, String> getAllKeysWithValues();

  Set<String> getKeysByPattern(String pattern);

  Map<String, String> getKeysAndValuesByPattern(String pattern);

}
