package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatusEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MenuService {

  void registerMenu(MenuPostRequestDto requestDto, UUID storeId);

  MenuGetResponseDto getMenuById(UUID menuId);

  List<MenuGetResponseDto> getMenusByStoreId(UUID storeId, Pageable pageable);

  MenuGetResponseDto updateMenu(UUID menuId, MenuPutRequestDto requestDto);

  void updateMenuStatus(UUID menuId, MenuStatusEnum status);

  void deleteMenu(UUID menuId);

  List<MenuGetResponseDto> searchMenus(String keyword, Pageable pageable);
}