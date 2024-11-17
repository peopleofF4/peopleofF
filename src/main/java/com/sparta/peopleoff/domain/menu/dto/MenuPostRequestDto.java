package com.sparta.peopleoff.domain.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuPostRequestDto {

  private String menuName;
  private String menuDescription;
  private int price;
}
