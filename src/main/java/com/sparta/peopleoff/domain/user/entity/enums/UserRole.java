package com.sparta.peopleoff.domain.user.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {

  CUSTOMER(Authority.CUSTOMER),  // 사용자 권한
  OWNER(Authority.OWNER),     // 가맹점 사장 권한
  MANAGER(Authority.MANAGER), // 관리자 권한
  MASTER(Authority.MASTER);  // 관리자 권한

  private final String authority;

  UserRole(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

  public static class Authority {

    public static final String CUSTOMER = "ROLE_CUSTOMER";
    public static final String OWNER = "ROLE_OWNER";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String MASTER = "ROLE_MASTER";
  }

}
