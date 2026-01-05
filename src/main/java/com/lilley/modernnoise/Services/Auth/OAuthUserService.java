package com.lilley.modernnoise.Services.Auth;

import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserService {
    private final UserRepo userRepo;

    public User processOAuthUser(OAuth2User oAuthUser){
        String email = oAuthUser.getAttribute("email");
        String displayName = oAuthUser.getAttribute("name");

        return userRepo.findByEmail(email).orElseGet(() -> {
            User newUser = User.builder()
                    .email(email)
                    .displayName(displayName)
                    .provider("GOOGLE")
                    .build();
            return userRepo.save(newUser);
        });
    }
}
