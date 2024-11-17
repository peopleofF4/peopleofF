package com.sparta.peopleoff.domain.menu.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuPutRequestDto {

  private String menuName;
  private String menuDescription;
  private int price;
}