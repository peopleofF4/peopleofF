package com.sparta.peopleoff.domain.order.entity;

import com.sparta.peopleoff.domain.orderdetail.entity.OrderDetailEntity;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
  @Column(nullable = false, length = 50)
  private String orderStatus;

  @Column(nullable = false, length = 50)
  private String orderType;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  @OneToMany(mappedBy = "order")
  private List<OrderDetailEntity> orderDetailList;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private StoreEntity store;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

}
