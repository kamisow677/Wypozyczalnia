package com.example.demo.Services;

import com.example.demo.DTOs.BookDTO;
import com.example.demo.DTOs.ReaderDTO;

import java.util.List;


public interface ReaderService {

    List<ReaderDTO> getAllReaders();

    ReaderDTO saveReader(ReaderDTO readerDTO);

    void deleteReader(Long id);

    ReaderDTO getReaderById(Long id);

    BookDTO rentBook(Long readerId, Long bookId);

}
