package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.repository.MenuRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional(readOnly = true)
  public List<MenuGetResponseDto> getMenusByStoreId(UUID storeId) {
    return menuRepository.findByStoreId(storeId).stream()
        .map(MenuGetResponseDto::new)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public MenuGetResponseDto getMenuById(UUID menuId) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));
    return new MenuGetResponseDto(menu);
  }

  @Transactional
  public MenuEntity updateMenu(UUID menuId, MenuPutRequestDto requestDto) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 ID입니다."));

    menu.update(requestDto.getMenuName(), requestDto.getMenuDescription(), requestDto.getPrice());
    return menu;
  }

  @Transactional
  public void deleteMenu(UUID menuId) {
    if (!menuRepository.existsById(menuId)) {
      throw new IllegalArgumentException("유효하지 않은 메뉴 ID입니다.");
    }
    menuRepository.deleteById(menuId);
  }
}
