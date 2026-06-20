package com.biblio.book;

import static org.assertj.core.api.Assertions.assertThat;

import com.biblio.book.entity.Book;
import com.biblio.book.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    void savesAndRetrievesBook() {
        Book saved = repository.save(new Book("1984", "Orwell"));
        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    void deletesBook() {
        Book saved = repository.save(new Book("Dune", "Herbert"));
        repository.deleteById(saved.getId());
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    @Test
    void findAllReturnsSaved() {
        repository.save(new Book("A", "X"));
        repository.save(new Book("B", "Y"));
        assertThat(repository.findAll()).hasSize(2);
    }
}