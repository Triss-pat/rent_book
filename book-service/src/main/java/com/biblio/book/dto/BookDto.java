package com.biblio.book.dto;

import jakarta.validation.constraints.NotBlank;

public class BookDto {

    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "L'auteur est obligatoire")
    private String author;

    private boolean available;

    public BookDto() {
    }

    public BookDto(Long id, String title, String author, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}