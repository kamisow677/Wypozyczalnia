package com.example.demo.DTOs;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(includeFieldNames=true)
public class ReaderDTO{

    @NotNull
    @Size(min=2, max=30)
    private String firstName;

    @NotNull
    @Size(min=2, max=30)
    private String lastName;

    @NotNull
    private BigDecimal punishment;

    private List<BookDTO> booksDTO;
}
