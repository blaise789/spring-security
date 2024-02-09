package com.codedailywithme.springSecurityPractice.repository;


import com.codedailywithme.springSecurityPractice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
}