package com.sparta.peopleoff.domain.menu.dto;

import com.sparta.peopleoff.domain.menu.entity.MenuEntity;
import com.sparta.peopleoff.domain.menu.entity.enums.MenuStatusEnum;
import java.util.UUID;
import lombok.Getter;

@Getter
public class MenuGetResponseDto {

  private final UUID id;
  private final String menuName;
  private final String menuDescription;
  private final int price;
  private final MenuStatusEnum menuStatus;

  public MenuGetResponseDto(MenuEntity menu) {
    this.id = menu.getId();
    this.menuName = menu.getMenuName();
    this.menuDescription = menu.getMenuDescription();
    this.price = menu.getPrice();
    this.menuStatus = menu.getMenuStatus();
  }
}

