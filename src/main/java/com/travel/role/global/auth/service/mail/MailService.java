package com.travel.role.global.auth.service.mail;

import static com.travel.role.global.exception.ExceptionMessage.*;

import javax.mail.SendFailedException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.travel.role.global.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final JavaMailSender javaMailSender;

	public void sendPasswordMail(String password, String email) throws SendFailedException{
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setText("변경된 비밀번호는 : " + password + " 입니다!", false);
			mimeMessageHelper.setSubject("[역할여행] 변경된 비밀번호입니다.");
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			log.info("메일을 보내는데 실패하였습니다 {}", e.getMessage());
			throw new SendFailedException(MAIL_SEND_FAILD_ERROR);
		}
	}
}
