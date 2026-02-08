package com.lilley.modernnoise.Repos;

import com.lilley.modernnoise.Data.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByFriendCode(UUID friendCode);
    boolean existsByEmail(String email);

}
