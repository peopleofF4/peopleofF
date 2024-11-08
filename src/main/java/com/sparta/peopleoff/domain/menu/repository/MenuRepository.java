package com.sparta.peopleoff.domain.menu.repository;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {

  List<MenuEntity> findByStoreId(UUID storeId);
}