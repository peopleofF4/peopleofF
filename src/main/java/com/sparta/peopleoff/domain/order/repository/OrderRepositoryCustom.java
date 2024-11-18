package com.sparta.peopleoff.domain.order.repository;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

  Page<OrderEntity> searchOrder(OrderType orderType, UUID menuID, Pageable pageable);

}
