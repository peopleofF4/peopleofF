package com.sparta.peopleoff.domain.category.entity;

import com.sparta.peopleoff.domain.category.dto.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Setter
    @Column(nullable = false, length = 100)
    private String categoryName;

    public CategoryEntity(CategoryRequestDto categoryRequestDto) {
        this.categoryName = categoryRequestDto.getCategoryName();
    }
}
