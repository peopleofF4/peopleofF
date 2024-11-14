package com.sparta.peopleoff.domain.menu.entity;

import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class MenuEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String menuName;

  @Column(nullable = false, length = 255)
  private String menuDescription;

  @Column(nullable = false)
  private int price;

  // Enum : on_sale:판매중 / sold_out:하루품절 / hiding:숨김
  @Column(nullable = false, length = 50)
  private String menuStatus;

  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  private StoreEntity store;
}
