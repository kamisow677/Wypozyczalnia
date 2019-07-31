package com.example.demo.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.Nullable;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book")
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NonNull
    private String title;

    @Column(name = "author")
    @NonNull
    private String author;

    @Nullable
    @Column(name = "returnDate")
    private LocalDate returnDate;

    @Nullable
    @Column(name = "updatePunishmentDate")
    private LocalDate updatePunishmentDate;

    @Nullable
    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "reader_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Reader reader;
}