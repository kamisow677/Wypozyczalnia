package com.example.demo.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;
import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@ToString(includeFieldNames=true)
public class BookDTO {
    @NonNull
    @Size(min=1, max=30)
    private String title;

    @NonNull
    @Size(min=2, max=30)
    private String author;

    private LocalDate returnDate;

    private LocalDate updatePunishmentDate;

    @JsonIgnore
    private ReaderDTO readerDTO;
}
