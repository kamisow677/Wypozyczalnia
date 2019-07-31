package com.example.demo.Services;

import com.example.demo.Configs.PunishmentConfig;
import com.example.demo.DTOs.BookDTO;
import com.example.demo.DTOs.ReaderDTO;
import com.example.demo.Entities.Book;
import com.example.demo.Entities.Reader;
import com.example.demo.Exceptions.BookServiceException;
import com.example.demo.Exceptions.ReaderServiceException;
import com.example.demo.Mappers.BookMapper;
import com.example.demo.Mappers.ReaderMapper;
import com.example.demo.Mappers.ReaderMapperImpl;
import com.example.demo.Repositories.BookRepository;
import com.example.demo.Repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderServiceImpl implements ReaderService{

    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final ReaderMapper readerMapper;
    private final BookMapper bookMapper;
    private final TrelloService trelloService;
    private final PunishmentConfig punishmentConfig;

    public List<ReaderDTO> getAllReaders(){
        return readerMapper.entityToDto(readerRepository.findAll());
    }

    public ReaderDTO saveReader(ReaderDTO readerDTO){
        Reader reader = readerMapper.dtoToEntity(readerDTO);
        Reader saved = readerRepository.save(reader);
        trelloService.refreshUserList();
        return readerMapper.entityToDto(saved);
    }

    public void deleteReader(Long id){
        readerRepository.deleteById(id);
    }

    private Reader getReader(Long id){
        return readerRepository.findById(id)
                .orElseThrow(()->new ReaderServiceException.ReaderBadId("BAD ID"));
    }

    private Book getBook(Long id){
        return  bookRepository.findById(id)
                .orElseThrow(()->new BookServiceException.BookBadId("BAD ID"));
    }

    public ReaderDTO getReaderById(Long id)  {
        return readerMapper.entityToDto(getReader(id));
    }

    @Transactional
    public BookDTO rentBook(Long readerId, Long bookId)  {
        Reader reader = getReader(readerId);
        Book book = getBook(bookId);

        if (book.getReader() != null) {
            throw new ReaderServiceException.AlreadyRented("THIS BOOK IS ALREADY RENTED");
        }

        book.setReader(reader);
        book.setReturnDate(LocalDate.now().plusDays(punishmentConfig.getDAYS_TO_READ()));
        book.setUpdatePunishmentDate(LocalDate.now());
        bookRepository.save(book);
        return bookMapper.entityToDto(book);
    }
}