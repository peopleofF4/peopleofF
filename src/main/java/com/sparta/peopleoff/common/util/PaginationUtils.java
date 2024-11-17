package com.sparta.peopleoff.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

  // 객체 생성 제한
  private PaginationUtils() {
    throw new UnsupportedOperationException("Utility class");
  }

  public static Pageable createPageable(String sortBy, String sortDirection, int pageSize,
      int page) {
    int validatedPage = Math.max(page - 1, 0);
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

    int validatedPageSize = validatePageSize(pageSize);

    return PageRequest.of(page, validatedPageSize, Sort.by(direction, sortBy));
  }

  private static int validatePageSize(int pageSize) {
    // 기본 페이지 사이즈 설정 (10, 30, 50 중 하나, 그 외 10)
    return (pageSize == 10 || pageSize == 30 || pageSize == 50) ? pageSize : 10;
  }
}
