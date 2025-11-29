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
        public static final String DEFAULT_PASSWORD = "news@2025";
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
        public static String[] MATCHER_USER_API = {"/api/v1/auth/**","/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/"};
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

}
