package com.biblio.loan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biblio.loan.client.BookClient;
import com.biblio.loan.dto.BookResponse;
import com.biblio.loan.dto.LoanRequest;
import com.biblio.loan.dto.LoanResponse;
import com.biblio.loan.entity.Loan;
import com.biblio.loan.exception.BookUnavailableException;
import com.biblio.loan.exception.LoanNotFoundException;
import com.biblio.loan.repository.LoanRepository;
import java.util.List;
import java.util.Optional;

import com.biblio.loan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository repository;

    @Mock
    private BookClient bookClient;

    @InjectMocks
    private LoanService service;

    private BookResponse book(boolean available) {
        BookResponse b = new BookResponse();
        b.setId(1L);
        b.setTitle("1984");
        b.setAvailable(available);
        return b;
    }

    @Test
    void createSavesLoanWhenBookAvailable() {
        when(bookClient.getBook(1L)).thenReturn(book(true));
        when(repository.save(any(Loan.class))).thenAnswer(i -> {
            Loan l = i.getArgument(0);
            l.setId(10L);
            return l;
        });

        LoanResponse result = service.create(new LoanRequest(1L, "Alice"));

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getBorrower()).isEqualTo("Alice");
        verify(bookClient).getBook(1L);
    }

    @Test
    void createFailsWhenBookUnavailable() {
        when(bookClient.getBook(1L)).thenReturn(book(false));

        assertThatThrownBy(() -> service.create(new LoanRequest(1L, "Alice")))
                .isInstanceOf(BookUnavailableException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void createFailsWhenBookMissing() {
        when(bookClient.getBook(2L)).thenThrow(new BookUnavailableException(2L));

        assertThatThrownBy(() -> service.create(new LoanRequest(2L, "Bob")))
                .isInstanceOf(BookUnavailableException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void findByIdReturnsLoan() {
        Loan loan = new Loan(1L, "Alice");
        loan.setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(loan));

        assertThat(service.findById(7L).getBorrower()).isEqualTo("Alice");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(LoanNotFoundException.class);
    }

    @Test
    void findAllReturnsResponses() {
        Loan loan = new Loan(1L, "Alice");
        loan.setId(1L);
        when(repository.findAll()).thenReturn(List.of(loan));

        assertThat(service.findAll()).hasSize(1);
    }
}