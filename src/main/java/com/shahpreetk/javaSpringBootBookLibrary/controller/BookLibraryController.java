package com.shahpreetk.javaSpringBootBookLibrary.controller;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.service.BookLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookLibraryController {

    @Autowired
    private BookLibraryService bookLibraryService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Book> getAllBooks(){
        return bookLibraryService.getAllBooks();
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Book getBookById(@PathVariable int id) {
        return bookLibraryService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody Book book) {
        return bookLibraryService.addBook(book);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id, @RequestBody Book updatedBook) {
        Book updated = bookLibraryService.updateBook(id, updatedBook);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int id) {
        bookLibraryService.deleteBook(id);
    }
}
