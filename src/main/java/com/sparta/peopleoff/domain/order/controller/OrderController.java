package com.sparta.peopleoff.domain.order.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResSuccessCode;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

  private final OrderService orderService;

  /**
   * 주문 생성 api
   *
   * @param orderPostRequestDto 입력 받을 주문 생성 데이터
   * @param storeId             storeId
   * @param user                요청한 유저
   * @return response
   */
  @PostMapping("/stores/{storeId}/orders")
  public ResponseEntity<ApiResponse<Void>> createOrder(
      @RequestBody OrderPostRequestDto orderPostRequestDto,
      @PathVariable("storeId") UUID storeId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.createOrder(orderPostRequestDto, storeId, user);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.OK(ResSuccessCode.CREATE_ORDER));
  }

  /**
   * 주문 검색 조회 api OWNER 사용
   *
   * @param user      owner
   * @param storeId   storeId
   * @param orderType ON_LINE / OFF_LINE
   * @param menuId    menuId
   * @param page      defaultValue = "1"
   * @param size      defaultValue = "10"
   * @param sortBy    created_At/updated_At
   * @return response
   */
  @GetMapping("/stores/{storeId}/orders/search")
  public ResponseEntity<ApiResponse<Page<OrderSearchResponseDto>>> searchOrder(
      @AuthenticationPrincipal UserDetailsImpl user,
      @PathVariable("storeId") UUID storeId,
      @RequestParam(value = "orderType", required = false) OrderType orderType,
      @RequestParam(value = "menuId", required = false) UUID menuId,
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy", defaultValue = "created_At") String sortBy
  ) {
    Page<OrderSearchResponseDto> res =
        orderService.searchOrder(storeId, user, orderType, menuId, page - 1, size, sortBy);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(res));
  }

  /**
   * 주문 조회 api CUSTOMER 사용
   *
   * @param user CUSTOMER
   * @return response
   */
  @GetMapping("/users/{userId}/orders")
  public ResponseEntity<ApiResponse<List<OrderSearchResponseDto>>> getCustomerOrderList(
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    List<OrderSearchResponseDto> res = orderService.getCustomerOrderList(user);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(res));
  }

  /**
   * 대면 주문 수정 api OWNER 사용
   *
   * @param orderPatchRequestDto 주문 수정 데이터
   * @param storeId              storeId
   * @param orderId              orderId
   * @param user                 OWNER
   * @return response
   */
  @PatchMapping("/stores/{storeId}/orders/{orderId}")
  public ResponseEntity<ApiResponse<Void>> updateOffLineOrder(
      @RequestBody OrderPatchRequestDto orderPatchRequestDto,
      @PathVariable("storeId") UUID storeId,
      @PathVariable("orderId") UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.updateOffLineOrder(orderPatchRequestDto, storeId, orderId, user);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.UPDATE_ORDER));
  }

  /**
   * 주문 취소 api
   *
   * @param orderId orderId
   * @param user    user
   * @return response
   */
  @DeleteMapping("/orders/{orderId}")
  public ResponseEntity<ApiResponse<Void>> cancelOrder(
      @PathVariable UUID orderId,
      @AuthenticationPrincipal UserDetailsImpl user
  ) {
    orderService.cancelOrder(orderId, user);
    return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.OK(ResSuccessCode.DELETE_ORDER));
  }
}
