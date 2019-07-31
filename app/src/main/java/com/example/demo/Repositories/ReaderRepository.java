package com.example.demo.Repositories;


import com.example.demo.Entities.Book;
import com.example.demo.Entities.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Reader findReaderByFirstName(String firstName);

}