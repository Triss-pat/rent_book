package com.biblio.loan.exception;

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException(Long bookId) {
        super("Livre indisponible : " + bookId);
    }
}
