package com.itwillbs.bookjuk.service.find;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService {
    private final JavaMailSender javaMailSender;

    @Value("${mail.username}")
    private String senderEmail;


    //javaMailAPI 사용해서 메일 만들기
    public MimeMessage CreateMail(String mail, String password){
        log.info("Create mail: {}", mail, password);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            //보내는 사람이 메일
            message.setFrom(senderEmail);
            //비밀번호 찾기한 사용자 받아온 메일을 받는사람에 추가!
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증"); //제목
            String body = "";   //본문내용
            body += "<h3>" + "임시비밀번호 입니다." + "</h3>";
            body += "<h1>" + password + "</h1>";
            body += "<h3>" + "감사합니다!" + "</h3>";
            message.setText(body, "UTF-8", "html"); //body에 추가한 본문내용 저장
        }
        catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
        return message;
    }
    //만든 메일 보내기
    public boolean sendMail(String mail, String password){
        log.info("sendMail()");
        MimeMessage message = CreateMail(mail, password); //javaMailAPI 사용해서 만든 메일을 받아온다
        javaMailSender.send(message);  //보냄처리
        return true;
    }





}
