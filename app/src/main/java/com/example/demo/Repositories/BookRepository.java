package com.example.demo.Repositories;

import com.example.demo.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByIdAndReaderIsNotNull(Long id);

    List<Book> findByReaderIsNotNull();

}
