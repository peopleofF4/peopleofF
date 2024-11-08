package com.sparta.peopleoff.domain.payment.entity;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_payment")
public class PaymentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private int totalPayment;

  // Enum : pending(default):보류 / success:성공 / failed:실패
  @Column(nullable = false, length = 50)
  private String paymentStatus;

  @OneToOne
  @JoinColumn(name = "order_id",nullable = false)
  private OrderEntity order;

}
