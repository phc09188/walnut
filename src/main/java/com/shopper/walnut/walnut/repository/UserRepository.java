package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByEmailAuthKey(String emailAuthKey);
}
