package com.travel.role.domain.comment.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.comment.dto.CommentListResDTO;
import com.travel.role.domain.comment.dto.CommentReqDTO;
import com.travel.role.domain.comment.service.CommentService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/room/{room_id}/comments")
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;

	@PostMapping({"", "/{parent_id}"})
	@ResponseStatus(HttpStatus.CREATED)
	public void createComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@PathVariable(value = "parent_id", required = false) Long parentId,
		@RequestBody @Valid CommentReqDTO reqDTO) {

		commentService.createComment(userPrincipal.getEmail(), roomId, parentId, reqDTO);
	}

	@GetMapping({"", "/{parent_id}"})
	public ResponseEntity<CommentListResDTO> getComments(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@PathVariable(value = "parent_id", required = false) Long parentId) {

		CommentListResDTO comments = commentService.getComments(userPrincipal.getEmail(), roomId, parentId);

		return ResponseEntity.ok(comments);
	}

	@PutMapping("/{comment_id}")
	public void modifyComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@PathVariable("comment_id") Long commentId,
		@RequestBody @Valid CommentReqDTO reqDTO) {

		commentService.modifyComment(userPrincipal.getEmail(), roomId, commentId, reqDTO);
	}

	@DeleteMapping("/{comment_id}")
	public void deleteComment(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("room_id") Long roomId,
		@PathVariable("comment_id") Long commentId) {

		commentService.deleteComment(userPrincipal.getEmail(), roomId, commentId);
	}
}
