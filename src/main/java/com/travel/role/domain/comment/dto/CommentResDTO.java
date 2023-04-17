package com.travel.role.domain.comment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.travel.role.domain.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResDTO {

	private Long commentId;

	private String content;

	private String username;

	@JsonFormat(pattern = "yyyy/MM/dd hh:mm:ss")
	private LocalDateTime createdDate;

	public static CommentResDTO fromComment(Comment comment) {

		return new CommentResDTO(comment.getId(), comment.getContent(), comment.getUser().getName(), comment.getCreateDate());
	}
}
