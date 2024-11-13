package com.sparta.peopleoff.domain.orderdetail.entity;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.order.entity.OrderEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private DeletionStatus deletionStatus;

  private OrderDetailEntity(MenuEntity menu, OrderEntity order, int menuCount) {
    this.menu = menu;
    this.order = order;
    this.menuCount = menuCount;
    this.deletionStatus = DeletionStatus.ACTIVE;
  }

  public static OrderDetailEntity toEntity(MenuEntity menu, OrderEntity order, int menuCount) {
    return new OrderDetailEntity(menu, order, menuCount);
  }

  public void cancel() {
    this.deletionStatus = DeletionStatus.DELETED;
  }

  public void updateOrderDetail(MenuEntity menu, int menuCount) {
    this.menu = menu;
    this.menuCount = menuCount;
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }
}
