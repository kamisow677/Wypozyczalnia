package com.example.demo.Controllers;

import com.example.demo.DTOs.BookDTO;
import com.example.demo.Exceptions.BookServiceException;
import com.example.demo.Services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDTO>> read(){
        List<BookDTO> allBooks = bookService.getAllBooks();
        log.info(allBooks.toString());
        return ResponseEntity.ok(allBooks);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<BookDTO> readById(@PathVariable Long id) {
            return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookDTO bookDto) {
        return ResponseEntity.ok(bookService.saveBook(bookDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/returnBook/{id}")
    public ResponseEntity returnBook(@PathVariable Long id) throws BookServiceException.BookAlreadyReturned {
            return ResponseEntity.ok(bookService.returnBook(id));
    }

    @PutMapping(value = "/extension/{id}")
    public ResponseEntity extension(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.extendBook(id));
    }
}
