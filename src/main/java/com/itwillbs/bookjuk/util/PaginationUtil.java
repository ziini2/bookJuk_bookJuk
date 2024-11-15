package com.itwillbs.bookjuk.util;

import java.util.HashMap;
import java.util.Map;

public class PaginationUtil {
    public static Map<String, Object> getPagination(int currentPage, int totalPages, int pageBlockSize) {
        // 현재 페이지 그룹 계산
        int currentPageGroup = currentPage / pageBlockSize;

        // 시작 페이지 번호 (0부터 시작하므로 +1)
        int startPage = currentPageGroup * pageBlockSize;

        // 마지막 페이지 번호 계산
        int endPage = Math.min(startPage + pageBlockSize - 1, totalPages - 1);

        // 이전/다음 그룹 존재 여부
        boolean hasPreviousGroup = currentPageGroup > 0;
        boolean hasNextGroup = endPage < totalPages - 1;

        // 결과를 Map으로 반환
        Map<String, Object> result = new HashMap<>();
        result.put("startPage", startPage);
        result.put("endPage", endPage);
        result.put("hasPreviousGroup", hasPreviousGroup);
        result.put("hasNextGroup", hasNextGroup);

        return result;
    }
}
