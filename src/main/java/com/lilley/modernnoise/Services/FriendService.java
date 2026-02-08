package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.FriendDto;
import com.lilley.modernnoise.Data.Dtos.Response.FriendCodeResponse;
import com.lilley.modernnoise.Data.Dtos.Response.SuccessfulFriendCodeResponse;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FriendService {
    private final UserRepo userRepo;

    @Transactional
    public void AddFriend(UUID userId, UUID friendCode){
        var friendOpt = userRepo.findByFriendCode(friendCode).orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        var user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        var userFriendCode = user.getFriendCode();
        if (userFriendCode == null){
            user.setFriendCode(UUID.randomUUID());
        }

        if (userFriendCode == friendCode){
            return;
        }

        var friendSet = user.getFriendSet();
        if (friendSet.contains(friendOpt)){
            return;
        }
        user.getFriendSet().add(friendOpt);
        userRepo.save(user);
    }

    public SuccessfulFriendCodeResponse GetUsernameByFriendCode(UUID friendCode){
        var friendOpt = userRepo.findByFriendCode(friendCode);
        if(friendOpt.isEmpty()){
            throw new IllegalArgumentException("Friend not found");
        }
        return new SuccessfulFriendCodeResponse(friendOpt.get().getDisplayName());
    }

    @Transactional
    public void RemoveFriend(User user, UUID friendCode){
        var friendOpt = userRepo.findByFriendCode(friendCode);

        if(friendOpt.isEmpty()){
            throw new IllegalArgumentException("User or Friend not found");
        }

        var friendSet = user.getFriendSet();
        if (!friendSet.contains(friendOpt.get())){
            return;
        }

        user.getFriendSet().remove(friendOpt.get());
        userRepo.save(user);
    }

    @Transactional
    public FriendCodeResponse GenerateFriendCode(User user){
        if(user.getFriendCode() != null){
            return new FriendCodeResponse(user.getFriendCode());
        }
        var newFriendCode =  UUID.randomUUID();

        user.setFriendCode(newFriendCode);
        userRepo.save(user);

        return new FriendCodeResponse(newFriendCode);
    }

    @Transactional
    public List<FriendDto> GetFriends(UUID userId){
        var user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        var friendSet = user.getFriendSet();
        log.info("User {} has {} friends.", user.getDisplayName(), friendSet.size());
        return friendSet.stream()
                .map(friend -> new FriendDto(friend.getFriendCode(), friend.getDisplayName()))
                .toList();
    }

    public UUID GetFriendCode(User user){
        return user.getFriendCode();
    }
    public User GetUserByFriendCode(UUID friendCode){
        return userRepo.findByFriendCode(friendCode).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
