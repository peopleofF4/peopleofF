package com.sparta.peopleoff.domain.order.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderSearchResponseDto {

  private Order order;
  private List<MenuItem> menuItems;

  public OrderSearchResponseDto(Order order, List<MenuItem> menuItems) {
    this.order = order;
    this.menuItems = menuItems;
  }

  @Getter
  public static class Order {

    private UUID orderId;
    private Long customerId;
    private String orderRequest;
    private String deliveryAddress;
    private int totalPrice;

    public Order(UUID orderId, Long customerId, String orderRequest, String deliveryAddress,
        int totalPrice) {
      this.orderId = orderId;
      this.customerId = customerId;
      this.orderRequest = orderRequest;
      this.deliveryAddress = deliveryAddress;
      this.totalPrice = totalPrice;
    }
  }

  @Getter
  public static class MenuItem {

    private UUID menuId;
    private int menuCount;

    public MenuItem(UUID menuId, int menuCount) {
      this.menuId = menuId;
      this.menuCount = menuCount;
    }
  }
}
