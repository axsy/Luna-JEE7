package com.alekseyorlov.luna.ejb.domain;

public class Pageable {

	private Integer pageNumber;

	public Pageable(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}
	
}
