package com.biblio.book.service;

import com.biblio.book.dto.BookDto;
import com.biblio.book.entity.Book;
import com.biblio.book.exception.BookNotFoundException;
import com.biblio.book.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public BookDto findById(Long id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        return toDto(book);
    }

    public BookDto create(BookDto dto) {
        Book book = new Book(dto.getTitle(), dto.getAuthor());
        return toDto(repository.save(book));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getTitle(),
                book.getAuthor(), book.isAvailable());
    }
}