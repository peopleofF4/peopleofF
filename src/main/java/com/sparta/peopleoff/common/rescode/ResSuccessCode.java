package com.sparta.peopleoff.common.rescode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResSuccessCode implements ResCodeIfs {

  // USER
  SIGNUP_SUCCESS(HttpStatus.OK.value(), "200", "signup Success"),
  LOGIN_SUCCESS(HttpStatus.OK.value(), "200", "login Success"),
  USER_DELETED(HttpStatus.OK.value(), "200", "user successfully deleted"),
  USER_UPDATED(HttpStatus.OK.value(), "200", "user successfully updated"),
  PASSWORD_UPDATED(HttpStatus.OK.value(), "200", "password successfully updated"),
  MANAGER_APPLY(HttpStatus.OK.value(), "200", "manager apply success"),

  // TOKEN
  ACCESS_TOKEN_GENERATED(HttpStatus.CREATED.value(), "201", "AccessToken is generated"),

  // CATEGORY
  CATEGORY_CREATED(HttpStatus.OK.value(), "200", "Category Created"),
  CATEGORY_UPDATED(HttpStatus.OK.value(), "200", "Category Updated"),
  CATEGORY_DELETED(HttpStatus.OK.value(), "200", "Category Deleted"),

  // ADMIN
  USER_ROLE_UPDATED(HttpStatus.OK.value(), "200", "user role successfully updated"),
  STORE_REGISTRATION_UPDTAED(HttpStatus.OK.value(), "200", "store registration updated"),
  STORE_DELETION_UPDTAED(HttpStatus.OK.value(), "200", "store deletion updated"),
  MANAGER_APPROVE(HttpStatus.OK.value(), "200", "Manager Approve Success"),

  // ORDER
  CREATE_ORDER(HttpStatus.CREATED.value(), "201", "Order successfully created"),
  UPDATE_ORDER(HttpStatus.OK.value(), "200", "Order successfully updated"),
  DELETE_ORDER(HttpStatus.OK.value(), "200", "Order successfully deleted");;

  private final Integer httpStatusCode;   // 아래와 상응하는 HttpStatusCode

  private final String resCode;    // Internal ErrorCode

  private final String description;
}
