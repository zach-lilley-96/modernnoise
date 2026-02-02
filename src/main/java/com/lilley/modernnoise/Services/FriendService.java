package com.lilley.modernnoise.Services;

import com.lilley.modernnoise.Data.Dtos.FriendDto;
import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepo userRepo;

    @Transactional
    public void AddFriend(User user, UUID friendCode){
        var friendOpt = userRepo.findByFriendCode(friendCode);

        if(friendOpt.isEmpty()){
            throw new IllegalArgumentException("User or Friend not found");
        }
        var friendSet = user.getFriendSet();
        if (friendSet.contains(friendOpt.get())){
            return;
        }
        user.getFriendSet().add(friendOpt.get());
        userRepo.save(user);
    }

    public String GetUsernameByFriendCode(UUID friendCode){
        var friendOpt = userRepo.findByFriendCode(friendCode);
        if(friendOpt.isEmpty()){
            throw new IllegalArgumentException("Friend not found");
        }
        return friendOpt.get().getDisplayName();
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
    public UUID GenerateFriendCode(User user){
        if(user.getFriendCode() != null){
            return user.getFriendCode();
        }
        var newFriendCode =  UUID.randomUUID();

        user.setFriendCode(newFriendCode);
        userRepo.save(user);

        return newFriendCode;
    }


    public List<FriendDto> GetFriends(User user){
        var friendSet = user.getFriendSet();
        return friendSet.stream()
                .map(friend -> new FriendDto(friend.getFriendCode(), friend.getDisplayName()))
                .toList();
    }

    public UUID GetFriendCode(User user){
        return user.getFriendCode();
    }
}
