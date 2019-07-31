package com.example.demo.Services;

import com.example.demo.Configs.PunishmentConfig;
import com.example.demo.Entities.Book;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class PunishmentService {

    @Autowired
    private  Clock clock;

    @Autowired
    private  PunishmentConfig punishmentConfig;

    public  BigDecimal calculatePunishmentForBook(Book book){
        BigDecimal punishmentForBookNew = BigDecimal.ZERO;

        long daysLateFromReturnDate = ChronoUnit.DAYS.between(book.getReturnDate(), LocalDate.now(clock));
        long daysLateFromReturnDatePlus7 = daysLateFromReturnDate - punishmentConfig.getNORMAL_LENGTH();

        if (book.getUpdatePunishmentDate().isBefore(book.getReturnDate()) && LocalDate.now(clock).isAfter(book.getReturnDate())){
            punishmentForBookNew = (daysLateFromReturnDate <  punishmentConfig.getNORMAL_LENGTH() + 1)
                    ? new BigDecimal(punishmentConfig.getBASE_PRICE())
                    : (new BigDecimal(punishmentConfig.getINTEREST_PRICE()))
                            .multiply(new BigDecimal(daysLateFromReturnDate - punishmentConfig.getNORMAL_LENGTH()))
                            .add(new BigDecimal(punishmentConfig.getBASE_PRICE()));

        } else if (book.getUpdatePunishmentDate().isAfter(book.getReturnDate())
                && book.getUpdatePunishmentDate().isBefore(book.getReturnDate().plusDays(punishmentConfig.getNORMAL_LENGTH()))
                && LocalDate.now(clock).isAfter(book.getReturnDate().plusDays(punishmentConfig.getNORMAL_LENGTH()))) {
            punishmentForBookNew = (new BigDecimal(punishmentConfig.getINTEREST_PRICE())).multiply(new BigDecimal(Long.toString(daysLateFromReturnDatePlus7)));

        } else if (book.getUpdatePunishmentDate().isAfter(book.getReturnDate().plusDays(punishmentConfig.getNORMAL_LENGTH()))){
            long days =  ChronoUnit.DAYS.between(book.getUpdatePunishmentDate(), LocalDate.now(clock));
            punishmentForBookNew = new BigDecimal(punishmentConfig.getINTEREST_PRICE()).multiply(new BigDecimal(Long.toString(days)));
        }
        return punishmentForBookNew;
    }
}
