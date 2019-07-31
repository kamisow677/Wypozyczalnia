package com.example.demo.Controllers;

import com.example.demo.DTOs.BookDTO;
import com.example.demo.DTOs.ReaderDTO;
import com.example.demo.Services.ReaderService;
import com.example.demo.Services.TrelloService;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.node.TextNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderService readerService;
    private final TrelloService trelloService;

    @PutMapping(value = "/{readerId}/books/{bookId}")
    public ResponseEntity<BookDTO> rentBook(@PathVariable Long readerId, @PathVariable Long bookId){
        return ResponseEntity.ok(readerService.rentBook(readerId, bookId));
    }

    @GetMapping
    public ResponseEntity<List<ReaderDTO>> read(){
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReaderDTO> readById(@PathVariable Long id){
        return ResponseEntity.ok(readerService.getReaderById(id));
    }

    /*
    This endpoint enables reader to add suggestion for library as his card on Trello
     */
    @GetMapping(value = "/{readerId}/suggestions")
    public ResponseEntity trelloSuggest(@PathVariable Long readerId, @RequestBody TextNode suggestion){
        trelloService.postSuggestion(readerId, suggestion.asText());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ReaderDTO> create(@Valid @RequestBody ReaderDTO readerDTO) {
        return ResponseEntity.ok(readerService.saveReader(readerDTO));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }



}
