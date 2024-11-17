package com.sparta.peopleoff.domain.menu.repository;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {

  Page<MenuEntity> findByStoreIdAndDeletionStatus(UUID storeId, DeletionStatus deletionStatus,
      Pageable pageable);

  Page<MenuEntity> findByMenuNameContainingOrMenuDescriptionContainingAndDeletionStatus(
      String menuNameKeyword, String menuDescriptionKeyword, DeletionStatus deletionStatus,
      Pageable pageable
  );
}