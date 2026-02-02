package com.lilley.modernnoise.Controllers;

import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import com.lilley.modernnoise.Services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final UserRepo userRepo;

    @GetMapping("/friendCode")
    public ResponseEntity<UUID> GetFriendCode(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(friendService.GetFriendCode(user));
    }

    @GetMapping("/generate")
    public ResponseEntity<UUID> GenerateFriendCode(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(friendService.GenerateFriendCode(user));
    }

    @GetMapping("/me")
    public ResponseEntity<?> GetFriends(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(friendService.GetFriends(user));
    }


    @PostMapping("/search")
    public ResponseEntity<?> SearchForUserByFriendCode(@RequestBody String friendCode){
        return ResponseEntity.ok(friendService.GetUsernameByFriendCode(UUID.fromString(friendCode)));
    }


    @PostMapping
    public ResponseEntity<?> AddFriend(@AuthenticationPrincipal User user, @RequestBody String friendCode){
        var friendCodeUUID = UUID.fromString(friendCode);
        friendService.AddFriend(user, friendCodeUUID);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> RemoveFriend(@AuthenticationPrincipal User user, @RequestBody String friendCode){
        var friendCodeUUID = UUID.fromString(friendCode);
        friendService.RemoveFriend(user, friendCodeUUID);
        return ResponseEntity.ok().build();
    }
}
