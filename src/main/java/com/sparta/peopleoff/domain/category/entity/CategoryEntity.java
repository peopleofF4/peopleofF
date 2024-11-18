package com.sparta.peopleoff.domain.category.entity;

import com.sparta.peopleoff.common.entity.SoftDeleteEntity;
import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_category")
public class CategoryEntity extends SoftDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Setter
  @Column(nullable = false, length = 100, unique = true)
  private String categoryName;

  public CategoryEntity(CategoryRequestDto categoryRequestDto) {
    this.categoryName = categoryRequestDto.getCategoryName();
  }
}
