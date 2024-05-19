package com.shahpreetk.javaSpringBootBookLibrary.service;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.BookLibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookLibraryService {

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    public List<Book> getAllBooks(){
        return bookLibraryRepository.findAll();
    }
}
