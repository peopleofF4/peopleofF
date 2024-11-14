package com.sparta.peopleoff.domain.order.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.order.dto.OrderPatchRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderSearchResponseDto;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import com.sparta.peopleoff.domain.order.service.OrderService;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  // 주문 생성
  @PostMapping("/api/v1/stores/{storeId}/orders")
  public ResponseEntity<ApiResponse<Void>> createOrder(
      @RequestBody OrderPostRequestDto orderPostRequestDto,
      @PathVariable("storeId") UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.createOrder(orderPostRequestDto, storeId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  // 주문 검색 조회 owner 사용
  @GetMapping("/api/v1/stores/{storeId}/orders/search")
  public ResponseEntity<ApiResponse<Page<OrderSearchResponseDto>>> searchOrder(
      @AuthenticationPrincipal UserDetailsImpl user,
      @PathVariable("storeId") UUID storeId,
      @RequestParam(value = "orderType", required = false) OrderType orderType,
      @RequestParam(value = "menuId", required = false) UUID menuId,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    Page<OrderSearchResponseDto> res =
        orderService.searchOrder(storeId, user, orderType, menuId, page - 1, size);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(res));
  }

  // 주문 조회
  @GetMapping("/api/v1/users/{userId}/orders")
  public ResponseEntity<ApiResponse<List<OrderSearchResponseDto>>> getCustomerOrderList(
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    List<OrderSearchResponseDto> res = orderService.getCustomerOrderList(user);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(res));
  }

  // 대면 주문 수정
  @PatchMapping("/api/v1/stores/{storeId}/orders/{orderId}")
  public ResponseEntity<ApiResponse<Void>> updateOffLineOrder(
      @RequestBody OrderPatchRequestDto orderPatchRequestDto,
      @PathVariable("storeId") UUID storeId,
      @PathVariable("orderId") UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.updateOffLineOrder(orderPatchRequestDto, storeId, orderId, user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(ApiResponse.OK(ResBasicCode.OK));
  }

  // 주문 취소
  @DeleteMapping("/api/v1/orders/{orderId}")
  public ResponseEntity<ApiResponse<Void>> cancelOrder(
      @PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.cancelOrder(orderId, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.OK(ResBasicCode.OK));
  }
}
