package com.sparta.peopleoff.domain.orderdetail.repository;

import com.sparta.peopleoff.domain.orderdetail.entity.OrderDetailEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, UUID> {

}
