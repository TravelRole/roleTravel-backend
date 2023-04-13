package com.travel.role.domain.user.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.travel.role.global.exception.ExceptionMessage;
import com.travel.role.global.util.FormatterUtil;

public class UserProfileModifyReqDTO {

	@NotBlank(message = ExceptionMessage.USERNAME_NOT_EMPTY)
	private String name;

	@Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})\\/(0[0-9]|1[0-2])\\/(0[1-9]|[1-2][0-9]|3[0-1])$", message = ExceptionMessage.BIRTH_PATTERN)
	private String birth;

	public String getName() {
		return name;
	}

	public LocalDate getBirth() {
		return FormatterUtil.stringToLocalDate(this.birth, "yyyy/MM/dd", ExceptionMessage.BIRTH_PATTERN);
	}
}
