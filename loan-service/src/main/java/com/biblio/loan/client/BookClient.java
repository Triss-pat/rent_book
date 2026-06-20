package com.biblio.loan.client;

import com.biblio.loan.dto.BookResponse;
import com.biblio.loan.exception.BookUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class BookClient {

    private final RestClient restClient;

    public BookClient(RestClient.Builder builder,
                      @Value("${book.base-url:http://localhost:8081}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public BookResponse getBook(Long bookId) {
        try {
            return restClient.get()
                    .uri("/api/books/{id}", bookId)
                    .retrieve()
                    .body(BookResponse.class);
        } catch (RestClientResponseException ex) {
            throw new BookUnavailableException(bookId);
        }
    }
}