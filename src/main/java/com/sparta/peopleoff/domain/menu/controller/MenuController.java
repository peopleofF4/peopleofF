package com.sparta.peopleoff.domain.menu.controller;

import com.sparta.peopleoff.common.apiresponse.ApiResponse;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatusEnum;
import com.sparta.peopleoff.domain.menu.service.MenuService;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> registerMenu(
      @PathVariable UUID storeId,
      @RequestBody MenuPostRequestDto requestDto) {
    menuService.registerMenu(requestDto, storeId);
    return ResponseEntity.status(ResBasicCode.CREATED.getHttpStatusCode())
        .body(ApiResponse.OK(ResBasicCode.CREATED));
  }

  @GetMapping("/stores/{storeId}/menu")
  public ResponseEntity<ApiResponse<List<MenuGetResponseDto>>> getAllMenusByStore(
      @PathVariable UUID storeId,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page) {

    pageSize = (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;

    Pageable pageable = PageRequest.of(
        page,
        pageSize,
        Sort.by(
            sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
            sortBy.equals("updatedAt") ? "updatedAt" : "createdAt"
        )
    );

    List<MenuGetResponseDto> menus = menuService.getMenusByStoreId(storeId, pageable);
    return ResponseEntity.ok(ApiResponse.OK(menus, ResBasicCode.OK));
  }

  @GetMapping("/menu/{menuId}")
  public ResponseEntity<ApiResponse<MenuGetResponseDto>> getMenuById(@PathVariable UUID menuId) {
    MenuGetResponseDto menu = menuService.getMenuById(menuId);
    return ResponseEntity.ok(ApiResponse.OK(menu, ResBasicCode.OK));
  }

  @PutMapping("/menu/{menuId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<MenuGetResponseDto>> updateMenu(
      @PathVariable UUID menuId,
      @RequestBody MenuPutRequestDto requestDto) {
    MenuGetResponseDto menu = menuService.updateMenu(menuId, requestDto);
    return ResponseEntity.ok(ApiResponse.OK(menu, ResBasicCode.OK));
  }

  @PatchMapping("/menu/{menuId}/status")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> updateMenuStatus(
      @PathVariable UUID menuId,
      @RequestParam MenuStatusEnum status) {
    menuService.updateMenuStatus(menuId, status);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @DeleteMapping("/menu/{menuId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
  public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable UUID menuId) {
    menuService.deleteMenu(menuId);
    return ResponseEntity.ok(ApiResponse.OK(ResBasicCode.OK));
  }

  @GetMapping("/menu/search")
  public ResponseEntity<ApiResponse<List<MenuGetResponseDto>>> searchMenus(
      @RequestParam String keyword,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    pageSize = (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;

    Pageable pageable = PageRequest.of(
        page,
        pageSize,
        Sort.by(
            sortDirection.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
            sortBy.equals("updatedAt") ? "updatedAt" : "createdAt"
        )
    );

    List<MenuGetResponseDto> menus = menuService.searchMenus(keyword, pageable);
    return ResponseEntity.ok(ApiResponse.OK(menus, ResBasicCode.OK));
  }
}