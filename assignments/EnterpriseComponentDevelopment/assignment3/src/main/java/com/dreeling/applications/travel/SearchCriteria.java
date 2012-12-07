/*
 * 
 */
package com.dreeling.applications.travel;

import java.io.Serializable;

/**
 * A backing bean for the main hotel search form. Encapsulates the criteria
 * needed to perform a hotel search.
 */
public class SearchCriteria implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The user-provided search criteria for finding Hotels.
	 */
	private String searchString;

	/** The maximum page size of the Hotel result list. */
	private int pageSize;

	/**
	 * The current page of the Hotel result list.
	 */
	private int page;

	/**
	 * Gets the search string.
	 * 
	 * @return the search string
	 */
	public String getSearchString() {
		return searchString;
	}

	/**
	 * Sets the search string.
	 * 
	 * @param searchString
	 *            the new search string
	 */
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the new page size
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Gets the page.
	 * 
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 * 
	 * @param page
	 *            the new page
	 */
	public void setPage(int page) {
		this.page = page;
	}
}
