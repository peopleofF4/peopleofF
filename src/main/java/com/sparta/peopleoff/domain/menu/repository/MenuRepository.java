package com.sparta.peopleoff.domain.menu.repository;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuRepository extends JpaRepository<MenuEntity, UUID> {

}
