package com.example.demo.unit;

import com.example.demo.Configs.PunishmentConfig;
import com.example.demo.Entities.Book;
import com.example.demo.Services.PunishmentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.mockito.Mockito.doReturn;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

public class PunishmentTest {

    @Mock
    private Clock clock;

    @Mock
    private PunishmentConfig punishmentConfig;

    private PunishmentService punishmentService;

    private Clock fixedClock;

    private Book bookDummy;

    private void setNewClock(LocalDate localDate){
        fixedClock = Clock.fixed(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        doReturn(fixedClock.instant()).when(clock).instant();
        doReturn(fixedClock.getZone()).when(clock).getZone();
        punishmentService = new PunishmentService(fixedClock,punishmentConfig);
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        bookDummy = new Book("Iliada","Homer");
        bookDummy.setReturnDate(LocalDate.parse("2019-06-30"));
        bookDummy.setUpdatePunishmentDate(LocalDate.parse("2019-06-20"));
        setNewClock(LocalDate.of(2019, 7, 15));

        when(punishmentConfig.getCRITICAL_PUNISHMENT()).thenReturn(18.0);
        when(punishmentConfig.getDAYS_TO_READ()).thenReturn(30);
        when(punishmentConfig.getNORMAL_LENGTH()).thenReturn(7);
        when(punishmentConfig.getINTEREST_PRICE()).thenReturn(0.5);
        when(punishmentConfig.getBASE_PRICE()).thenReturn(2.0);
    }

    @Test
    public void  testOnceCalculatedAfterDiscountTime(){
        setNewClock(LocalDate.of(2019, 7, 15));

        BigDecimal bigDecimal =  punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("6.0")));
    }

    @Test
    public void  testAllThreePathUpdateBefore(){
        setNewClock(LocalDate.of(2019, 7, 2));
        BigDecimal bigDecimal = punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("2")) );

        setNewClock(LocalDate.of(2019, 7, 4));
        bigDecimal =  punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("2")));

        setNewClock(LocalDate.of(2019, 7, 20));
        bigDecimal =  punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("8.5")));
    }

    @Test
    public void  testUpdateAfterReturn(){
        bookDummy.setReturnDate(LocalDate.parse("2019-06-30"));
        bookDummy.setUpdatePunishmentDate(LocalDate.parse("2019-07-03"));

        setNewClock(LocalDate.of(2019, 7, 2));
        BigDecimal bigDecimal =  punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("0")));

        setNewClock(LocalDate.of(2019, 7, 20));
        bigDecimal = punishmentService.calculatePunishmentForBook(bookDummy);
        assertThat(bigDecimal, is(new BigDecimal("6.5")));
    }
}