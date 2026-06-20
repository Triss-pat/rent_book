package com.biblio.loan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class LoanRequest {
    @NotNull(message = "Le livre est obligatoire")
    private Long bookId;

    @NotBlank(message = "L'emprunteur est obligatoire")
    private String borrower;

    public LoanRequest() {
    }

    public LoanRequest(Long bookId, String borrower) {
        this.bookId = bookId;
        this.borrower = borrower;
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
