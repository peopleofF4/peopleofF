package com.sparta.peopleoff.domain.category.repository;

import com.sparta.peopleoff.domain.category.entity.CategoryEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

  Optional<CategoryEntity> findByCategoryName(String categoryName);
}
