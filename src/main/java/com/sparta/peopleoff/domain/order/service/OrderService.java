package com.sparta.peopleoff.domain.order.service;

import com.sparta.peopleoff.domain.order.dto.OrderPatchRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderSearchResponseDto;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface OrderService {

  Page<OrderSearchResponseDto> searchOrder(UUID storeId, UserDetailsImpl user, OrderType orderType,
      UUID menuId, int page, int size, String sortBy);

  List<OrderSearchResponseDto> getCustomerOrderList(UserDetailsImpl user);

  void createOrder(OrderPostRequestDto reqDto, UUID storeId, UserDetailsImpl user);

  void updateOffLineOrder(OrderPatchRequestDto reqDto, UUID storeId, UUID orderId,
      UserDetailsImpl user);

  void cancelOrder(UUID orderId, UserDetailsImpl user);
}
