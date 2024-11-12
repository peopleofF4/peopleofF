package com.sparta.peopleoff.domain.orderdetail.entity;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_order_detail")
public class OrderDetailEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private int menuCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "menu_id", nullable = false)
  private MenuEntity menu;

  private OrderDetailEntity(MenuEntity menu, OrderEntity order, int menuCount) {
    this.menu = menu;
    this.order = order;
    this.menuCount = menuCount;
  }

  public static OrderDetailEntity toEntity(MenuEntity menu, OrderEntity order, int menuCount) {
    return new OrderDetailEntity(menu, order, menuCount);
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }
}
