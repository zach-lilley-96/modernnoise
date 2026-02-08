package com.lilley.modernnoise.Controllers;

import java.util.List;
import java.util.UUID;

import com.lilley.modernnoise.Data.Dtos.FriendDto;
import com.lilley.modernnoise.Data.Dtos.Requests.FriendSearchDto;
import com.lilley.modernnoise.Data.Dtos.Response.FriendCodeResponse;
import com.lilley.modernnoise.Data.Dtos.Response.SuccessfulFriendCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Services.FriendService;

import lombok.RequiredArgsConstructor;

@RestController
@Slf4j
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friendCode")
    public ResponseEntity<UUID> GetFriendCode(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GetFriendCode(user));
    }

    @GetMapping("/generate")
    public ResponseEntity<FriendCodeResponse> GenerateFriendCode(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GenerateFriendCode(user));
    }

    @GetMapping("/me")
    public ResponseEntity<List<FriendDto>> GetFriends(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GetFriends(user.getId()));
    }

    @PostMapping("/search")
    public ResponseEntity<SuccessfulFriendCodeResponse> SearchForUserByFriendCode(@RequestBody FriendSearchDto friendRequest) {
        log.info("Searching for user by friend code: {}", friendRequest.friendCode());
        return ResponseEntity.ok(friendService.GetUsernameByFriendCode(UUID.fromString(friendRequest.friendCode())));
    }

    @PostMapping
    public ResponseEntity<?> AddFriend(@AuthenticationPrincipal User user, @RequestBody FriendSearchDto friendRequest) {
        log.info("Adding friend {} to user {}", friendRequest.friendCode(), user.getEmail());
        var friendCodeUUID = UUID.fromString(friendRequest.friendCode());
        friendService.AddFriend(user.getId(), friendCodeUUID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> RemoveFriend(@AuthenticationPrincipal User user, @RequestBody FriendSearchDto friendRequest) {
        var friendCodeUUID = UUID.fromString(friendRequest.friendCode());
        friendService.RemoveFriend(user, friendCodeUUID);
        return ResponseEntity.ok().build();
    }
}
