package com.travel.role.global.domain.dto;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageInfo {

	private long currentPage; // 현재 페이지
	private long pageSize; // 페이지당 요소 수
	private int size; // 현재 페이지의 요소 수
	private long totalSize; // 전체 요소 갯수

	public static <T> PageInfo from(Page<T> page) {

		PageInfo pageInfo = new PageInfo();

		pageInfo.currentPage = page.getNumber();
		pageInfo.pageSize = page.getSize();
		pageInfo.size = page.getNumberOfElements();
		pageInfo.totalSize = page.getTotalElements();

		return pageInfo;
	}


}
