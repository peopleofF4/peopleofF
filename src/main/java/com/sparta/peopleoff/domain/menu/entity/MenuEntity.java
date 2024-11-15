package com.sparta.peopleoff.domain.menu.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatusEnum;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_menu")
public class MenuEntity extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String menuName;

  @Column(nullable = false, length = 255)
  private String menuDescription;

  @Column(nullable = false)
  private int price;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private MenuStatusEnum menuStatus = MenuStatusEnum.ON_SALE;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private StoreEntity store;

  public MenuEntity(String menuName, String menuDescription, int price, StoreEntity store) {
    this.menuName = menuName;
    this.menuDescription = menuDescription;
    this.price = price;
    this.store = store;
  }

  public void update(String menuName, String menuDescription, int price) {
    this.menuName = menuName;
    this.menuDescription = menuDescription;
    this.price = price;
  }

  public void updateMenuStatus(MenuStatusEnum menuStatus) {
    this.menuStatus = menuStatus;
  }

  public void delete() {
    this.setDeletionStatus(DeletionStatus.DELETED);
  }
}