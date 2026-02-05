package com.lilley.modernnoise.Controllers;

import java.util.UUID;

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
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friendCode")
    public ResponseEntity<UUID> GetFriendCode(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GetFriendCode(user));
    }

    @GetMapping("/generate")
    public ResponseEntity<UUID> GenerateFriendCode(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GenerateFriendCode(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> GetFriends(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendService.GetFriends(user));
    }

    @PostMapping("/search")
    public ResponseEntity<?> SearchForUserByFriendCode(@RequestBody String friendCode) {
        return ResponseEntity.ok(friendService.GetUsernameByFriendCode(UUID.fromString(friendCode)));
    }

    @PostMapping
    public ResponseEntity<?> AddFriend(@AuthenticationPrincipal User user, @RequestBody String friendCode) {
        var friendCodeUUID = UUID.fromString(friendCode);
        friendService.AddFriend(user, friendCodeUUID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> RemoveFriend(@AuthenticationPrincipal User user, @RequestBody String friendCode) {
        var friendCodeUUID = UUID.fromString(friendCode);
        friendService.RemoveFriend(user, friendCodeUUID);
        return ResponseEntity.ok().build();
    }
}
