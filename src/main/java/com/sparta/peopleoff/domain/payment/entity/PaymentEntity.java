package com.sparta.peopleoff.domain.payment.entity;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.entity.enums.OrderStatus;
import com.sparta.peopleoff.domain.payment.entity.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
  @Enumerated(EnumType.STRING)
  private PaymentStatus paymentStatus;

  @OneToOne
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity order;

  private PaymentEntity(int totalPayment, OrderEntity order) {
    this.paymentStatus = PaymentStatus.SUCCESS;
    this.totalPayment = totalPayment;
    this.order = order;
  }

  public static PaymentEntity toEntity(int totalPayment, OrderEntity order) {
    return new PaymentEntity(totalPayment, order);
  }

  public void cancel() {
    this.paymentStatus = PaymentStatus.FAILED;
  }
}
