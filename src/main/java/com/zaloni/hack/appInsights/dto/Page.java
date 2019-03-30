package com.zaloni.hack.appInsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Final class to encapsulate page related information. Ideal usage of the class is using
 * composition design pattern instead of inheritance
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Page {
	private int chunkSize;
	private int currentPage;
	private String sortBy;
	private SortOrder sortOrder;
	private long totalRecords;


	public Page() {

     }

     public Page(int chunkSize, int currentPage, String sortBy, SortOrder sortOrder, long totalRecords) {
          this.chunkSize = chunkSize;
          this.currentPage = currentPage;
          this.sortBy = sortBy;
          this.sortOrder = sortOrder;
          this.totalRecords = totalRecords;
     }

     /**
	 * get the page size of the query
	 * @return page size
	 */
	public int getChunkSize() {
		return chunkSize;
	}

	/**
	 * set the page size of the query
	 * @param chunkSize page size
	 */
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 * get the current page number
	 * @return current page number
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * set the current page number
	 * @param currentPage current page number
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	/**
	 * get the sorting property
	 * @return sort property
	 */
	public String getSortBy() {
		return sortBy;
	}

	/**
	 * set the sort property
	 * @param sortBy sort property
	 */
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	/**
	 * get the ordering property
	 * @return ordering property
	 */
	public SortOrder getSortOrder() {
		return sortOrder;
	}

	/**
	 * set the ordering property
	 * @param orderBy ordering property
	 */
	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * get total records in the search type
	 * @return total records present
	 */
	public long getTotalRecords() {
		return totalRecords;
	}

	/**
	 * set total records of the search type
	 * @param totalRecords total records present
	 */
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [chunkSize=").append(chunkSize).append(", currentPage=").append(currentPage)
				.append(", sortBy=").append(sortBy).append(", sortOrder=").append(sortOrder).append(", totalRecords=")
				.append(totalRecords).append("]");
		return builder.toString();
	}
}
