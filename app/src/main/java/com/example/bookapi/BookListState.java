package com.example.bookapi;

import com.example.bookapi.model.Book;

import java.util.List;

public class BookListState {
    public static final class Loading extends BookListState {}
    public static final class Success extends BookListState {
        private final List<Book> books;
        public Success(List<Book> books) {
            this.books = books;
        }
        public List<Book> getBooks() {
            return books;
    }
    }
    public static final class Error extends BookListState {
        private final Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }
}
