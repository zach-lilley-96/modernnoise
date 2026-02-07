//package com.lilley.modernnoise.Seeder;
//
//import com.lilley.modernnoise.Data.Entities.User;
//import com.lilley.modernnoise.Repos.UserRepo;
//import com.lilley.modernnoise.Services.FriendService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class InitialUsersSeeder implements CommandLineRunner {
//    private final UserRepo userRepo;
//    private final FriendService friendService;
//    @Override
//    public void run(String... args) throws Exception {
//        var user1 = User.builder()
//                .email("fakeemail@gmail.com")
//                .displayName("John Doe")
//                .friendCode(UUID.randomUUID())
//                .provider("GOOGLE")
//                .build();
//        userRepo.save(user1);
//
//        var user2 = User.builder()
//                .email("fakeemail2@gmail.com")
//                .displayName("Jane Smith")
//                .friendCode(UUID.randomUUID())
//                .provider("GOOGLE")
//                .build();
//        userRepo.save(user2);
//
//        var myself = userRepo.findByEmail("zlilley96@gmail.com").orElseThrow(() -> new RuntimeException("Myself not found"));
//
//        friendService.AddFriend(user2, myself.getFriendCode());
//        friendService.AddFriend(user1, myself.getFriendCode());
//        friendService.AddFriend(user1, user2.getFriendCode());
//
//
//    }
//}
