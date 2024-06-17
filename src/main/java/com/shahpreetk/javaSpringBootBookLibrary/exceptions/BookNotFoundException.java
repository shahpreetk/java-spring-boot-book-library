package com.shahpreetk.javaSpringBootBookLibrary.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(int id) {
        super("Could not find book " + id);
    }
}
