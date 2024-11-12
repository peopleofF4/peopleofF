package com.sparta.peopleoff.domain.order.dto;

import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderPostRequestDto {

  private Order order;
  private List<MenuItem> menuItems;

  @Getter
  public static class Order {

    @NotBlank
    private int totalPrice;
    @NotBlank
    private String orderRequest;
    @NotBlank
    private String deliveryAddress;

    public OrderEntity toEntity(StoreEntity store, UserEntity user) {
      return new OrderEntity(
          this.totalPrice,
          this.orderRequest,
          this.deliveryAddress,
          store,
          user);
    }
  }

  @Getter
  public static class MenuItem {

    @NotBlank
    private UUID menuId;
    @NotBlank
    private int menuCount;
  }
}

