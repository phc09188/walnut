package com.shopper.walnut.walnut.repository;

import com.shopper.walnut.walnut.model.entity.QnA;
import com.shopper.walnut.walnut.model.status.QnaStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {
    Optional<QnA> findBySubject(String subject);
    List<QnA> findAllByStatus(QnaStatus status);
}
