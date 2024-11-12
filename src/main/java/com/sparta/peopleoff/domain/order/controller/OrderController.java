package com.sparta.peopleoff.domain.order.controller;

import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.service.OrderService;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // 주문 생성
  @PostMapping("/api/v1/store/{storeId}/order")
  public ResponseEntity<Void> createOnlineOrder(
      @RequestBody OrderPostRequestDto orderPostRequestDto,
      @PathVariable("storeId") UUID storeId,
      UserEntity user
  ) {
    orderService.createOnlineOrder(orderPostRequestDto, storeId, user);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  // 주문 취소
  @DeleteMapping("/api/v1/order/{orderId}")
  public ResponseEntity<Void> cancelOrder(
      @PathVariable UUID orderId,
      UserEntity user
  ) {
    orderService.cancelOrder(orderId, user);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
