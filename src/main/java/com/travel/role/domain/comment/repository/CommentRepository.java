package com.travel.role.domain.comment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.travel.role.domain.comment.entity.Comment;
import com.travel.role.domain.comment.repository.querydsl.CommentQuerydsl;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentQuerydsl {

	@Query("SELECT c FROM Comment c join fetch c.fromUser WHERE c.id = :commentId ")
	Optional<Comment> findByIdWithFromUser(@Param("commentId") Long commentId);
}
