package com.sparta.peopleoff.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class OrderPatchRequestDto {

  private Order order;
  private List<MenuItem> menuItems;

  @Getter
  public static class Order {

    @NotBlank
    private int totalPrice;
  }

  @Getter
  public static class MenuItem {

    @NotBlank
    private UUID menuId;
    @NotBlank
    private int menuCount;
  }
}
