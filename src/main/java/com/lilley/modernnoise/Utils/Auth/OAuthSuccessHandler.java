package com.lilley.modernnoise.Utils.Auth;

import com.lilley.modernnoise.Services.Auth.JwtService;
import com.lilley.modernnoise.Services.Auth.OAuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final OAuthUserService userService;
    private final JwtService jwtService;

    @Value("${app.redirect-url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(@NonNull HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuthUser = (OAuth2User) authentication.getPrincipal();
        assert oAuthUser != null;
        var user = userService.processOAuthUser(oAuthUser);
        var token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true) // true in prod (HTTPS)
                .path("/")
                .maxAge(Duration.ofHours(6))
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(redirectUrl);
    }
}
