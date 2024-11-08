package com.sparta.peopleoff.domain.store.entity;

import com.sparta.peopleoff.common.enums.DeletionStatusEnum;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.store.entity.enums.RegistrationStatusEnum;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_store")
@Getter
@NoArgsConstructor
public class StoreEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String storeName;

  @Column(nullable = false, length = 255)
  private String storeAddress;

  @Column(nullable = false, length = 50)
  private String storePhoneNumber;

  @Column(nullable = false, length = 50)
  private String businessNumber;

  @Column(nullable = false)
  private int minimumOrderPrice;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RegistrationStatusEnum registrationStatus = RegistrationStatusEnum.PENDING;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DeletionStatusEnum deletionStatus = DeletionStatusEnum.ACTIVE;

  @Column
  private int totalRating;

  @Column
  private int ratingCount;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @OneToMany(mappedBy = "store")
  private List<MenuEntity> menuList;


  public StoreEntity(String storeName, String storeAddress, String storePhoneNumber,
      String businessNumber, int minimumOrderPrice, UserEntity user,
      CategoryEntity category) {
    this.storeName = storeName;
    this.storeAddress = storeAddress;
    this.storePhoneNumber = storePhoneNumber;
    this.businessNumber = businessNumber;
    this.minimumOrderPrice = minimumOrderPrice;
    this.user = user;
    this.category = category;
  }
}