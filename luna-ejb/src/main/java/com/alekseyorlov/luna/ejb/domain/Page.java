package com.alekseyorlov.luna.ejb.domain;

import java.util.List;

public class Page<Entry> {

	private Long totalElements;
	
	private Long totalPages;
	
	private List<Entry> content;

	public Page(List<Entry> content, Long totalElements, Long totalPages) {
		this.content = content;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public List<Entry> getContent() {
		return content;
	}

}
