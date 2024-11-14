package com.sparta.peopleoff.domain.menu.repository;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {

  Optional<MenuEntity> findByMenuName(String menuName);
}
