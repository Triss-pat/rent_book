package com.biblio.loan.controller;

import com.biblio.loan.dto.LoanRequest;
import com.biblio.loan.dto.LoanResponse;
import com.biblio.loan.service.LoanService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService service;

    public LoanController(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoanResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public LoanResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<LoanResponse> create(@Valid @RequestBody LoanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }
}