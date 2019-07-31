package com.example.demo.Exceptions;
import org.springframework.stereotype.Component;

public class BookServiceException extends RuntimeException {

    public static class BookAlreadyReturned extends RuntimeException {
        public BookAlreadyReturned(String info) {
            super(info);
        }
    }

    public static class BookCantExtend extends RuntimeException {
        public BookCantExtend(String info) {
            super(info);
        }
    }

    public static class BookBadId extends RuntimeException {
        public BookBadId(String info) {
            super(info);
        }
    }

}