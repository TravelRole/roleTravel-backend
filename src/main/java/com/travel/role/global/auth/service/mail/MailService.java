package com.travel.role.global.auth.service.mail;

import static com.travel.role.global.exception.dto.ExceptionMessage.*;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final AmazonSimpleEmailService amazonSimpleEmailService;

	public void sendPasswordMail(String password, String email) throws SendFailedException {
		Session session=  Session.getDefaultInstance(new Properties());
		MimeMessage mimeMessage = new MimeMessage(session);
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setFrom("no-reply@travel-role.com");
			mimeMessageHelper.setText("변경된 비밀번호는 : " + password + " 입니다!", false);
			mimeMessageHelper.setSubject("[역할여행] 변경된 비밀번호입니다.");

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			mimeMessage.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));
			amazonSimpleEmailService.sendRawEmail(new SendRawEmailRequest(rawMessage));
		} catch (Exception e) {
			log.info("메일을 보내는데 실패하였습니다 {}", e.getMessage());
			throw new SendFailedException(MAIL_SEND_FAILED_ERROR);
		}
	}
}
