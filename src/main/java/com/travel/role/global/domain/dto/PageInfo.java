package com.travel.role.global.domain.dto;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageInfo {

	private static final int GROUP_SIZE = 3;
	private long currentPage; // 현재 페이지
	private long pageSize; // 페이지당 요소 수
	private int size; // 현재 페이지의 요소 수
	private int maxPossiblePageInGroup; // 현재 페이지에서 그룹 내의 최대한으로 갈 수 있는 페이지
	private boolean hasNextPageGroup; // 다음 페이지 그룹 존재 여부

	public static <T> PageInfo from(Page<T> page) {

		PageInfo pageInfo = new PageInfo();
		int currentPage = page.getNumber();
		int totalPage = page.getTotalPages();

		pageInfo.currentPage = currentPage;
		pageInfo.pageSize = page.getSize();
		pageInfo.size = page.getNumberOfElements();
		pageInfo.maxPossiblePageInGroup = getMaxPossiblePageInGroup(currentPage, totalPage);
		pageInfo.hasNextPageGroup = hasNextPageGroup(currentPage, totalPage);

		return pageInfo;
	}

	private static int getMaxPossiblePageInGroup(int currentPage, int totalPage) {

		// 0 1 2 / 3 4 5 / 6 7 8
		int maxPageInGroup = ((currentPage / GROUP_SIZE) + 1) * GROUP_SIZE - 1;

		return Math.min(maxPageInGroup, totalPage - 1);
	}

	private static boolean hasNextPageGroup(int currentPage, int totalPage) {

		int maxPageInGroup = ((currentPage / GROUP_SIZE) + 1) * GROUP_SIZE - 1;

		return maxPageInGroup < totalPage - 1;
	}
}
