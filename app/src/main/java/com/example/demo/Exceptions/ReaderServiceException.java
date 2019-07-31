package com.example.demo.Exceptions;

public class ReaderServiceException {
    public static class AlreadyRented extends RuntimeException {
        public AlreadyRented(String info) {
            super(info);
        }
    }

    public static class ReaderBadId extends RuntimeException {
        public ReaderBadId(String info) {
            super(info);
        }
    }

}
