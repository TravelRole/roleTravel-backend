package com.travel.role.domain.comment.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.travel.role.global.domain.dto.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResDTO<T> {

	private PageInfo pageInfo;
	private List<T> contents;

	public static <T> PageResDTO<T> from(Page<T> page) {

		return new PageResDTO<>(PageInfo.from(page), page.getContent());
	}
}
