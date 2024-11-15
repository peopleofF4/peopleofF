package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import java.util.List;
import java.util.UUID;

public interface MenuService {

  MenuEntity registerMenu(MenuPostRequestDto requestDto, UUID storeId);

  List<MenuGetResponseDto> getMenusByStoreId(UUID storeId);

  MenuGetResponseDto getMenuById(UUID menuId);

  MenuEntity updateMenu(UUID menuId, MenuPutRequestDto requestDto);

  void deleteMenu(UUID menuId);

  List<MenuGetResponseDto> searchMenus(String keyword);
}