package com.itwillbs.bookjuk.domain.rent;

public class SearchResultDTO {
    private Long bookNum;
    private String bookName;
    private String storeName;

    public SearchResultDTO(Long bookNum, String bookName, String storeName) {
        this.bookNum = bookNum;
        this.bookName = bookName;
        this.storeName = storeName;
    }

	public Long getBookNum() {
		return bookNum;
	}

	public void setBookNum(Long bookNum) {
		this.bookNum = bookNum;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
    
    
}
