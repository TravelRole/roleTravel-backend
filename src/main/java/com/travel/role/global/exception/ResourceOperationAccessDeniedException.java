package com.travel.role.global.exception;

public class ResourceOperationAccessDeniedException extends RuntimeException {

	public ResourceOperationAccessDeniedException(String resource, String operation) {
		super(String.format(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, resource, operation));
	}
}
