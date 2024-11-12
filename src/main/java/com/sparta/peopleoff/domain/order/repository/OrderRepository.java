package com.sparta.peopleoff.domain.order.repository;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

}
