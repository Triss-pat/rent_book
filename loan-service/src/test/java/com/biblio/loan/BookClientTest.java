package com.biblio.loan;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.biblio.loan.client.BookClient;
import com.biblio.loan.dto.BookResponse;
import com.biblio.loan.exception.BookUnavailableException;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestClient;

class BookClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(WireMockConfiguration.options().dynamicPort())
            .build();

    private BookClient client() {
        return new BookClient(RestClient.builder(), wireMock.baseUrl());
    }

    @Test
    void returnsBookWhenServiceResponds200() {
        wireMock.stubFor(get(urlEqualTo("/api/books/1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":1,\"title\":\"1984\",\"available\":true}")));

        BookResponse book = client().getBook(1L);

        assertThat(book.getTitle()).isEqualTo("1984");
        assertThat(book.isAvailable()).isTrue();
    }

    @Test
    void throwsWhenServiceReturns404() {
        wireMock.stubFor(get(urlEqualTo("/api/books/99"))
                .willReturn(aResponse().withStatus(404)));

        assertThatThrownBy(() -> client().getBook(99L))
                .isInstanceOf(BookUnavailableException.class);
    }

    @Test
    void throwsWhenServiceReturns500() {
        wireMock.stubFor(get(urlEqualTo("/api/books/5"))
                .willReturn(aResponse().withStatus(500)));

        assertThatThrownBy(() -> client().getBook(5L))
                .isInstanceOf(BookUnavailableException.class);
    }
}