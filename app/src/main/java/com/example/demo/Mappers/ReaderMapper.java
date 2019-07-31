package com.example.demo.Mappers;


import com.example.demo.DTOs.ReaderDTO;
import com.example.demo.Entities.Reader;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" , uses = BookMapper.class)
public interface ReaderMapper extends BaseMapper<Reader,ReaderDTO> {

    @Mapping(target = "booksDTO", source = "reader.books")
    ReaderDTO entityToDto(Reader reader);

    @IterableMapping(qualifiedByName = "entityToDto")
    List<ReaderDTO> entityToDto(List<Reader> reader);

    @Mapping(target = "books", source = "readerDTO.booksDTO")
    Reader dtoToEntity(ReaderDTO readerDTO);


}

