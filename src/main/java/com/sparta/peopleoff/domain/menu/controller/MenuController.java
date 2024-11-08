package com.sparta.peopleoff.domain.menu.controller;

import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.service.MenuService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MenuController {

  private final MenuService menuService;

  public MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  @PostMapping("/stores/{storeId}/menu")
  public ResponseEntity<MenuEntity> registerMenu(@PathVariable UUID storeId,
      @RequestBody MenuPostRequestDto requestDto) {
    MenuEntity menu = menuService.registerMenu(requestDto, storeId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/stores/{storeId}/menu")
  public ResponseEntity<List<MenuGetResponseDto>> getAllMenusByStore(@PathVariable UUID storeId) {
    List<MenuGetResponseDto> menus = menuService.getMenusByStoreId(storeId);
    return ResponseEntity.ok(menus);
  }

  @GetMapping("/menu/{menuId}")
  public ResponseEntity<MenuGetResponseDto> getMenuById(@PathVariable UUID menuId) {
    MenuGetResponseDto menu = menuService.getMenuById(menuId);
    return ResponseEntity.ok(menu);
  }

  @PutMapping("/menu/{menuId}")
  public ResponseEntity<MenuEntity> updateMenu(@PathVariable UUID menuId,
      @RequestBody MenuPutRequestDto requestDto) {
    MenuEntity menu = menuService.updateMenu(menuId, requestDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/menu/{menuId}")
  public ResponseEntity<?> deleteMenu(@PathVariable UUID menuId) {
    menuService.deleteMenu(menuId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/menu/search")
  public ResponseEntity<List<MenuGetResponseDto>> searchMenus(@RequestParam String keyword) {
    List<MenuGetResponseDto> menus = menuService.searchMenus(keyword);
    return ResponseEntity.ok(menus);
  }
}
