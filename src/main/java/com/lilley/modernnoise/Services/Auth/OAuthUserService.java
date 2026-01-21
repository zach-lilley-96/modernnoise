package com.lilley.modernnoise.Services.Auth;

import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthUserService {
    private final UserRepo userRepo;

    public User processOAuthUser(OAuth2User oAuthUser){
        String email = oAuthUser.getAttribute("email");
        String displayName = oAuthUser.getAttribute("name");
        log.info("Processing OAuth user: {}", email);

        return userRepo.findByEmail(email).orElseGet(() -> {
            log.info("User {} not found, creating new user profile.", email);
            User newUser = User.builder()
                    .email(email)
                    .displayName(displayName)
                    .provider("GOOGLE")
                    .build();
            return userRepo.save(newUser);
        });
    }
}
