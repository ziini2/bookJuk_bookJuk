package com.itwillbs.bookjuk.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventConditionParser {

	public static int parseRequiredValue(String conditionString) {
		log.info("=============================" + conditionString);
        String numberString = conditionString.replaceAll("[^0-9]", "");      
        if (numberString.isEmpty()) {
            return 0;
        }
        log.info("=============================" + numberString);
        return Integer.parseInt(numberString);
    }

    public static String parseConditionType(String conditionString) {
    	if (conditionString.equals("신규 가입자")) {
            return "신규 가입";
        } else if (conditionString.equals("로그인한 회원")) {
            return "로그인";
        }    
        if (conditionString.contains("회 이")) {
            return "대여 횟수";
        } else if (conditionString.contains("원 이")) {
            return "대여 금액";
        }       
            throw new IllegalArgumentException("알 수 없는 조건 유형: " + conditionString);     
    }
	
}
