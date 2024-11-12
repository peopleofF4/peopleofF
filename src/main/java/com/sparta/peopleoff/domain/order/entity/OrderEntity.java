package com.sparta.peopleoff.domain.order.entity;

import com.sparta.peopleoff.domain.order.entity.enums.OrderStatus;
import com.sparta.peopleoff.domain.order.entity.enums.OrderType;
import com.sparta.peopleoff.domain.orderdetail.entity.OrderDetailEntity;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_order")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private int totalPrice;

  @Column(nullable = false, length = 255)
  private String orderRequest;

  // Enum : placed:주문접수 / preparing:준비중 / delivering:배송중 / delivered:배송완료 / canceled:취소
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private OrderStatus orderStatus;

  // Enum : 대면 / 온라인
  @Column(nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private OrderType orderType;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  @OneToMany(mappedBy = "order")
  private List<OrderDetailEntity> orderDetailList = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private StoreEntity store;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  private String deliveryAddress;

  public void addOrderDetail(OrderDetailEntity orderDetail) {
    this.orderDetailList.add(orderDetail);
    orderDetail.setOrder(this);
  }

  public OrderEntity(
      int totalPrice,
      String orderRequest,
      String deliveryAddress,
      StoreEntity store,
      UserEntity user
  ) {
    this.totalPrice = totalPrice;
    this.orderRequest = orderRequest;
    this.deliveryAddress = deliveryAddress;
    this.store = store;
    this.user = user;
    this.orderStatus = OrderStatus.PLACED;
    this.orderType = OrderType.ON_LINE;
    this.expiredAt = LocalDateTime.now().plusMinutes(5);
  }
}
