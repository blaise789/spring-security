package com.codedailywithme.springSecurityPractice.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private  final BookRepository bookRepository;
    public void save(BookRequest request) {
    var book=Book.builder()
            .id(request.getId())
            .author(request.getAuthor())
            .isbn(request.getIsbn())
            .build();

    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }
}
