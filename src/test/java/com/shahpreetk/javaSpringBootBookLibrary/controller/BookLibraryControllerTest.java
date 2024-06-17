package com.shahpreetk.javaSpringBootBookLibrary.controller;

import com.shahpreetk.javaSpringBootBookLibrary.Book;
import com.shahpreetk.javaSpringBootBookLibrary.exceptions.BookNotFoundException;
import com.shahpreetk.javaSpringBootBookLibrary.exceptions.CustomExceptionHandler;
import com.shahpreetk.javaSpringBootBookLibrary.service.BookLibraryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookLibraryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookLibraryService bookLibraryService;

    @InjectMocks
    private BookLibraryController bookLibraryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookLibraryController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
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
    public void testGetAllBooks() throws Exception {
        Book book1 = createBook(1, "Book1", "Author1", "Description1");
        Book book2 = createBook(2, "Book2", "Author2", "Description2");
        List<Book> books = List.of(book1, book2);

        when(bookLibraryService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Book1"))
                .andExpect(jsonPath("$[0].author").value("Author1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Book2"))
                .andExpect(jsonPath("$[1].author").value("Author2"))
                .andExpect(jsonPath("$[1].description").value("Description2"));
    }

    @Test
    public void testGetBookById() throws Exception {
        Book book = createBook(1, "Book1", "Author1", "Description1");

        when(bookLibraryService.getBookById(1)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1"))
                .andExpect(jsonPath("$.author").value("Author1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testGetBookNotFoundException() throws Exception {
        when(bookLibraryService.getBookById(anyInt())).thenThrow(new BookNotFoundException(1));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = createBook(1, "Book1", "Author1", "Description1");

        when(bookLibraryService.addBook(any(Book.class))).thenReturn(book);

        String bookJson = "{ \"name\": \"Book1\", \"author\": \"Author1\", \"description\": \"Description1\" }";


        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1"))
                .andExpect(jsonPath("$.author").value("Author1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book updatedBook = createBook(1, "Book1Updated", "Author1", "Description1");

        when(bookLibraryService.updateBook(eq(1), any(Book.class))).thenReturn(updatedBook);

        String updatedBookJson = "{ \"id\": \"1\", \"name\": \"Book1Updated\", \"author\": \"Author1\", \"description\": \"Description1\" }";

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Book1Updated"))
                .andExpect(jsonPath("$.author").value("Author1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }

    @Test
    public void testUpdateBook_IdMismatch() throws Exception {
        when(bookLibraryService.updateBook(eq(1), any(Book.class))).thenThrow(new RuntimeException("The ID in the request body does not match the ID in the URL."));

        String updatedBookJson = "{ \"id\": 2, \"name\": \"Book1Updated\", \"author\": \"Author1\", \"description\": \"Description1\" }";

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("The ID in the request body does not match the ID in the URL."));
    }

    @Test
    public void testUpdateBook_NotFound() throws Exception {

        when(bookLibraryService.updateBook(eq(1), any(Book.class))).thenThrow(new BookNotFoundException(1));

        String updatedBookJson = "{ \"id\": 1, \"name\": \"Book1Updated\", \"author\": \"Author1\", \"description\": \"Description1\" }";

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Could not find book 1"));
    }
    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

}
