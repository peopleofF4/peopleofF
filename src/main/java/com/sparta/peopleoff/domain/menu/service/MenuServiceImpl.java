package com.sparta.peopleoff.domain.menu.service;

import com.sparta.peopleoff.common.enums.DeletionStatus;
import com.sparta.peopleoff.common.rescode.ResBasicCode;
import com.sparta.peopleoff.domain.menu.dto.MenuGetResponseDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPostRequestDto;
import com.sparta.peopleoff.domain.menu.dto.MenuPutRequestDto;
import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatus;
import com.sparta.peopleoff.domain.menu.repository.MenuRepository;
import com.sparta.peopleoff.domain.store.entity.StoreEntity;
import com.sparta.peopleoff.domain.store.repository.StoreRepository;
import com.sparta.peopleoff.exception.CustomApiException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final MenuRepository menuRepository;
  private final StoreRepository storeRepository;

  /**
   * 메뉴 등록
   *
   * @param menuRequestDto
   * @param storeId
   */
  @Override
  @Transactional
  public void registerMenu(MenuPostRequestDto menuRequestDto, UUID storeId) {
    StoreEntity store = storeRepository.findById(storeId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 스토어 ID입니다."));

    MenuEntity newMenu = new MenuEntity(
        menuRequestDto.getMenuName(),
        menuRequestDto.getMenuDescription(),
        menuRequestDto.getPrice(),
        store
    );

    menuRepository.save(newMenu);
  }

  /**
   * 특정 가게의 전체 메뉴 조회
   *
   * @param storeId
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<MenuGetResponseDto> getMenusByStoreId(UUID storeId, Pageable pageable) {
    Page<MenuEntity> menuPage = menuRepository.findByStoreIdAndDeletionStatus(storeId,
        DeletionStatus.ACTIVE, pageable);

    return menuPage.stream()
        .map(MenuGetResponseDto::new)
        .collect(Collectors.toList());
  }

  /**
   * 특정 메뉴 조회
   *
   * @param menuId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public MenuGetResponseDto getMenuById(UUID menuId) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(
            () -> new CustomApiException(ResBasicCode.BAD_REQUEST, "유효하지 않은 메뉴 ID입니다."));
    return new MenuGetResponseDto(menu);
  }

  /**
   * 메뉴 수정
   *
   * @param menuId
   * @param menuUpdateRequestDto
   */
  @Override
  @Transactional
  public void updateMenu(UUID menuId, MenuPutRequestDto menuUpdateRequestDto) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 메뉴를 찾을 수 없습니다."));

    menu.update(menuUpdateRequestDto.getMenuName(), menuUpdateRequestDto.getMenuDescription(),
        menuUpdateRequestDto.getPrice());
  }

  /**
   * 메뉴 상태 변경
   *
   * @param menuId
   * @param status
   */
  @Transactional
  @Override
  public void updateMenuStatus(UUID menuId, MenuStatus status) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 메뉴를 찾을 수 없습니다."));

    menu.updateMenuStatus(status);
  }

  /**
   * 메뉴 삭제 (soft delete)
   *
   * @param menuId
   */
  @Override
  @Transactional
  public void deleteMenu(UUID menuId) {
    MenuEntity menu = menuRepository.findById(menuId)
        .orElseThrow(() -> new CustomApiException(ResBasicCode.BAD_REQUEST, "해당 메뉴를 찾을 수 없습니다."));

    menu.delete();
  }

  /**
   * 메뉴 검색
   *
   * @param keyword
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public List<MenuGetResponseDto> searchMenus(String keyword, Pageable pageable) {
    Page<MenuEntity> menuPage = menuRepository.findByMenuNameContainingOrMenuDescriptionContainingAndDeletionStatus(
        keyword, keyword, DeletionStatus.ACTIVE, pageable);

    return menuPage.stream()
        .map(MenuGetResponseDto::new)
        .collect(Collectors.toList());
  }
}