package org.oplearn.project.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.dto.request.UserFilterRequest;
import org.oplearn.project.dto.request.UserRequest;
import org.oplearn.project.dto.response.PageResponse;
import org.oplearn.project.dto.response.user.UserFilterResponse;
import org.oplearn.project.dto.response.user.UserMentionResponse;
import org.oplearn.project.dto.response.user.UserResponse;
import org.oplearn.project.entity.user.User;
import org.oplearn.project.exception.base.ForbiddenException;
import org.oplearn.project.exception.base.UserNotFoundException;
import org.oplearn.project.exception.base.user.EmailAlreadyExistedException;
import org.oplearn.project.exception.base.user.PhoneNumberAlreadyExistedException;
import org.oplearn.project.exception.base.user.UsernameAlreadyExistedException;
import org.oplearn.project.repository.UserRepository;
import org.oplearn.project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.oplearn.project.constanst.OpLearnConstants.AuditorConstant.ADMIN;
import static org.oplearn.project.utils.PasswordEncoderUtils.getPasswordEncoder;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getById(Long userId) {
        return repository.getByIdAndDeletedFalse(userId);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsernameAndDeletedFalse(username).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    @Override
    public UserResponse create(UserRequest request) {
        log.info("create");
        log.debug("Request: {}", request);

        this.checkValidateCreate(request);

        User user = this.mapRequestToEntity(request);

        user = repository.save(user);

        return this.mapEntityToResponse(user);
    }

    private void checkValidateCreate(UserRequest request) {
        log.info("checkValidateCreate");
        log.debug("request: {}", request);

        this.checkEmailExists(request.getEmail());
        this.checkPhoneNumberExists(request.getPhoneNumber());
        this.checkUsernameExists(request.getUsername());

    }

    private User mapRequestToEntity(UserRequest userRequest) {
        return new User(
                userRequest.getUsername(),
                userRequest.getFullName(),
                userRequest.getEmail(),
                userRequest.getPhoneNumber()
        );
    }

    private UserResponse mapEntityToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getStatus()
        );
    }

    @Transactional
    @Override
    public UserResponse update(UserRequest request, Long id) {
        log.info("update");
        log.debug("request: {}, id: {}", request, id);

        User existingUser = this.checkExistById(id);
        this.checkValidForUpdate(existingUser, request);
        this.setValueForUpdate(existingUser, request);

        existingUser = repository.save(existingUser);
        return this.mapEntityToResponse(existingUser);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Deleting");
        log.debug("id: {}", id);

        User user = this.checkExistById(id);
        if (user.getUsername().equals(ADMIN)) {
            throw new ForbiddenException();
        }
        user.setDeleted(true);
        repository.save(user);
    }


    @Override
    public void checkUsernameExists(String username) {
        log.info("=== Start checkUsernameExists");
        log.debug("(checkUsernameExists) username: {}", username);

        if (isUsernameExisted(username)) {
            log.error("Username {} already exists", username);
            throw new UsernameAlreadyExistedException();
        }
    }

    @Override
    public void checkPhoneNumberExists(String phoneNumber) {
        log.info("=== Start checkPhoneNumberExists");
        log.debug("(checkPhoneNumberExists) phoneNumber: {}", phoneNumber);


        if (isPhoneExisted(phoneNumber)) {
            log.error("Phone number {} already exists", phoneNumber);
            throw new PhoneNumberAlreadyExistedException();
        }
    }

    @Override
    public void checkEmailExists(String email) {
        log.info("=== Start CheckEmailExists");
        log.debug("(checkEmailExists) email:{}", email);
        
        if (isEmailExisted(email)) {
            log.error("Email {} already exists", email);
            throw new EmailAlreadyExistedException();
        }
    }

    @Transactional
    @Override
    public void resetPassword(Long id, String randomPassword) {
        log.info("=== Start resetPassword");
        log.debug("(changePassword) id: {}", id);

        User user = this.checkExistById(id);
        user.setPassword(getPasswordEncoder().encode(randomPassword));
        repository.save(user);
    }


    @Override
    public void save(User users) {
        repository.save(users);
    }

    @Override
    public PageResponse<UserFilterResponse> filter(UserFilterRequest request) {
        log.info("Filtering");
        log.debug("Request: {}", request);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        String keyword = request.getKeyword() == null ? "" : request.getKeyword();
        Page<UserFilterResponse> users = repository.filter(
                keyword,
                request.getStatus(),
                pageable
        );

        return PageResponse.of(users.getContent(), (int) users.getTotalElements());

    }

    @Override
    public UserResponse detail(Long id) {
        log.info("=== Start detail");
        log.debug("id: {}", id);
        User user = repository.getByIdAndDeletedFalse(id);
        if (Objects.isNull(user) || user.getDeleted() == true) {
            log.error("User not found exception");
            throw new UserNotFoundException();
        }
        return mapEntityToResponse(user);
    }


    @Override
    public User checkExistById(Long id) {
        log.info("(checkExistById");
        log.debug("checkExistById: {}", id);

        User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
        if (user == null || user.getDeleted()) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private void checkValidForUpdate(User user, UserRequest request) {
        if (Objects.nonNull(user.getEmail()) && !user.getEmail().equals(request.getEmail()) &&
                isEmailExisted(request.getEmail())) {
            throw new EmailAlreadyExistedException();
        }

        if (Objects.nonNull(user.getPhoneNumber()) && !user.getPhoneNumber().equals(request.getPhoneNumber()) &&
                isPhoneExisted(request.getPhoneNumber())) {
            throw new PhoneNumberAlreadyExistedException();
        }

    }

    private boolean objectNotNullAndNotEmpty(String object) {
        return object != null && !object.isEmpty();
    }

    private boolean isUsernameExisted(String username) {
        return objectNotNullAndNotEmpty(username)
                && repository.existsByUsernameIgnoreCaseAndDeletedIsFalse(username);
    }

    private boolean isEmailExisted(String email) {
        return objectNotNullAndNotEmpty(email)
                && repository.existsByEmailAndDeletedIsFalse(email);
    }

    private boolean isPhoneExisted(String phone) {
        return objectNotNullAndNotEmpty(phone)
                && repository.existsByPhoneNumberAndDeletedIsFalse(phone);
    }

    @Override
    public PageResponse<UserMentionResponse> searchForMention(String keyword, int page, int size) {
        log.info("=== Start searchForMention");
        log.debug("(searchForMention) keyword: {}, page: {}, size: {}", keyword, page, size);

        Pageable pageable = PageRequest.of(page, size);
        String kw = keyword == null ? "" : keyword;
        Page<UserMentionResponse> result = repository.searchForMention(kw, pageable);

        return PageResponse.of(result.getContent(), (int) result.getTotalElements());
    }

    private void setValueForUpdate(User user, UserRequest request) {
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        user.setStatus(request.getStatus());
    }

}