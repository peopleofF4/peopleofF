package com.sparta.peopleoff.domain.payment.repository;

import com.sparta.peopleoff.domain.payment.entity.PaymentEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {

  PaymentEntity findByOrderId(UUID orderId);
}
