package com.example.eureka_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "wypKsi", configuration = FeignClientAdminConfiguration.class)
@RequestMapping("/books")
public interface WypClient {

    @GetMapping
    ResponseEntity<List<BookDTO>> getBooks();

    @GetMapping( value = "/{id}", consumes = "application/json")
    ResponseEntity<BookDTO> getBook(@PathVariable Long id);

    @PostMapping
    ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDto);
}