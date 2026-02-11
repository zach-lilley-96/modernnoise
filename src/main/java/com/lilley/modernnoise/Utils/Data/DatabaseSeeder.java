package com.lilley.modernnoise.Utils.Data;

import com.lilley.modernnoise.Data.Entities.User;
import com.lilley.modernnoise.Repos.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepo userRepo;

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() == 0) {
            log.info("Seeding 20 fake users for development mode...");
            for (int i = 1; i <= 20; i++) {
                User user = User.builder()
                        .email("user" + i + "@example.com")
                        .displayName("Fake User " + i)
                        .provider("fake")
                        .build();
                userRepo.save(user);
            }
            log.info("Finished seeding users.");
        } else {
            log.info("Database already contains users, skipping seeding.");
        }
    }
}
