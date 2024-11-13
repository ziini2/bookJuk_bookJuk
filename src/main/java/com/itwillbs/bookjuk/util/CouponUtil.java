package com.itwillbs.bookjuk.util;

import java.util.Random;

public class CouponUtil {

	// 쿠폰 번호 랜덤으로 설정
	public static String generateRandomCouponNum(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder couponNum = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            couponNum.append(characters.charAt(index));
        }
        return couponNum.toString();
    }
	
}
