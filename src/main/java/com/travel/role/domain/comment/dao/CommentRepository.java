package com.travel.role.domain.comment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	@Query("SELECT c FROM Comment c"
		+ " INNER JOIN FETCH c.user"
		+ " WHERE c.parent.id IS NULL"
		+ " ORDER BY c.createDate DESC")
	List<Comment> findAllFirstDepthComments();

	@Query("SELECT c FROM Comment c"
		+ " INNER JOIN FETCH c.user"
		+ " WHERE c.parent.id = :parentId"
		+ " ORDER BY c.createDate DESC")
	List<Comment> findAllChildCommentsByParentId(@Param("parentId") Long parentId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "DELETE FROM comment WHERE group_id = :groupId AND depth >= :depth ORDER BY depth DESC)", nativeQuery = true)
	void deleteAllByGroupIdAndDepth(@Param("groupId") Long groupId, @Param("depth") Integer depth);
}
