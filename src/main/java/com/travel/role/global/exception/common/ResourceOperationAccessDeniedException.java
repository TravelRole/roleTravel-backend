package com.travel.role.global.exception.common;

import com.travel.role.global.exception.ExceptionMessage;

public class ResourceOperationAccessDeniedException extends RuntimeException {

	public ResourceOperationAccessDeniedException(Resource resource, Operation operation) {
		super(String.format(ExceptionMessage.RESOURCE_OPERATION_ACCESS_DENIED, resource.getResourceName(),
			operation.getOperationName()));
	}

	public enum Resource {
		COMMENT("댓글");

		private final String resourceName;

		Resource(String resourceName) {
			this.resourceName = resourceName;
		}

		public String getResourceName() {
			return resourceName;
		}
	}

	public enum Operation {
		MODIFY("수정"), DELETE("삭제");

		private final String operationName;

		Operation(String operationName) {
			this.operationName = operationName;
		}

		public String getOperationName() {
			return operationName;
		}
	}
}
