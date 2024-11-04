package com.itwillbs.bookjuk.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsCertificationUtil {
    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    //메세지 서비스를 위한 객체
    DefaultMessageService messageService;

    //의존성 주입이 완료된 후 초기화를 수행하는 메서드
    @PostConstruct
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    //단일 메세지 발송
    public void sendSMS(String toPhoneNumber, String code){
        Message message = new Message(); //새 메세지 객체 생성
        message.setFrom(fromNumber); //발신자 번호 설정
        message.setTo(toPhoneNumber); //수신자 번호 설정
        message.setText("본인확인 인증번호 [" + code + "] 입니다."); //메세지 내용 설정
        this.messageService.sendOne(new SingleMessageSendingRequest(message)); //메세지 발송

    }

}
