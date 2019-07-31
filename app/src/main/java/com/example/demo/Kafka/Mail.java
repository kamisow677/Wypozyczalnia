package com.example.demo.Kafka;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Data
@ToString(includeFieldNames=true)
public class Mail {
    @NonNull
    @Pattern(regexp = "^[0-9a-zA-Z]+\\.[0-9a-zA-Z]+@gmail\\.com")
    private String mail;

    @NonNull
    private String subject;

    @NonNull
    private String text;
}
