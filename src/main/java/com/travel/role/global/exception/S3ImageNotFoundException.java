package com.travel.role.global.exception;

public class S3ImageNotFoundException extends RuntimeException {

	public S3ImageNotFoundException(String imageProperty) {
		super(String.format(ExceptionMessage.IMAGE_NOT_FOUND_IN_S3, imageProperty));
	}
}
