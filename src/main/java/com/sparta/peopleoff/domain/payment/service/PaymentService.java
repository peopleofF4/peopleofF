package com.sparta.peopleoff.domain.payment.service;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.order.repository.OrderRepository;
import com.sparta.peopleoff.domain.payment.repository.PaymentRepository;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import com.sparta.peopleoff.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;

//  public void createPayment(UUID orderId, UserEntity user) {
//    OrderEntity orderEntity = orderRepository.findById(orderId)
//        .orElseThrow(()->new IllegalArgumentException("해당 주문번호가 존재하지 않습니다"));
//
//
//  }
}
