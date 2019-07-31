package com.example.demo.Services;


import com.example.demo.DTOs.BookDTO;
import com.example.demo.Entities.Book;
import com.example.demo.Exceptions.BookServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookDTO> getAllBooks();

    BookDTO saveBook(BookDTO bookDTO);

    void deleteBook(Long id);

    BookDTO getBookById(Long id) ;

    BookDTO returnBook(Long id) ;

    public void updatePunishments();

    BookDTO extendBook(Long id);
}
