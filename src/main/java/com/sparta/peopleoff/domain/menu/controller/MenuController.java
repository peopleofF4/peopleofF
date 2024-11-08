package com.sparta.peopleoff.domain.menu.controller;

import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.service.MenuService;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

  private final MenuService menuService;

  public MenuController(MenuService menuService) {
    this.menuService = menuService;
  }

  @PostMapping("/{storeId}")
  public ResponseEntity<MenuEntity> registerMenu(@PathVariable UUID storeId,
      @RequestBody MenuPostRequestDto requestDto) {
    MenuEntity menu = menuService.registerMenu(requestDto, storeId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{menuId}")
  public ResponseEntity<MenuEntity> updateMenu(@PathVariable UUID menuId,
      @RequestBody MenuPutRequestDto requestDto) {
    MenuEntity menu = menuService.updateMenu(menuId, requestDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{menuId}")
  public ResponseEntity<?> deleteMenu(@PathVariable UUID menuId) {
    menuService.deleteMenu(menuId);
    return ResponseEntity.noContent().build();
  }
}
