package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.repository.MenuRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;

  public MenuService(MenuRepository menuRepository, StoreRepository storeRepository) {
    this.menuRepository = menuRepository;
    this.storeRepository = storeRepository;
  }

  @Transactional
  public MenuEntity registerMenu(MenuPostRequestDto requestDto, UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 스토어 ID입니다."));

    MenuEntity menu = new MenuEntity(
        requestDto.getMenuName(),
        requestDto.getMenuDescription(),
        requestDto.getPrice(),
        store
    );

    return menuRepository.save(menu);
  }
}
