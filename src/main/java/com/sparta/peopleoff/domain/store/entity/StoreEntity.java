package com.sparta.peopleoff.domain.store.entity;


import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

  // Enum : pending(default):보류 / accepted:수락 / rejected:거절
  @Column(nullable = false)
  private String registrationStatus;

  // Enum : active:활성 / deleted:삭제
  @Column(nullable = false)
  private String deletionStatus;

  @Column
  private int totalRating;

  @Column
  private int ratingCount;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @OneToOne
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @OneToMany(mappedBy = "store")
  private List<MenuEntity> menuList;


}
