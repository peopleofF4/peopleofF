package com.sparta.peopleoff.domain.payment.repository;

import com.sparta.peopleoff.domain.payment.entity.PaymentEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {

}
