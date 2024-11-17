package com.sparta.peopleoff.domain.order.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.order.dto.OrderPatchRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderSearchResponseDto;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import com.sparta.peopleoff.domain.order.repository.OrderRepository;
import com.sparta.peopleoff.domain.orderdetail.entity.OrderDetailEntity;
import com.sparta.peopleoff.domain.orderdetail.repository.OrderDetailRepository;
import com.sparta.peopleoff.domain.payment.entity.PaymentEntity;
import com.sparta.peopleoff.domain.payment.repository.PaymentRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.domain.user.entity.enums.UserRole;
import com.sparta.peopleoff.exception.CustomApiException;
import com.sparta.peopleoff.security.UserDetailsImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final StoreRepository storeRepository;
  private final PaymentRepository paymentRepository;

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
  @Override
  @Transactional(readOnly = true)
  public Page<OrderSearchResponseDto> searchOrder(UUID storeId, UserDetailsImpl user,
      OrderType orderType, UUID menuId, int page, int size, String sortBy) {

    StoreEntity store = findStoreEntity(storeId);

    validateUserAuthorization(user, store);

    size = (size == 30 || size == 50) ? size : 10;

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
    Page<OrderEntity> orderEntities = orderRepository.searchOrder(orderType, menuId, pageable);

    return orderEntities.map(orderEntity -> {
      OrderSearchResponseDto.Order orderDto = mapToOrderDto(orderEntity);

      List<OrderSearchResponseDto.MenuItem> menuItems = orderEntity.getOrderDetailList().stream()
          .map(this::mapToMenuItemDto).collect(Collectors.toList());

      return new OrderSearchResponseDto(orderDto, menuItems);
    });
  }

  /**
   * CUSTOMER의 주문 내역 조회
   *
   * @param user user
   * @return List 주문 내역 조회
   */
  @Override
  @Transactional(readOnly = true)
  public List<OrderSearchResponseDto> getCustomerOrderList(UserDetailsImpl user) {

    List<OrderEntity> orderEntities = orderRepository.findAllByUserId(user.getUser().getId());
    List<OrderSearchResponseDto> responseDtos = new ArrayList<>();

    for (OrderEntity orderEntity : orderEntities) {
      OrderSearchResponseDto.Order orderDto = mapToOrderDto(orderEntity);

      List<OrderSearchResponseDto.MenuItem> menuItems = orderEntity.getOrderDetailList().stream()
          .map(this::mapToMenuItemDto).collect(Collectors.toList());

      responseDtos.add(new OrderSearchResponseDto(orderDto, menuItems));
    }
    return responseDtos;
  }

  /**
   * 주문 생성 메서드
   *
   * @param reqDto  주문 요청시 받는 데이터
   * @param storeId storeId
   * @param user    주문 요청 user
   */
  @Override
  @Transactional
  public void createOrder(OrderPostRequestDto reqDto, UUID storeId, UserDetailsImpl user) {

    StoreEntity store = findStoreEntity(storeId);
    OrderEntity orderEntity = reqDto.getOrder().toEntity(store, user.getUser());

    validateAndSaveOrder(orderEntity, store);

    for (OrderPostRequestDto.MenuItem menuItem : reqDto.getMenuItems()) {
      MenuEntity menu = store.getMenuList().stream()
          .filter(m -> m.getId().equals(menuItem.getMenuId())).findFirst().orElseThrow(
              () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "입력한 메뉴가 존재하지 않습니다"));

      OrderDetailEntity orderDetailEntity =
          OrderDetailEntity.toEntity(menu, orderEntity, menuItem.getMenuCount());

      orderEntity.addOrderDetail(orderDetailEntity);
    }
    orderDetailRepository.saveAll(orderEntity.getOrderDetailList());
    paymentRepository.save(PaymentEntity.toEntity(reqDto.getOrder().getTotalPrice(), orderEntity));
  }

  /**
   * 대면 주문 수정 메서드, owner만 사용 가능
   *
   * @param reqDto  주문 수정시 받는 데이터
   * @param storeId storeId
   * @param orderId orderId
   * @param user    user
   */
  @Override
  @Transactional
  public void updateOffLineOrder(OrderPatchRequestDto reqDto, UUID storeId, UUID orderId,
      UserDetailsImpl user) {

    StoreEntity store = findStoreEntity(storeId);
    OrderEntity orderEntity = findOrderEntity(orderId);

    validateUserPermission(orderEntity, user);

    orderDetailRepository.deleteAll(orderEntity.getOrderDetailList());
    orderEntity.getOrderDetailList().clear();

    orderEntity.updateOrder(reqDto.getOrder().getTotalPrice());

    for (OrderPatchRequestDto.MenuItem menuItem : reqDto.getMenuItems()) {
      MenuEntity menu = store.getMenuList().stream()
          .filter(m -> m.getId().equals(menuItem.getMenuId())).findFirst()
          .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "입력한 메뉴가 존재하지 않습니다"));

      OrderDetailEntity orderDetailEntity =
          OrderDetailEntity.toEntity(menu, orderEntity, menuItem.getMenuCount());

      orderEntity.addOrderDetail(orderDetailEntity);
    }

    orderDetailRepository.saveAll(orderEntity.getOrderDetailList());

    PaymentEntity payment = paymentRepository.findByOrderId(orderId);
    payment.update(reqDto.getOrder().getTotalPrice());
  }

  /**
   * 주문 취소 메서드, 주문 생성후 5분 이내
   *
   * @param orderId orderId
   * @param user    user
   */
  @Override
  @Transactional
  public void cancelOrder(UUID orderId, UserDetailsImpl user) {

    OrderEntity order = findOrderEntity(orderId);

    validateUserPermission(order, user);
    checkOrderCancellationTime(order);

    order.cancel();

    List<OrderDetailEntity> orderDetails = order.getOrderDetailList().stream().toList();
    for (OrderDetailEntity orderDetail : orderDetails) {
      orderDetail.setDeletionStatus(DeletionStatus.DELETED);
    }

    paymentRepository.findByOrderId(orderId).cancel();
  }

  private OrderEntity findOrderEntity(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 주문번호가 존재하지 않습니다"));
  }

  private StoreEntity findStoreEntity(UUID storeId) {
    return storeRepository.findById(storeId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "입력한 가게의 정보가 존재하지 않습니다"));
  }

  private void validateUserAuthorization(UserDetailsImpl user, StoreEntity store) {
    if (!user.getUser().getId().equals(store.getUser().getId())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "주문을 조회할 권한이 없습니다.");
    }
  }

  private void validateUserPermission(OrderEntity order, UserDetailsImpl user) {
    if (!order.getUser().getId().equals(user.getUser().getId())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "권한이 없습니다.");
    }
  }

  private void checkOrderCancellationTime(OrderEntity order) {
    if (LocalDateTime.now().isAfter(order.getExpiredAt())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "주문은 생성 후 5분이 초과하여 삭제할 수 없습니다.");
    }
  }

  private void validateAndSaveOrder(OrderEntity orderEntity, StoreEntity store) {
    if (orderEntity.getUser().getRole() == UserRole.CUSTOMER) {
      orderRepository.save(orderEntity);
    } else if (orderEntity.getUser().getRole() == UserRole.OWNER) {
      orderRepository.save(orderEntity);
      orderEntity.updateOffLine(store);
    } else {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "잘못된 사용자 권한입니다.");
    }
  }

  private OrderSearchResponseDto.Order mapToOrderDto(OrderEntity orderEntity) {
    return new OrderSearchResponseDto.Order(orderEntity.getId(), orderEntity.getUser().getId(),
        orderEntity.getOrderRequest(), orderEntity.getDeliveryAddress(),
        orderEntity.getTotalPrice());
  }

  private OrderSearchResponseDto.MenuItem mapToMenuItemDto(OrderDetailEntity orderDetailEntity) {
    return new OrderSearchResponseDto.MenuItem(orderDetailEntity.getMenu().getId(),
        orderDetailEntity.getMenuCount());
  }
}


