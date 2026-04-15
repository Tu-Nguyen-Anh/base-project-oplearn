# SOAR NEWS - Tài liệu API

> **Phiên bản:** 2.0.0
> **Cập nhật lần cuối:** 2026-04-10
> **Base URL:** `http://localhost:8188/api/v1`
> **Swagger UI:** `http://localhost:8188/swagger-ui.html`

---

## MỤC LỤC

1. [Thông tin chung](#1-thông-tin-chung)
2. [Xác thực & Phân quyền](#2-xác-thực--phân-quyền)
3. [Cấu trúc Response](#3-cấu-trúc-response)
4. [Module: Xác thực (Auth)](#4-module-xác-thực-auth)
5. [Module: Người dùng (User)](#5-module-người-dùng-user)
6. [Module: Nguồn tin (Source)](#6-module-nguồn-tin-source)
7. [Module: Chủ đề (Topic)](#7-module-chủ-đề-topic)
8. [Module: Bài viết (Article)](#8-module-bài-viết-article)
9. [Module: Cộng đồng (Blog Post)](#9-module-cộng-đồng-blog-post)
10. [Module: Tin nhắn (Chat)](#10-module-tin-nhắn-chat)
11. [Module: Dashboard](#11-module-dashboard)
12. [Module: Góp ý (Feedback)](#12-module-góp-ý-feedback)
13. [Module: Upload](#13-module-upload)
14. [Bảng mã lỗi](#14-bảng-mã-lỗi)
15. [Data Models](#15-data-models)

---

## 1. THÔNG TIN CHUNG

### Kết nối

| Thông số | Giá trị |
|---|---|
| **Base URL** | `http://localhost:8188` |
| **API Prefix** | `/api/v1` |
| **Swagger UI** | `http://localhost:8188/swagger-ui.html` |
| **API Docs JSON** | `http://localhost:8188/v3/api-docs` |
| **Content-Type** | `application/json` |
| **Encoding** | `UTF-8` |

### Tech stack

- **Framework:** Spring Boot 3.2.2 / Java 17
- **Database:** PostgreSQL (`news_db`)
- **Cache:** Redis (token + presence)
- **Authentication:** JWT Bearer Token
- **Migration:** Liquibase
- **WebSocket:** STOMP over SockJS

---

## 2. XÁC THỰC & PHÂN QUYỀN

### 2.1 Xác thực

Mọi request (trừ public endpoints) phải gửi kèm header:

```
Authorization: Bearer <access_token>
```

Token được lấy từ `POST /api/v1/auth/login`.

### 2.2 Hệ thống phân quyền (RBAC)

Hệ thống có **3 vai trò (Role)**:

| Role | Mô tả |
|------|-------|
| `ADMIN` | Toàn quyền tất cả màn hình |
| `AUTHOR` | Quyền đăng/sửa/xóa bài viết (articles) + toàn bộ quyền USER |
| `USER` | Xem nội dung, nhắn tin (chat), đăng bài cộng đồng (blog posts) |

### 2.3 Ma trận phân quyền chi tiết

| Chức năng | ADMIN | AUTHOR | USER |
|-----------|:-----:|:------:|:----:|
| **Users** |||||
| Tạo / Sửa / Xóa user | ✅ | ❌ | ❌ |
| Xem thông tin user | ✅ | ✅ | ✅ |
| Xem lịch sử user | ✅ | ❌ | ❌ |
| Filter danh sách user | ✅ | ❌ | ❌ |
| Reset password | ✅ | ❌ | ❌ |
| Đổi mật khẩu (của chính mình) | ✅ | ✅ | ✅ |
| **Articles (Bài báo)** |||||
| Tạo / Sửa / Xóa bài báo | ✅ | ✅ | ❌ |
| Xem / Filter bài báo | ✅ | ✅ | ✅ |
| Yêu thích, xem lịch sử | ✅ | ✅ | ✅ |
| Bình luận bài báo | ✅ | ✅ | ✅ |
| **Blog Posts (Cộng đồng)** |||||
| Tạo / Sửa / Xóa bài đăng | ✅ | ✅ | ✅ |
| Xem, like, share, comment | ✅ | ✅ | ✅ |
| **Topics (Chủ đề)** |||||
| Tạo / Sửa / Xóa topic | ✅ | ❌ | ❌ |
| Xem, follow topic | ✅ | ✅ | ✅ |
| **Sources (Nguồn tin)** |||||
| Tạo / Sửa / Xóa source | ✅ | ❌ | ❌ |
| Xem source | ✅ | ✅ | ✅ |
| **Chat (Tin nhắn)** |||||
| Tất cả chức năng chat | ✅ | ✅ | ✅ |
| **Dashboard** |||||
| Xem thống kê | ✅ | ❌ | ❌ |
| **Feedback (Góp ý)** |||||
| Gửi / Xem / Xóa góp ý của mình | ✅ | ✅ | ✅ |
| Quản lý tất cả góp ý | ✅ | ❌ | ❌ |
| **Upload** |||||
| Upload ảnh | ✅ | ✅ | ✅ |

### 2.4 Response khi không có quyền

- **401 Unauthorized**: Chưa đăng nhập hoặc token không hợp lệ
- **403 Forbidden**: Đã đăng nhập nhưng role không có quyền truy cập

---

## 3. CẤU TRÚC RESPONSE

### Response bao ngoài

```json
{
  "code": 200,
  "message": "Success",
  "data": { }
}
```

| Field | Type | Mô tả |
|-------|------|-------|
| `code` | int | HTTP status code |
| `message` | string | Thông báo |
| `data` | object/array/null | Dữ liệu trả về |

### Phân trang (PageResponse)

```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "content": [ ],
    "total_elements": 100
  }
}
```

---

## 4. MODULE: XÁC THỰC (AUTH)

### 4.1 Đăng nhập

```
POST /api/v1/auth/login
```

**Public** – Không cần token.

**Request body:**
```json
{
  "username": "admin",
  "password": "News@2025"
}
```

**Response 200:**
```json
{
  "code": 200,
  "message": "Success",
  "data": {
    "access_token": "<jwt>",
    "refresh_token": "<jwt>",
    "user_id": 1,
    "username": "admin",
    "full_name": "Administrator",
    "role": "ADMIN"
  }
}
```

---

### 4.2 Đăng xuất

```
POST /api/v1/auth/logout
```

**Auth required.** Xoá token khỏi Redis.

**Response 200:** `data: null`

---

### 4.3 Làm mới token

```
POST /api/v1/auth/refresh
```

**Request body:**
```json
{
  "refresh_token": "<refresh_jwt>"
}
```

**Response 200:** Cùng cấu trúc với login.

---

### 4.4 Kiểm tra phiên đăng nhập

```
GET /api/v1/auth/get-session
```

**Auth required.**

**Response 200:** Thông tin user hiện tại.

---

## 5. MODULE: NGƯỜI DÙNG (USER)

### 5.1 Tạo người dùng

```
POST /api/v1/users
```

**Role:** `ADMIN`

**Request body:**
```json
{
  "username": "john_doe",
  "full_name": "John Doe",
  "email": "john@example.com",
  "phone_number": "0901234567",
  "status": 0,
  "role": "USER"
}
```

> `role` nhận một trong: `ADMIN`, `AUTHOR`, `USER`. Mặc định là `USER`.

**Response 201:**
```json
{
  "code": 201,
  "message": "Success",
  "data": {
    "id": 10,
    "username": "john_doe",
    "full_name": "John Doe",
    "email": "john@example.com",
    "phone_number": "0901234567",
    "status": 0,
    "role": "USER"
  }
}
```

---

### 5.2 Cập nhật người dùng

```
PUT /api/v1/users/{id}
```

**Role:** `ADMIN`

**Request body:** Tương tự tạo mới (có thể thay đổi role).

**Response 200:** Thông tin user đã cập nhật.

---

### 5.3 Xóa người dùng

```
DELETE /api/v1/users/{id}
```

**Role:** `ADMIN`

**Response 200:** `data: null`

---

### 5.4 Chi tiết người dùng

```
GET /api/v1/users/{id}
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Response 200:**
```json
{
  "data": {
    "id": 1,
    "username": "admin",
    "full_name": "Administrator",
    "email": "admin@soar.news",
    "phone_number": "0900000000",
    "status": 0,
    "role": "ADMIN"
  }
}
```

---

### 5.5 Lịch sử người dùng

```
GET /api/v1/users/{id}/histories?page=0&size=10
```

**Role:** `ADMIN`

**Response 200:**
```json
{
  "data": {
    "content": [
      { "id": 1, "user_id": 1, "message": "Tạo mới người dùng", "created_at": 1745000000000 }
    ],
    "total_elements": 5
  }
}
```

---

### 5.6 Kiểm tra email tồn tại

```
GET /api/v1/users/exist-email?email=john@example.com
```

**Auth required.** `200` = không tồn tại, `409` = đã tồn tại.

---

### 5.7 Kiểm tra username tồn tại

```
GET /api/v1/users/check-username?username=john_doe
```

**Auth required.**

---

### 5.8 Kiểm tra số điện thoại tồn tại

```
GET /api/v1/users/exist-phone?phone_number=0901234567
```

**Auth required.**

---

### 5.9 Reset mật khẩu

```
PUT /api/v1/users/reset-password/{id}
```

**Role:** `ADMIN`

Hệ thống tạo mật khẩu ngẫu nhiên và lưu lại.

**Response 200:** `data: null`

---

### 5.10 Đổi mật khẩu

```
PUT /api/v1/users/{id}/password
```

**Role:** `ADMIN`, `AUTHOR`, `USER` (chỉ đổi mật khẩu của chính mình)

**Request body:**
```json
{
  "old_password": "News@2025",
  "new_password": "NewPass@2025",
  "confirm_password": "NewPass@2025"
}
```

**Response 200:** `data: null`

---

### 5.11 Tìm kiếm user (mention)

```
GET /api/v1/users/mention-search?keyword=john&page=0&size=10
```

**Auth required.**

**Response 200:**
```json
{
  "data": {
    "content": [
      { "id": 1, "username": "john_doe", "full_name": "John Doe", "avatar": "..." }
    ],
    "total_elements": 1
  }
}
```

---

### 5.12 Filter danh sách user

```
POST /api/v1/users/filter
```

**Role:** `ADMIN`

**Request body:**
```json
{
  "keyword": "john",
  "status": [0, 1],
  "page": 0,
  "size": 10
}
```

**Response 200:**
```json
{
  "data": {
    "content": [
      {
        "id": 1,
        "username": "john_doe",
        "full_name": "John Doe",
        "email": "john@example.com",
        "phone": "0901234567",
        "status": 0,
        "role": "USER",
        "created_by": "admin",
        "created_at": 1745000000000
      }
    ],
    "total_elements": 1
  }
}
```

---

## 6. MODULE: NGUỒN TIN (SOURCE)

### 6.1 Tạo nguồn tin

```
POST /api/v1/sources
```

**Role:** `ADMIN`

**Request body:**
```json
{
  "name": "VnExpress",
  "url": "https://vnexpress.net",
  "description": "Báo điện tử VnExpress"
}
```

---

### 6.2 Cập nhật nguồn tin

```
PUT /api/v1/sources/{id}
```

**Role:** `ADMIN`

---

### 6.3 Xóa nguồn tin

```
DELETE /api/v1/sources/{id}
```

**Role:** `ADMIN`

---

### 6.4 Chi tiết nguồn tin

```
GET /api/v1/sources/{id}
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 6.5 Filter nguồn tin

```
POST /api/v1/sources/filter
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body:**
```json
{
  "keyword": "vnexpress",
  "page": 0,
  "size": 10
}
```

---

### 6.6 Tất cả nguồn tin kèm chủ đề

```
GET /api/v1/sources/all-with-topics
```

**Auth required.**

---

### 6.7 Kiểm tra tên / URL tồn tại

```
GET /api/v1/sources/exist-name?name=VnExpress
GET /api/v1/sources/exist-url?url=https://vnexpress.net
```

**Auth required.**

---

## 7. MODULE: CHỦ ĐỀ (TOPIC)

### 7.1 Tạo chủ đề

```
POST /api/v1/topics
```

**Role:** `ADMIN`

**Request body:**
```json
{
  "name": "Công nghệ",
  "url": "https://vnexpress.net/cong-nghe",
  "source_id": 1,
  "description": "Tin tức công nghệ"
}
```

---

### 7.2 Cập nhật / Xóa chủ đề

```
PUT /api/v1/topics/{id}
DELETE /api/v1/topics/{id}
```

**Role:** `ADMIN`

---

### 7.3 Chi tiết / Filter chủ đề

```
GET /api/v1/topics/{id}
POST /api/v1/topics/filter
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 7.4 Follow / Unfollow chủ đề

```
POST /api/v1/topics/{topicId}/follow
DELETE /api/v1/topics/{topicId}/follow
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 7.5 Danh sách đang follow

```
GET /api/v1/topics/following?page=0&size=10
```

**Auth required.**

---

### 7.6 Kiểm tra tên / URL tồn tại

```
GET /api/v1/topics/exist-name?name=Công nghệ
GET /api/v1/topics/exist-url?url=https://...
```

**Auth required.**

---

## 8. MODULE: BÀI VIẾT (ARTICLE)

### 8.1 Tạo bài viết

```
POST /api/v1/articles
```

**Role:** `ADMIN`, `AUTHOR`

**Request body:**
```json
{
  "title": "Tiêu đề bài viết",
  "link": "https://vnexpress.net/bai-viet-1",
  "guid": "unique-guid-001",
  "description": "Mô tả ngắn",
  "pub_date": 1745000000000,
  "image_link": "https://cdn.example.com/img.jpg",
  "topic_id": 1
}
```

**Response 201:**
```json
{
  "data": {
    "id": 100,
    "title": "Tiêu đề bài viết",
    "link": "https://vnexpress.net/bai-viet-1",
    "topic_id": 1,
    "pub_date": 1745000000000
  }
}
```

---

### 8.2 Cập nhật bài viết

```
PUT /api/v1/articles/{id}
```

**Role:** `ADMIN`, `AUTHOR`

---

### 8.3 Xóa bài viết

```
DELETE /api/v1/articles/{id}
```

**Role:** `ADMIN`, `AUTHOR`

---

### 8.4 Chi tiết bài viết

```
GET /api/v1/articles/{id}
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 8.5 Filter bài viết

```
POST /api/v1/articles/filter
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body:**
```json
{
  "keyword": "công nghệ",
  "topic_ids": [1, 2],
  "source_ids": [1],
  "page": 0,
  "size": 10
}
```

---

### 8.6 Yêu thích

```
POST /api/v1/articles/{articleId}/favorites     # Thêm yêu thích
DELETE /api/v1/articles/{articleId}/favorites   # Bỏ yêu thích
GET /api/v1/articles/favorites?page=0&size=10   # Danh sách yêu thích
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 8.7 Lịch sử đọc

```
POST /api/v1/articles/{articleId}/view          # Ghi nhận đọc
GET /api/v1/articles/view-history?page=0&size=10 # Lịch sử đọc
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 8.8 Bình luận bài viết

```
POST /api/v1/articles/{articleId}/comments      # Tạo bình luận
DELETE /api/v1/articles/comments/{commentId}    # Xóa bình luận
GET /api/v1/articles/{articleId}/comments?page=0&size=10 # Danh sách bình luận
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body (tạo):**
```json
{
  "content": "Bình luận hay!",
  "parent_comment_id": null
}
```

---

### 8.9 Kiểm tra link tồn tại

```
GET /api/v1/articles/exist-link?link=https://...
```

**Role:** `ADMIN`, `AUTHOR`

---

## 9. MODULE: CỘNG ĐỒNG (BLOG POST)

### 9.1 Tạo bài đăng

```
POST /api/v1/posts
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body:**
```json
{
  "content": "Nội dung bài đăng",
  "images": ["https://cdn.example.com/img1.jpg"]
}
```

---

### 9.2 Cập nhật / Xóa bài đăng

```
PUT /api/v1/posts/{id}
DELETE /api/v1/posts/{id}
```

**Role:** `ADMIN`, `AUTHOR`, `USER` (service kiểm tra chủ sở hữu)

---

### 9.3 Chi tiết / Filter bài đăng

```
GET /api/v1/posts/{id}
POST /api/v1/posts/filter
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body filter:**
```json
{
  "keyword": "",
  "user_id": null,
  "page": 0,
  "size": 10
}
```

---

### 9.4 Like / Unlike

```
POST /api/v1/posts/{id}/like
DELETE /api/v1/posts/{id}/like
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 9.5 Chia sẻ

```
POST /api/v1/posts/{id}/share
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 9.6 Bình luận bài đăng

```
POST /api/v1/posts/{postId}/comments            # Tạo bình luận / reply
DELETE /api/v1/posts/{postId}/comments/{commentId}
GET /api/v1/posts/{postId}/comments?page=0&size=10
GET /api/v1/posts/{postId}/comments/{commentId}/replies?page=0&size=10
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 9.7 Hồ sơ người dùng

```
GET /api/v1/posts/profile/{userId}?page=0&size=10
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

## 10. MODULE: TIN NHẮN (CHAT)

### 10.1 REST APIs

#### Chat Groups

```
POST /api/v1/chat/groups                            # Tạo nhóm
GET  /api/v1/chat/groups                            # Danh sách nhóm của tôi
GET  /api/v1/chat/groups/{groupId}                  # Chi tiết nhóm
DELETE /api/v1/chat/groups/{groupId}                # Xóa nhóm
PUT  /api/v1/chat/groups/{groupId}/name             # Đổi tên nhóm
DELETE /api/v1/chat/groups/{groupId}/leave          # Rời nhóm
POST /api/v1/chat/groups/direct/{targetUserId}      # Nhắn tin trực tiếp (DM)
```

#### Thành viên nhóm

```
POST   /api/v1/chat/groups/{groupId}/members               # Thêm thành viên
DELETE /api/v1/chat/groups/{groupId}/members/{userId}      # Xóa thành viên
PUT    /api/v1/chat/groups/{groupId}/members/{userId}/admin # Cấp quyền admin
```

#### Tin nhắn

```
GET  /api/v1/chat/groups/{groupId}/messages                           # Danh sách tin nhắn
POST /api/v1/chat/groups/{groupId}/messages/read                      # Đánh dấu đã đọc
GET  /api/v1/chat/groups/{groupId}/messages/{messageId}/reads         # Ai đã đọc
DELETE /api/v1/chat/groups/{groupId}/messages/{messageId}/recall      # Thu hồi tin nhắn
```

#### Reactions

```
POST   /api/v1/chat/groups/{groupId}/messages/{messageId}/reactions          # Thêm reaction
DELETE /api/v1/chat/groups/{groupId}/messages/{messageId}/reactions/{emoji}  # Xóa reaction
```

#### Khác

```
GET  /api/v1/chat/groups/{groupId}/presence     # Thành viên online
GET  /api/v1/chat/groups/{groupId}/history      # Lịch sử nhóm
POST /api/v1/chat/groups/heartbeat              # Cập nhật trạng thái online
```

**Role:** `ADMIN`, `AUTHOR`, `USER` (tất cả có thể dùng chat)

---

### 10.2 WebSocket

**Endpoint kết nối:** `ws://localhost:8188/ws`

**Gửi tin nhắn:**

```
STOMP SEND destination: /app/chat/{groupId}
Authorization: Bearer <access_token>

{
  "content": "Xin chào!",
  "message_type": "TEXT"
}
```

**Nhận tin nhắn:**

```
STOMP SUBSCRIBE destination: /topic/chat/{groupId}
```

**Response:**
```json
{
  "id": 1,
  "group_id": 5,
  "sender_id": 2,
  "sender_username": "john_doe",
  "sender_full_name": "John Doe",
  "sender_avatar": "...",
  "content": "Xin chào!",
  "message_type": "TEXT",
  "created_at": 1745000000000
}
```

**message_type:** `TEXT`, `IMAGE`, `EMOJI`

**Subscriptions khác:**

| Topic | Mô tả |
|-------|-------|
| `/topic/chat/{groupId}/presence` | Trạng thái online thành viên |
| `/topic/chat/{groupId}/read` | Ai đã đọc tin nhắn |
| `/topic/chat/{groupId}/reaction` | Reactions thay đổi |
| `/topic/chat/{groupId}/recall` | Tin nhắn bị thu hồi |
| `/topic/notifications/{userId}` | Thông báo cá nhân |

---

## 11. MODULE: DASHBOARD

```
GET /api/v1/dashboard/articles/growth?year=2026
GET /api/v1/dashboard/articles/daily?year=2026&month=4
GET /api/v1/dashboard/articles/by-source?year=2026
```

**Role:** `ADMIN` only

**Response growth:**
```json
{
  "data": {
    "year": 2026,
    "months": [
      { "month": 1, "count": 150 },
      { "month": 2, "count": 200 }
    ]
  }
}
```

---

## 12. MODULE: GÓP Ý (FEEDBACK)

### 12.1 Tạo góp ý

```
POST /api/v1/feedbacks
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Request body:**
```json
{
  "title": "Lỗi hiển thị trang",
  "content": "Trang chủ bị lỗi trên mobile",
  "image_urls": ["https://cdn.example.com/screenshot.jpg"]
}
```

---

### 12.2 Danh sách góp ý của tôi

```
GET /api/v1/feedbacks?page=0&size=10
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

---

### 12.3 Chi tiết / Xóa góp ý

```
GET    /api/v1/feedbacks/{feedbackId}
DELETE /api/v1/feedbacks/{feedbackId}
```

**Role:** `ADMIN`, `AUTHOR`, `USER` (chỉ chủ sở hữu)

---

### 12.4 [Admin] Tất cả góp ý

```
GET /api/v1/admin/feedbacks?status=0&page=0&size=10
```

**Role:** `ADMIN`

`status`: `0=PENDING`, `1=IN_REVIEW`, `2=RESOLVED`, `3=REJECTED`

---

### 12.5 [Admin] Cập nhật trạng thái góp ý

```
PUT /api/v1/admin/feedbacks/{feedbackId}/status
```

**Role:** `ADMIN`

**Request body:**
```json
{
  "status": 2
}
```

---

## 13. MODULE: UPLOAD

```
POST /api/v1/upload/images
```

**Role:** `ADMIN`, `AUTHOR`, `USER`

**Content-Type:** `multipart/form-data`

| Param | Giá trị |
|-------|---------|
| `files` | File ảnh (JPEG/PNG/GIF/WebP) |
| Số lượng tối đa | 10 ảnh |
| Kích thước tối đa | 10MB/ảnh |

**Response 200:**
```json
{
  "data": [
    {
      "url": "https://storage.example.com/feedbacks/abc123.jpg",
      "original_name": "screenshot.jpg",
      "size": 204800,
      "content_type": "image/jpeg"
    }
  ]
}
```

---

## 14. BẢNG MÃ LỖI

| HTTP Code | Mô tả |
|-----------|-------|
| `200` | Thành công |
| `201` | Tạo mới thành công |
| `400` | Dữ liệu đầu vào không hợp lệ |
| `401` | Chưa xác thực hoặc token hết hạn |
| `403` | Không có quyền (role không phù hợp) |
| `404` | Không tìm thấy tài nguyên |
| `409` | Xung đột dữ liệu (email/username đã tồn tại) |
| `500` | Lỗi server |

**Response lỗi mẫu:**
```json
{
  "code": 403,
  "message": "Access Denied",
  "data": null
}
```

---

## 15. DATA MODELS

### UserRole (Enum)

| Giá trị | Mô tả |
|---------|-------|
| `ADMIN` | Quản trị viên – toàn quyền |
| `AUTHOR` | Tác giả – có quyền đăng bài viết |
| `USER` | Người dùng thông thường |

### User

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | Long | ID user |
| `username` | String | Tên đăng nhập (unique) |
| `full_name` | String | Họ tên |
| `email` | String | Email (unique) |
| `phone_number` | String | Số điện thoại (unique) |
| `avatar` | String | URL ảnh đại diện |
| `status` | int | `0=ACTIVE`, `1=INACTIVE` |
| `role` | UserRole | Vai trò: `ADMIN`, `AUTHOR`, `USER` |

### ActiveStatus

| Giá trị | Mô tả |
|---------|-------|
| `0` | ACTIVE – đang hoạt động |
| `1` | INACTIVE – bị tắt |
| `-1` | UNKNOWN |

### Article

| Field | Type | Mô tả |
|-------|------|-------|
| `id` | Long | ID bài viết |
| `title` | String | Tiêu đề |
| `link` | String | Link bài viết (unique) |
| `guid` | String | GUID (unique) |
| `description` | String | Mô tả |
| `pub_date` | Long | Ngày đăng (Unix ms) |
| `image_link` | String | URL ảnh đại diện |
| `topic_id` | Long | Chủ đề |

### MessageType (Chat)

| Giá trị | Mô tả |
|---------|-------|
| `TEXT` | Tin nhắn văn bản |
| `IMAGE` | Tin nhắn hình ảnh |
| `EMOJI` | Tin nhắn emoji |

### FeedbackStatus

| Giá trị | Mô tả |
|---------|-------|
| `0` | PENDING – Chờ xử lý |
| `1` | IN_REVIEW – Đang xem xét |
| `2` | RESOLVED – Đã giải quyết |
| `3` | REJECTED – Từ chối |
