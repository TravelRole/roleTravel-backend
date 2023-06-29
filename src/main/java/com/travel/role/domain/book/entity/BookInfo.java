package com.travel.role.domain.book.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "book_info")
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfo {
	@Id
	@Column(name = "book_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "is_booked", nullable = false, columnDefinition = "TINYINT(1)")
	@ColumnDefault(value = "0")
	private Boolean isBooked;

	@Column(name = "book_etc", length = 100)
	private String bookEtc;

	public void updateEtc(String etc) {
		this.bookEtc = etc;
	}

	public void updateIsBooked(Boolean isBooked) {
		this.isBooked = isBooked;
	}
}
