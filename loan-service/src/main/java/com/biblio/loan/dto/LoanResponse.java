package com.biblio.loan.dto;

public class LoanResponse {
    private Long id;
    private Long bookId;
    private String borrower;

    public LoanResponse() {
    }

    public LoanResponse(Long id, Long bookId, String borrower) {
        this.id = id;
        this.bookId = bookId;
        this.borrower = borrower;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}
