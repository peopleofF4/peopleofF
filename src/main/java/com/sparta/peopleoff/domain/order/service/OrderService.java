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

  /**
   * owner의 주문 목록 조회 메서드 요청시 값이 없으면 전체조회 타입과 menuId로 조회가능
   *
   * @param storeId   storeId
   * @param user      owner만 사용가능
   * @param orderType 타입으로 조회 가능
   * @param menuId    menuId로 조회 가능
   * @param page      10
   * @param size      size
   * @param sortBy    sortBy
   * @return page 주문 목록 조회
   */
  Page<OrderSearchResponseDto> searchOrder(UUID storeId, UserDetailsImpl user, OrderType orderType,
      UUID menuId, int page, int size, String sortBy);

  /**
   * CUSTOMER의 주문 내역 조회
   *
   * @param user user
   * @return List 주문 내역 조히
   */
  List<OrderSearchResponseDto> getCustomerOrderList(UserDetailsImpl user);

  /**
   * 주문 생성 메서드
   *
   * @param reqDto  주문 요청시 받는 데이터
   * @param storeId storeId
   * @param user    주문 요청 user
   */
  void createOrder(OrderPostRequestDto reqDto, UUID storeId, UserDetailsImpl user);

  /**
   * 대면 주문 수정 메서드, owner만 사용 가능
   *
   * @param reqDto  주문 수정시 받는 데이터
   * @param storeId storeId
   * @param orderId orderId
   * @param user    user
   */
  void updateOffLineOrder(OrderPatchRequestDto reqDto, UUID storeId, UUID orderId,
      UserDetailsImpl user);

  /**
   * 주문 취소 메서드, 주문 생성후 5분 이내
   *
   * @param orderId orderId
   * @param user    user
   */
  void cancelOrder(UUID orderId, UserDetailsImpl user);
}
