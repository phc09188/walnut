package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.UserReceptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReceptionStatusRepository extends JpaRepository<UserReceptionStatus, String> {
}
