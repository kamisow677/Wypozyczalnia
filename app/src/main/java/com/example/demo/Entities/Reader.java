package com.example.demo.Entities;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "reader")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Reader {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstName")
    @NonNull
    private String firstName;


    @Column(name = "lastName")
    @NonNull
    private String lastName;

    @Column(name = "punishment")
    @NonNull
    private BigDecimal punishment;

    @Nullable
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "reader")
    private List<Book> books;

}
