package com.Android.Android_App.Repository;

import com.Android.Android_App.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // This is used by Spring Security to find a user by their email
    Optional<User> findByEmail(String email);
}