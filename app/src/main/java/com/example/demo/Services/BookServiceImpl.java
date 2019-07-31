package com.example.demo.Services;

import com.example.demo.Configs.PunishmentConfig;
import com.example.demo.DTOs.BookDTO;
import com.example.demo.Entities.Book;
import com.example.demo.Exceptions.BookServiceException;
import com.example.demo.Mappers.BookMapper;
import com.example.demo.Repositories.BookRepository;
import com.example.demo.Repositories.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final BookMapper bookMapper;
    private final TrelloService trelloService;
    private final Clock clock;
    private final PunishmentService punishmentService;
    private final PunishmentConfig punishmentConfig;

    public List<BookDTO> getAllBooks(){
        return bookMapper.entityToDto(bookRepository.findAll());
    }

    private Book getBook(Long id){
        return bookRepository.findById(id)
                .orElseThrow(()-> new BookServiceException.BookBadId("BAD ID"));
    }

    public BookDTO getBookById(Long id){
        return bookMapper.entityToDto(getBook(id));
    }

    public BookDTO saveBook(BookDTO bookDTO){
        final Book save = bookRepository.save(bookMapper.dtoToEntity(bookDTO));
        return bookMapper.entityToDto(save);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

    @Transactional
    public BigDecimal updatePunishment(Book b){
        BigDecimal punish = b.getReader().getPunishment().add(punishmentService.calculatePunishmentForBook(b));
        b.getReader().setPunishment(punish);
        b.setUpdatePunishmentDate(LocalDate.now(clock));
        bookRepository.save(b);
        readerRepository.save(b.getReader());
        if (b.getReader().getPunishment().compareTo(new BigDecimal(punishmentConfig.getCRITICAL_PUNISHMENT()))==1){
            trelloService.postIndebtedness(b.getReader(),String.valueOf(punish));
        }
        return  b.getReader().getPunishment();
    }

    public void updatePunishments(){
        List<Book> byReaderIsNotNull = bookRepository.findByReaderIsNotNull();
        byReaderIsNotNull.forEach(this::updatePunishment);
    }

    public BookDTO extendBook(Long id) {
        Book book = getBook(id);
        if (book.getReturnDate().isAfter(LocalDate.now(clock))){
            new BookServiceException.BookCantExtend("YOU CANT EXTEND YOUR BOOK");
        } else  {
            book.setReturnDate(LocalDate.now(clock).plusDays(punishmentConfig.getDAYS_TO_READ()));
        }
        return bookMapper.entityToDto(book);
    }

    @Transactional
    public BookDTO returnBook(Long id){
        Book book = bookRepository.findByIdAndReaderIsNotNull(id)
                .orElseThrow(()-> new BookServiceException.BookAlreadyReturned("ALREADY RETURNED"));

        updatePunishment(book);
        book.setReader(null);
        book.setReturnDate(null);
        book.setUpdatePunishmentDate(null);
        bookRepository.save(book);
        return bookMapper.entityToDto(book);
    }
}
