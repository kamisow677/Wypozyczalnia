package com.example.demo.Mappers;


import com.example.demo.DTOs.BookDTO;
import com.example.demo.Entities.Book;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper extends  BaseMapper<Book,BookDTO> {

    @Mapping(target = "readerDTO", source = "book.reader")
    BookDTO entityToDto(Book book);

    @IterableMapping(qualifiedByName = "entityToDto")
    List<BookDTO> entityToDto(List<Book> book);

    @Mapping(target = "reader", source = "bookDTO.readerDTO")
    Book dtoToEntity(BookDTO bookDTO);
}
