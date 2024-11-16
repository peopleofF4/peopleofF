package com.sparta.peopleoff.domain.menu.dto;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatus;
import lombok.Getter;

@Getter
public class MenuGetResponseDto {

  private final String menuName;
  private final String menuDescription;
  private final int price;
  private final MenuStatus menuStatus;

  public MenuGetResponseDto(MenuEntity menu) {
    this.menuName = menu.getMenuName();
    this.menuDescription = menu.getMenuDescription();
    this.price = menu.getPrice();
    this.menuStatus = menu.getMenuStatus();
  }
}

