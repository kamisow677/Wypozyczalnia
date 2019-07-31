package com.example.demo.Mappers;

import java.util.List;

public interface BaseMapper <E,D>{
    D entityToDto(E entity);
    List<D> entityToDto(List<E> entity);
    E dtoToEntity(D dto);
}
