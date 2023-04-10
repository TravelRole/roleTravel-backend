package com.travel.role.global.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTime {

	@CreatedDate
	@Column(updatable = false, name = "created_at")
	private LocalDateTime createDate;

	@LastModifiedDate
	@Column(name = "modified_at")
	private LocalDateTime modifiedDate;
}
