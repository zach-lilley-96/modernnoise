package com.lilley.modernnoise.Controllers.Auth;

import com.lilley.modernnoise.Data.Entities.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("auth")
public class AuthController {

    @GetMapping("/me")
    public User me(@AuthenticationPrincipal User user){
        log.info("Checking current user: {}", user != null ? user.getEmail() : "anonymous");
        return user;
    }

    @GetMapping("/status")
    public ResponseEntity<Void> checkStatus(Principal principal) {
        if (principal == null) {
            log.info("Auth status check: UNAUTHORIZED");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Auth status check: OK for user {}", principal.getName());
        return ResponseEntity.ok().build();
    }
    @PostMapping("/logout")
    public void logout(HttpServletResponse response){
        log.info("Logging out user");
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // or match your environment like in the success handler
                .path("/")
                .maxAge(0)
                .sameSite("Lax") // or "None" in prod; must match what was used when set
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
