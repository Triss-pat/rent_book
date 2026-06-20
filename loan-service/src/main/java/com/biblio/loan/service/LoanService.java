package com.biblio.loan.service;

import com.biblio.loan.client.BookClient;
import com.biblio.loan.dto.BookResponse;
import com.biblio.loan.dto.LoanRequest;
import com.biblio.loan.dto.LoanResponse;
import com.biblio.loan.entity.Loan;
import com.biblio.loan.exception.BookUnavailableException;
import com.biblio.loan.exception.LoanNotFoundException;
import com.biblio.loan.repository.LoanRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    private final LoanRepository repository;
    private final BookClient bookClient;

    public LoanService(LoanRepository repository, BookClient bookClient) {
        this.repository = repository;
        this.bookClient = bookClient;
    }

    public LoanResponse create(LoanRequest request) {
        BookResponse book = bookClient.getBook(request.getBookId());
        if (book == null || !book.isAvailable()) {
            throw new BookUnavailableException(request.getBookId());
        }
        Loan saved = repository.save(new Loan(request.getBookId(), request.getBorrower()));
        return toResponse(saved);
    }

    public List<LoanResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public LoanResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new LoanNotFoundException(id));
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(loan.getId(), loan.getBookId(), loan.getBorrower());
    }
}