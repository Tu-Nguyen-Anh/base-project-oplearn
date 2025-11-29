package org.oplearn.project.configuration.auditor;


import lombok.extern.slf4j.Slf4j;
import org.oplearn.project.entity.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

import static org.oplearn.project.constanst.OpLearnConstants.AuditorConstant.ANONYMOUS;
import static org.oplearn.project.constanst.OpLearnConstants.AuditorConstant.SYSTEM;


@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: " + authentication);

        if (Objects.nonNull(authentication) && !this.isAnonymous() && (Objects.nonNull(authentication.getPrincipal()))) {
            User user = (User) authentication.getPrincipal();
            return Optional.of(user.getUsername());
        }
        return Optional.of(SYSTEM);
    }


    private boolean isAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(ANONYMOUS);
    }
}
