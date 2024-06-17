package com.shahpreetk.javaSpringBootBookLibrary.service;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.BookLibraryRepository;
import com.shahpreetk.javaSpringBootBookLibrary.exceptions.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookLibraryServiceTest {

    @Mock
    private BookLibraryRepository bookLibraryRepository;

    @InjectMocks
    private BookLibraryService bookLibraryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Book createBook(int id, String name, String author, String description) {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setAuthor(author);
        book.setDescription(description);
        return book;
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = createBook(1, "Book1", "Author1", "Description1");
        Book book2 = createBook(2, "Book2", "Author2", "Description2");

        when(bookLibraryRepository.findAll()).thenReturn(java.util.List.of(book1, book2));

        List<Book> result = bookLibraryService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book1", result.getFirst().getName());
        assertEquals("Author1", result.get(0).getAuthor());
        assertEquals("Description1", result.get(0).getDescription());
        assertEquals("Book2", result.get(1).getName());
        assertEquals("Author2", result.get(1).getAuthor());
        assertEquals("Description2", result.get(1).getDescription());
    }

    @Test
    public void testGetBookById_Success() {
        Book book = createBook(1, "Book1", "Author1", "Description1");

        when(bookLibraryRepository.findById(1)).thenReturn(Optional.of(book));

        Book result = bookLibraryService.getBookById(1);

        assertEquals("Book1", result.getName());
        assertEquals("Author1", result.getAuthor());
        assertEquals("Description1", result.getDescription());
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookLibraryRepository.findById(1)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookLibraryService.getBookById(1);
        });

        assertEquals("Could not find book 1", exception.getMessage());
    }

    @Test
    public void testAddBook() {
        Book book = createBook(1, "Book1", "Author1", "Description1");

        when(bookLibraryRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookLibraryService.addBook(book);

        assertEquals("Book1", result.getName());
        assertEquals("Author1", result.getAuthor());
        assertEquals("Description1", result.getDescription());
    }

    @Test
    public void testUpdateBook_Success() {
        Book existingBook = createBook(1, "Original Book", "Original Author", "Original Description");
        Book updatedBook = createBook(1, "Updated Book", "Updated Author", "Updated Description");

        when(bookLibraryRepository.findById(1)).thenReturn(Optional.of(existingBook));
        when(bookLibraryRepository.save(any(Book.class))).thenReturn(updatedBook);

        Book result = bookLibraryService.updateBook(1, updatedBook);

        assertEquals("Updated Book", result.getName());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    public void testUpdateBook_IdMismatch() {
        Book updatedBook = createBook(2, "Updated Book", "Updated Author", "Updated Description");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookLibraryService.updateBook(1, updatedBook);
        });

        assertEquals("The ID in the request body does not match the ID in the URL.", exception.getMessage());
    }

    @Test
    public void testUpdateBook_NotFound() {
        Book updatedBook = createBook(1, "Updated Book", "Updated Author", "Updated Description");

        when(bookLibraryRepository.findById(1)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookLibraryService.updateBook(1, updatedBook);
        });

        assertEquals("Could not find book 1", exception.getMessage());
    }

    @Test
    public void testDeleteBook() {
        Book book = createBook(1, "Book1", "Author1", "Description1");

        bookLibraryService.deleteBook(1);

        verify(bookLibraryRepository, times(1)).deleteById(1);

    }

}
