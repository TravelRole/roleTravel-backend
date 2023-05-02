package com.travel.role.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.user.dto.SimpleUserInfoResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentResDTO {

	private Long commentId;

	private String content;

	private SimpleUserInfoResDTO fromUserInfo;

	private String toUsername;

	@JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private LocalDateTime createdDate;

	private boolean deleted;
}
