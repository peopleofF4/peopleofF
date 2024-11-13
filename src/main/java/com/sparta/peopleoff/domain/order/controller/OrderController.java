package com.sparta.peopleoff.domain.order.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.service.OrderService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
  public ResponseEntity<ApiResponse<Void>> createOrder(
      @RequestBody OrderPostRequestDto orderPostRequestDto,
      @PathVariable("storeId") UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.createOrder(orderPostRequestDto, storeId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.OK(ResBasicCode.CREATED));
  }

//  // 대면 주문 수정
//  @PatchMapping("/api/v1/store/{storeId}/order/{orderId}")
//  public ResponseEntity<ApiResponse<Void>> updateOfflineOrder(
//      @RequestBody OrderPatchRequestDto orderPatchRequestDto,
//      @PathVariable("storeId") UUID storeId,
//      @PathVariable("orderId") UUID orderId,
//      @AuthenticationPrincipal UserDetailsImpl user
//  ) {
//    orderService.updateOfflineOrder(orderPatchRequestDto, storeId, orderId, user);
//    return ResponseEntity.status(HttpStatus.OK)
//        .body(ApiResponse.OK(ResBasicCode.OK));
//  }

  // 주문 취소
  @DeleteMapping("/api/v1/order/{orderId}")
  public ResponseEntity<ApiResponse<Void>> cancelOrder(
      @PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.cancelOrder(orderId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.OK(ResBasicCode.OK));
  }
}
