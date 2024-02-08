package com.codedailywithme.springSecurityPractice.book;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor

public class BookController {
    private final BookService service;
    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody BookRequest request
    ){
        service.save(request);
        return ResponseEntity.accepted().build();
    }
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks(){
        return ResponseEntity.ok(service.findAllBooks());
    }



}
