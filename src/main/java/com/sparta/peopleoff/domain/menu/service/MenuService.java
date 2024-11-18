package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MenuService {

  void registerMenu(MenuPostRequestDto requestDto, UUID storeId);

  MenuGetResponseDto getMenuById(UUID menuId);

  List<MenuGetResponseDto> getMenusByStoreId(UUID storeId, Pageable pageable);

  void updateMenu(UUID menuId, MenuPutRequestDto requestDto);

  void updateMenuStatus(UUID menuId, MenuStatus status);

  void deleteMenu(UUID menuId);

  List<MenuGetResponseDto> searchMenus(String keyword, Pageable pageable);
}