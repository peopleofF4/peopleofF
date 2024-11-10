package com.sparta.peopleoff.domain.category.entity;

import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "p_category")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 100)
  private String categoryName;

  public CategoryEntity(CategoryRequestDto categoryRequestDto) {
    this.categoryName = categoryRequestDto.getCategoryName();
  }
}
