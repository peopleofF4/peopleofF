package com.sparta.peopleoff.domain.store.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.enums.RegistrationStatus;
import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.store.dto.StorePutRequestDto;
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
public class StoreEntity extends SoftDeleteEntity {

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
  private RegistrationStatus registrationStatus = RegistrationStatus.PENDING;

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

  public void update(StorePutRequestDto dto, CategoryEntity category) {
    this.storeName = dto.getStoreName();
    this.storeAddress = dto.getStoreAddress();
    this.storePhoneNumber = dto.getStorePhoneNumber();
    this.businessNumber = dto.getBusinessNumber();
    this.minimumOrderPrice = dto.getMinimumOrderPrice();
    this.category = category;
  }

  public void delete() {
    this.setDeletionStatus(DeletionStatus.DELETED);
  }

  public void setRegistrationStatus(
      RegistrationStatus registrationStatus) {
    this.registrationStatus = registrationStatus;
  }

  public void addRating(int newRating) {
    this.totalRating += newRating;
    this.ratingCount += 1;
  }

  public void updateRating(int previousRating, int newRating) {
    this.totalRating = this.totalRating - previousRating + newRating;
  }

  public void removeRating(int rating) {
    this.totalRating -= rating;
    this.ratingCount = Math.max(this.ratingCount - 1, 0); // 최소 0으로 유지
  }
}