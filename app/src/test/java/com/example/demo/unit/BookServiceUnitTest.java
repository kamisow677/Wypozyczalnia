package com.example.demo.unit;

import com.example.demo.Configs.PunishmentConfig;
import com.example.demo.DTOs.BookDTO;
import com.example.demo.Entities.Book;
import com.example.demo.Entities.Reader;
import com.example.demo.Exceptions.BookServiceException;
import com.example.demo.Mappers.BookMapper;
import com.example.demo.Mappers.BookMapperImpl;
import com.example.demo.Repositories.BookRepository;
import com.example.demo.Repositories.ReaderRepository;
import com.example.demo.Services.BookServiceImpl;
import com.example.demo.Services.PunishmentService;
import com.example.demo.Services.TrelloService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

public class BookServiceUnitTest {

    @Mock
    private ReaderRepository readerRepository;

    @Mock
    private BookRepository bookRepository;

    private BookMapper bookMapper;

    @Mock
    private PunishmentConfig punishmentConfig;

    @Mock
    private TrelloService trelloService;

    @Mock
    private Clock clock;

    @Mock
    private PunishmentService punishmentService;

    private BookServiceImpl bookService;

    @Mock
    ObjectMapper objectMapper;

    private Book bookDummy;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Reader reader = new Reader("Karolo","Krawczyk",new BigDecimal("2.0"));
        bookDummy = new Book("Iliada","Homer");
        bookDummy.setReturnDate(LocalDate.parse("2019-06-30"));
        bookDummy.setUpdatePunishmentDate(LocalDate.parse("2019-06-20"));
        bookDummy.setReader(reader);
        Clock fixedClock = Clock.fixed(LocalDate.of(2019, 7, 15).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        bookMapper = new BookMapperImpl();
        when(punishmentConfig.getCRITICAL_PUNISHMENT()).thenReturn(18.0);
        when(punishmentConfig.getDAYS_TO_READ()).thenReturn(30);
        when(punishmentConfig.getNORMAL_LENGTH()).thenReturn(7);
        when(punishmentConfig.getINTEREST_PRICE()).thenReturn(0.5);
        when(punishmentConfig.getBASE_PRICE()).thenReturn(2.0);
        bookService = new BookServiceImpl(bookRepository,readerRepository,bookMapper,trelloService,fixedClock,punishmentService,punishmentConfig);
    }

    @Test
    public void  returnBook() throws BookServiceException.BookAlreadyReturned {
        when(bookRepository.findByIdAndReaderIsNotNull(any())).thenReturn(java.util.Optional.ofNullable(bookDummy));
        when(punishmentService.calculatePunishmentForBook(any())).thenReturn(new BigDecimal("0.0"));

        BookDTO bookDTO = bookService.returnBook(anyLong());

        assertEquals(bookDTO.getReaderDTO(), null);
        assertEquals(bookDTO.getUpdatePunishmentDate(), null);
        assertEquals(bookDTO.getUpdatePunishmentDate(), null);
    }

    @Test(expected = ResponseStatusException.class)
    public void  returnBookTestResourceNotFound() throws BookServiceException.BookAlreadyReturned {
        when(bookRepository.findByIdAndReaderIsNotNull(any())).thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "RESOURCE NOT FOUND"));

        bookService.returnBook(anyLong());
    }
}
