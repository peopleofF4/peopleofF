package com.sparta.peopleoff.domain.order.repository;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID>, OrderRepositoryCustom {

  List<OrderEntity> findAllByUserId(Long id);
}
