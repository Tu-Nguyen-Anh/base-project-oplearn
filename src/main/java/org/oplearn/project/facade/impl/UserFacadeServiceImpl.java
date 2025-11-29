package org.oplearn.project.facade.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.ChangePasswordRequest;
import org.oplearn.project.dto.request.UserFilterRequest;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.dto.response.user.UserHistoryResponse;
import org.oplearn.project.dto.response.user.UserResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.ForbiddenException;
import org.oplearn.project.exception.base.user.NewPasswordIncorrectException;
import org.oplearn.project.exception.base.user.NowPasswordIncorrectException;
import org.oplearn.project.exception.base.user.PasswordWasUsedException;
import org.oplearn.project.facade.UserFacadeService;
import org.oplearn.project.security.UserAuthenticated;
import org.oplearn.project.service.UserHistoryService;
import org.oplearn.project.service.UserService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.Message.*;
import static org.oplearn.project.utils.PasswordEncoderUtils.equalPassword;
import static org.oplearn.project.utils.PasswordEncoderUtils.getPasswordEncoder;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeServiceImpl implements UserFacadeService {
    private final UserService userService;
    private final UserHistoryService userHistoryService;

    @Transactional
    @Override
    public UserResponse create(UserRequest request) {
        log.info("=== Start create");
        log.debug("(create) request: {}", request);

        UserResponse response = userService.create(request);
        userHistoryService.saveHistory(response.getId(), CREATE_USER);
        return response;
    }

    @Transactional
    @Override
    public UserResponse update(UserRequest request, Long id) {
        log.info("=== Start update");
        log.debug("(update) request: {}", request);

        UserResponse response = userService.update(request, id);
        userHistoryService.saveHistory(response.getId(), UPDATE_USER);
        return response;
    }

    @Transactional
    @Override
    public void resetPassword(Long id) {
        log.info("=== Start reset password");
        log.debug("(resetPassword) id: {}", id);

        userService.resetPassword(id, this.generateRandomPassword());
        userHistoryService.saveHistory(id, RESET_PASSWORD_HISTORY);

    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("=== Start delete");
        log.debug("(delete) id: {}", id);
        userService.delete(id);
        userHistoryService.saveHistory(id, DELETE_USER);
    }

    @Override
    public PageResponse<UserHistoryResponse> getHistories(Long userId, int page, int size) {
        log.info("===> Start getHistories");

        return userHistoryService.filter(userId, page, size);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {
        log.info("(changePassword) id: {}", id);


        User currentUser = UserAuthenticated.getCurrentUserThrowUnAuthorized();
        if (!Objects.equals(currentUser.getId(), id)) {
            throw new ForbiddenException();
        }
        User user = userService.getById(id);

        if (!equalPassword(request.oldPassword(), user.getPassword())) {
            throw new NowPasswordIncorrectException();
        }

        if (equalPassword(request.newPassword(), user.getPassword())) {
            throw new PasswordWasUsedException();
        }

        if (!Objects.equals(request.newPassword(), request.confirmPassword())) {
            throw new NewPasswordIncorrectException();
        }
        user.setPassword(getPasswordEncoder().encode(request.newPassword()));

        userService.save(user);

    }

    @Override
    public PageResponse<UserFilterResponse> filter(UserFilterRequest request) {
        log.info("=== Start filter");
        log.debug("(filter) request: {}", request);
        
        return userService.filter(request);
    }

    private String generateRandomPassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String special = "*@#$%^&+=";
        String allChars = lowercase + uppercase + digits + special;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));

        for (int i = 0; i < 4; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }

}
