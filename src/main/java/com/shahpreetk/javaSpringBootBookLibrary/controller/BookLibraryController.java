package com.shahpreetk.javaSpringBootBookLibrary.controller;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.service.BookLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookLibraryController {

    @Autowired
    private BookLibraryService bookLibraryService;

    @GetMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Book> getAllBooks(){
        return bookLibraryService.getAllBooks();
    }
}
