package com.ensao.gi4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {

	private List<T> content;
	private int currentPage;
	private int totalPages;
	private long totalElements;
	private int pageSize;

	public static <T> PageResponse<T> fromPage(Page<T> page) {
		PageResponse<T> response = new PageResponse<>();
		response.setContent(page.getContent());
		response.setCurrentPage(page.getNumber());
		response.setTotalPages(page.getTotalPages());
		response.setTotalElements(page.getTotalElements());
		response.setPageSize(page.getSize());
		return response;
	}
}
