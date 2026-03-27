package org.oplearn.project.constanst;

public class OpLearnConstants {


    public static class CommonConstants {
        public static final String NEWS_PREFIX = "NEWS";
        public static final String TOKEN_TYPE = "Bearer";
        public static final String BEARER_TOKEN_TYPE_START = "Bearer ";
        public static final String ENCODING_UTF_8 = "UTF-8";
        public static final String LANGUAGE = "Accept-Language";
        public static final String DEFAULT_LANGUAGE = "en";
        public static final String BLANK = "";
        public static final String PARAM_KEYWORD = "keyword";
        public static final String PARAM_SIZE = "size";
        public static final String PARAM_PAGE = "page";
        public static final String PARAM_ALL = "all";
        public static final String PERCENT = "%";
        public static final String MESSAGE_SOURCE = "classpath:i18n/messages";
        public static final String BASE_PACKAGE_REPO = "org.oplearn.project";
        public static final String NOT_FOUND_MESSAGE = "Not found";
        public static final String BAD_REQUEST_MESSAGE = "Bad request";
        public static final String CONFLICT_MESSAGE = "Conflict occurred";
        public static final String BLANK_MESSAGE = "";
        public static final String DEFAULT_PASSWORD = "News@2025";
    }


    public static class AuditorConstant {
        private AuditorConstant() {
        }

        public static final String ADMIN = "ADMIN";
        public static final String ANONYMOUS = "anonymousUser";
        public static final String SYSTEM = "SYSTEM";
    }

    public static class StatusException {
        private StatusException() {
        }

        public static final Integer NOT_FOUND = 404;
        public static final Integer CONFLICT = 409;
        public static final Integer BAD_REQUEST = 400;
    }

    public static class MessageException {
        private MessageException() {
        }

        public static final String DEFAULT_CODE_BAD_REQUEST = "org.oplearn.project.exception.base.BadRequestException";
        public static final String DEFAULT_CODE_CONFLICT = "org.oplearn.project.exception.base.ConflictException";
        public static final String DEFAULT_CODE_NOTFOUND = "org.oplearn.project.exception.base.NotFoundException";
        public static final String DEFAULT_CODE_SERVER_ERROR = "org.oplearn.project.exception.base.InternalServerError";

    }

    public static class AuthConstant {
        private AuthConstant() {
        }

        public static String TYPE_TOKEN = "Bear ";
        public static String AUTHORIZATION = "Authorization";
        public static String[] MATCHER_USER_API = {"/api/v1/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/", "/ws/**"};
        public static String[] MATCHER_ADMIN_API = {"/api/v1/admin/**"};
    }

    public static class VariableConstant {
        private VariableConstant() {
        }

        public static final String SIZE_DEFAULT = "10";
        public static final String PAGE_DEFAULT = "0";
        public static final String IS_ALL_DEFAULT = "0";

    }

    public static final class ActiveStatus {
        public static final int UNKNOWN = -1;
        public static final int INACTIVE = 1;
        public static final int ACTIVE = 0;
    }

    public static final class NotificationType {
        private NotificationType() {
        }

        public static final String MENTION_IN_COMMENT = "MENTION_IN_COMMENT";
    }

    public static final class Message {

        private Message() {
        }

        public static final String SUCCESS = "org.openlearhub.success";
        public static final String INVALID_USERNAME = "Invalid Username";
        public static final String INVALID_FULL_NAME = "Invalid full name";
        public static final String INVALID_EMAIL = "Invalid Email";
        public static final String INVALID_PASSWORD = "Invalid Password";
        public static final String INVALID_PHONE_NUMBER = "Invalid PhoneNumber";
        public static final String INACTIVE_ACCOUNT = "Account is Inactived";
        public static final String CREATE_USER = "Tạo mới người dùng";
        public static final String UPDATE_USER = "Đã chỉnh sửa người dùng";
        public static final String RESET_PASSWORD_HISTORY = "Đã reset lại mật khẩu";
        public static final String DELETE_USER = "Đã xóa người dùng khỏi hệ thống";


    }

    public static final class ChatConstants {
        private ChatConstants() {
        }

        public static final String ROLE_ADMIN = "ADMIN";
        public static final String ROLE_MEMBER = "MEMBER";
        public static final String MESSAGE_TYPE_TEXT = "TEXT";
        public static final String MESSAGE_TYPE_IMAGE = "IMAGE";
        public static final String MESSAGE_TYPE_EMOJI = "EMOJI";
        public static final String WEBSOCKET_ENDPOINT = "/ws";
        public static final String TOPIC_PREFIX = "/topic";
        public static final String APP_PREFIX = "/app";
        public static final String CHAT_TOPIC = "/topic/chat/";
    }

    public static final class NotificationConstants {
        private NotificationConstants() {
        }

        public static final String NOTIFICATION_TOPIC = "/topic/notifications/%d";
    }

    public static final class ChatHistoryAction {
        private ChatHistoryAction() {
        }

        public static final String CREATE_GROUP = "CREATE_GROUP";
        public static final String RENAME_GROUP = "RENAME_GROUP";
        public static final String ADD_MEMBER = "ADD_MEMBER";
        public static final String REMOVE_MEMBER = "REMOVE_MEMBER";
        public static final String LEAVE_GROUP = "LEAVE_GROUP";
    }

    public static final class ChatException {
        private ChatException() {
        }

        public static final String CHAT_GROUP_NOT_FOUND = "org.oplearn.project.exception.base.chat.ChatGroupNotFoundException";
        public static final String NOT_GROUP_MEMBER = "org.oplearn.project.exception.base.chat.NotGroupMemberException";
        public static final String NOT_GROUP_ADMIN = "org.oplearn.project.exception.base.chat.NotGroupAdminException";
        public static final String MEMBER_ALREADY_EXISTS = "org.oplearn.project.exception.base.chat.MemberAlreadyExistsException";
        public static final String CHAT_MESSAGE_NOT_FOUND = "org.oplearn.project.exception.base.chat.ChatMessageNotFoundException";
        public static final String REACTION_ALREADY_EXISTS = "org.oplearn.project.exception.base.chat.ReactionAlreadyExistsException";
        public static final String REACTION_NOT_FOUND = "org.oplearn.project.exception.base.chat.ReactionNotFoundException";
        public static final String CANNOT_LEAVE_GROUP = "org.oplearn.project.exception.base.chat.CannotLeaveGroupException";
        public static final String NOT_MESSAGE_SENDER = "org.oplearn.project.exception.base.chat.NotMessageSenderException";
    }

    public static final class ChatPresence {
        private ChatPresence() {
        }

        public static final String ONLINE_USERS_KEY = "CHAT_ONLINE_USERS";
        public static final String USER_ONLINE_KEY_PREFIX = "presence:online:";
        public static final long ONLINE_TTL_MINUTES = 5L;
        public static final String PRESENCE_TOPIC = "/topic/chat/%s/presence";
        public static final String READ_RECEIPT_TOPIC = "/topic/chat/%s/read";
        public static final String REACTION_TOPIC = "/topic/chat/%s/reaction";
        public static final String RECALL_TOPIC = "/topic/chat/%s/recall";
        public static final String REACTION_ADD = "ADD";
        public static final String REACTION_REMOVE = "REMOVE";
    }

}
