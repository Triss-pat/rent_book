package com.biblio.loan;

import static org.assertj.core.api.Assertions.assertThat;

import com.biblio.loan.entity.Loan;
import com.biblio.loan.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Test
    void savesAndRetrievesLoan() {
        Loan saved = repository.save(new Loan(1L, "Alice"));
        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void findAllReturnsSaved() {
        repository.save(new Loan(1L, "Alice"));
        repository.save(new Loan(2L, "Bob"));
        assertThat(repository.findAll()).hasSize(2);
    }
}