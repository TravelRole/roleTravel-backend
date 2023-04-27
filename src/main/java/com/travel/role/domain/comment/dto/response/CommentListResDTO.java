package com.travel.role.domain.comment.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentListResDTO {

	private List<CommentResDTO> comments;

	public static CommentListResDTO from(List<CommentResDTO> commentResDTOS) {

		return new CommentListResDTO(commentResDTOS);
	}
}
