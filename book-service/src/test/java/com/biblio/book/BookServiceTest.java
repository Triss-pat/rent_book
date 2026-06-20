package com.biblio.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.biblio.book.dto.BookDto;
import com.biblio.book.entity.Book;
import com.biblio.book.exception.BookNotFoundException;
import com.biblio.book.repository.BookRepository;
import java.util.List;
import java.util.Optional;

import com.biblio.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    private Book sample(Long id) {
        Book b = new Book("1984", "Orwell");
        b.setId(id);
        return b;
    }

    @Test
    void findAllReturnsDtos() {
        when(repository.findAll()).thenReturn(List.of(sample(1L), sample(2L)));
        assertThat(service.findAll()).hasSize(2);
    }

    @Test
    void findByIdReturnsBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(sample(1L)));
        assertThat(service.findById(1L).getTitle()).isEqualTo("1984");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void createSavesBook() {
        when(repository.save(any(Book.class))).thenReturn(sample(5L));
        BookDto result = service.create(new BookDto(null, "1984", "Orwell", true));
        assertThat(result.getId()).isEqualTo(5L);
        verify(repository).save(any(Book.class));
    }

    @Test
    void deleteRemovesBook() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(repository.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> service.delete(5L))
                .isInstanceOf(BookNotFoundException.class);
    }
}