package com.sparta.peopleoff.domain.payment.controller;

import com.sparta.peopleoff.domain.payment.service.PaymentService;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

//  @PostMapping("{orderId}/payment")
//  public ResponseEntity<Void> payment(@PathVariable("orderId") UUID orderId, UserEntity user) {
//    paymentService.createPayment(orderId, user);
//    return ResponseEntity.ok().build();
//  }
}
