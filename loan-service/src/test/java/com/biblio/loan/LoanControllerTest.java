package com.biblio.loan;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.biblio.loan.controller.LoanController;
import com.biblio.loan.dto.LoanRequest;
import com.biblio.loan.dto.LoanResponse;
import com.biblio.loan.exception.BookUnavailableException;
import com.biblio.loan.service.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoanService service;

    @Test
    void getAllReturnsList() throws Exception {
        when(service.findAll()).thenReturn(List.of(
                new LoanResponse(1L, 1L, "Alice")));
        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].borrower").value("Alice"));
    }

    @Test
    void createReturns201() throws Exception {
        when(service.create(any(LoanRequest.class)))
                .thenReturn(new LoanResponse(5L, 1L, "Alice"));
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoanRequest(1L, "Alice"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void createReturns400WhenInvalid() throws Exception {
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoanRequest(null, ""))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReturns422WhenBookUnavailable() throws Exception {
        when(service.create(any(LoanRequest.class)))
                .thenThrow(new BookUnavailableException(1L));
        mockMvc.perform(post("/api/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoanRequest(1L, "Alice"))))
                .andExpect(status().isUnprocessableEntity());
    }
}