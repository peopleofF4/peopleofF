package com.sparta.peopleoff.domain.order.service;

import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.order.dto.OrderPatchRequestDto;
import com.sparta.peopleoff.domain.order.dto.OrderPostRequestDto;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
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
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderDetailRepository orderDetailRepository;
  private final StoreRepository storeRepository;
  private final PaymentRepository paymentRepository;

  @Transactional
  public void createOrder(OrderPostRequestDto reqDto, UUID storeId, UserDetailsImpl user) {

    StoreEntity store = findStoreEntity(storeId);
    OrderEntity orderEntity = reqDto.getOrder().toEntity(store, user.getUser());

    if (orderEntity.getUser().getRole() == UserRole.CUSTOMER) {
      orderRepository.save(orderEntity);
    } else if (orderEntity.getUser().getRole() == UserRole.OWNER) {
      orderRepository.save(orderEntity);
      orderEntity.updateOffLine(store);
    } else {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "잘못된 사용자 권한입니다.");
    }

    for (OrderPostRequestDto.MenuItem menuItem : reqDto.getMenuItems()) {
      MenuEntity menu = store.getMenuList().stream()
          .filter(m -> m.getId().equals(menuItem.getMenuId()))
          .findFirst()
          .orElseThrow(() -> new CustomApiException
              (ResBasicCode.BAD_REQUEST, "입력한 메뉴 '" + menuItem.getMenuId() + "'가 존재하지 않습니다"));

      OrderDetailEntity orderDetailEntity
          = OrderDetailEntity.toEntity(menu, orderEntity, menuItem.getMenuCount());
      orderEntity.addOrderDetail(orderDetailEntity);
    }
    orderDetailRepository.saveAll(orderEntity.getOrderDetailList());
    paymentRepository.save(PaymentEntity.toEntity(reqDto.getOrder().getTotalPrice(), orderEntity));
  }

  @Transactional
  public void updateOffLineOrder(OrderPatchRequestDto reqDto, UUID storeId,
      UUID orderId, UserDetailsImpl user) {

    StoreEntity store = findStoreEntity(storeId);
    OrderEntity orderEntity = findOrderEntity(orderId);

    if (!orderEntity.getUser().getId().equals(user.getUser().getId())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "주문을 수정할 권한이 없습니다.");
    }

    orderDetailRepository.deleteAll(orderEntity.getOrderDetailList());
    orderEntity.getOrderDetailList().clear();

    orderEntity.updateOrder(reqDto.getOrder().getTotalPrice());

    for (OrderPatchRequestDto.MenuItem menuItem : reqDto.getMenuItems()) {
      MenuEntity menu = store.getMenuList().stream()
          .filter(m -> m.getId().equals(menuItem.getMenuId()))
          .findFirst()
          .orElseThrow(() -> new CustomApiException
              (ResBasicCode.BAD_REQUEST, "입력한 메뉴 '" + menuItem.getMenuId() + "'가 존재하지 않습니다"));

      OrderDetailEntity orderDetailEntity
          = OrderDetailEntity.toEntity(menu, orderEntity, menuItem.getMenuCount());
      orderEntity.addOrderDetail(orderDetailEntity);
    }

    orderDetailRepository.saveAll(orderEntity.getOrderDetailList());

    PaymentEntity payment = paymentRepository.findByOrderId(orderId);
    payment.update(reqDto.getOrder().getTotalPrice());
  }

  @Transactional
  public void cancelOrder(UUID orderId, UserDetailsImpl user) {

    OrderEntity order = findOrderEntity(orderId);

    if (!order.getUser().getId().equals(user.getUser().getId())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "주문을 삭제할 권한이 없습니다.");
    }

    if (LocalDateTime.now().isAfter(order.getExpiredAt())) {
      throw new CustomApiException(ResBasicCode.BAD_REQUEST, "주문은 생성 후 5분이 초과하여 삭제할 수 없습니다.");
    }

    order.cancel();

    List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderId(orderId);
    for (OrderDetailEntity orderDetail : orderDetails) {
      orderDetail.cancel();
    }
    paymentRepository.findByOrderId(orderId).cancel();
  }

  private OrderEntity findOrderEntity(UUID orderId) {
    return orderRepository.findById(orderId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 주문번호가 존재하지 않습니다"));
  }

  private OrderDetailEntity findOrderDetailEntity(UUID orderDetailId) {
    return orderDetailRepository.findById(orderDetailId).orElseThrow(
        () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "입력한 주문 정보가 존재하지 않습니다."));
  }

  private StoreEntity findStoreEntity(UUID storeId) {
    return storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomApiException(
            ResBasicCode.BAD_REQUEST, "입력한 가게의 정보가 존재하지 않습니다"));
  }
}


