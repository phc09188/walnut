package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByEmailAuthKey(String emailAuthKey);
}
