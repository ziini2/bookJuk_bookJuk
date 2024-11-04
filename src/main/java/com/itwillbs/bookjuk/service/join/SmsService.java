package com.itwillbs.bookjuk.service.join;

import com.itwillbs.bookjuk.util.SmsCertificationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsCertificationUtil smsCertificationUtil;


    //실제 SMS 인증 서비스
    public String sendSMS(String phoneNumber){
        String code = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        smsCertificationUtil.sendSMS(phoneNumber, code);
        return code;
    }

    //테스트 SMS 인증 서비스
    public String sendSMSTest(String phoneNumber){
        log.info("sendSMSTest phoneNumber: {}", phoneNumber);
        String code = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        log.warn("code: {}", code);
        return code;
    }
}