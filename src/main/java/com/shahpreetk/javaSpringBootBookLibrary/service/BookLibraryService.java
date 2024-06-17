package com.shahpreetk.javaSpringBootBookLibrary.service;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.BookLibraryRepository;
import com.shahpreetk.javaSpringBootBookLibrary.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookLibraryService {

    @Autowired
    private BookLibraryRepository bookLibraryRepository;

    public List<Book> getAllBooks(){
        return bookLibraryRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookLibraryRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    public Book addBook(Book book) {
        return bookLibraryRepository.save(book);
    }

    public Book updateBook(int id, Book updatedBook) {
        if (updatedBook.getId() != id) {
            throw new RuntimeException("The ID in the request body does not match the ID in the URL.");
        }
        Optional<Book> optionalBook = bookLibraryRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book existingBook = optionalBook.get();
            existingBook.setName(updatedBook.getName());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setDescription(updatedBook.getDescription());
            return bookLibraryRepository.save(existingBook);
        } else {
            throw new BookNotFoundException(id);
        }
    }

    public void deleteBook(int id) {
        bookLibraryRepository.deleteById(id);
    }
}
