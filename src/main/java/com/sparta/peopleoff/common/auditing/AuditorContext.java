package com.sparta.peopleoff.common.auditing;

public class AuditorContext {

  private static final ThreadLocal<String> currentEmail = new ThreadLocal<>();

  // 값을 설정하는 메서드
  public static void setCurrentEmail(String email) {
    currentEmail.set(email);
  }

  // 값을 가져오는 메서드
  public static String getCurrentEmail() {
    return currentEmail.get();
  }

  // 값을 초기화하는 메서드
  public static void clear() {
    currentEmail.remove();
  }
}
