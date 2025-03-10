package org.oplearn.project.controller.advice;

import org.oplearn.project.entity.User;
import org.oplearn.project.service.base.BaseRedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/redis")
public class RedisController {

  private final BaseRedisService baseRedisService;

  public RedisController(BaseRedisService baseRedisService) {
    this.baseRedisService = baseRedisService;
  }

/*  @PostMapping("/{id}")
  public ResponseEntity<String> saveUser(@PathVariable("id") Long id, @RequestBody User user) {
    String key = "user:" + id;
    redisObjectService.saveObject(key, "pldfdf");
    return ResponseEntity.ok("User saved successfully to Redis.");
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
    String key = "user:" + id;
    User user = redisObjectService.get(key, User.class);
    return ResponseEntity.ok(user);
  }

  @GetMapping("n/{id}")
  public ResponseEntity<String> getString(@PathVariable("id") Long id) {
    String key = "user:" + id;
    String user = redisObjectService.get(key, String.class);
    return ResponseEntity.ok(user);
  }*/


  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable("id") String id) {
    User user = (User) baseRedisService.get(id);
    return ResponseEntity.ok(user);
  }

  // Endpoint lưu danh sách User vào Redis
  @PostMapping("/users")
  public ResponseEntity<String> saveAllUsers(@RequestBody User users) {
    baseRedisService.set("oks", users);
    return ResponseEntity.ok("All users saved successfully to Redis.");
  }

  @PostMapping("/users/json")
  public ResponseEntity<List<User>> getUsersAsJson(@RequestBody List<String> keys) {
    List<User> usersJson = baseRedisService.getAllByKeys(keys);
    return ResponseEntity.ok(usersJson);
  }

  @DeleteMapping
  public ResponseEntity<String> deleteMultipleKeys(@RequestBody List<String> keys) {
    Set<String> uniqueKeys = new HashSet<>(keys); // Loại bỏ trùng lặp
    baseRedisService.deleteAllByKeys(uniqueKeys);
    return ResponseEntity.ok("Deleted keys: " + uniqueKeys);
  }

  @DeleteMapping("/all")
  public ResponseEntity<String> deleteAllKeys() {
    baseRedisService.deleteAll(); // Gọi hàm xóa tất cả key
    return ResponseEntity.ok("All keys deleted successfully.");
  }


}
