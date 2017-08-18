package book.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import book.dto.Book;
import book.dto.BookResponse;
import book.service.BookService;

public class BookControllerTest {

    private BookController bookController;

    @Mock
    private BookService bookService;

    @Before
    public void setUp() {
        initMocks(this);
        bookController = new BookController(bookService);
    }

    @Test
    public void shouldCreateBook() {

        BookResponse expectedId = new BookResponse("Book with id: 1 has been created");
        Book input = new Book();

        when(bookService.create(input)).thenReturn(1L);

        ResponseEntity<BookResponse> responseEntity = bookController.createBook(input);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedId, responseEntity.getBody());
    }

    @Test
    public void shouldDeleteReturnSuccess() {

        when(bookService.delete(1L)).thenReturn(true);
        BookResponse expected = new BookResponse("Successfully deleted");

        ResponseEntity<BookResponse> responseEntity = bookController.delete(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());

        verify(bookService, times(1)).delete(1L);

    }

    @Test
    public void shouldDeleteReturnBadRequest() {

        when(bookService.delete(1L)).thenReturn(false);
        BookResponse expected = new BookResponse("Id: 1 does not exist");

        ResponseEntity<BookResponse> responseEntity = bookController.delete(1L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());

        verify(bookService, times(1)).delete(1L);
    }

    @Test
    public void shouldUpdateReturnSuccess() {

        BookResponse expected = new BookResponse("Successfully updated");
        Book book = createDefaultBook();

        when(bookService.update(1L, book)).thenReturn(true);

        ResponseEntity responseEntity = bookController.update(1L, book);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());

        verify(bookService, times(1)).update(1L, book);
    }



    @Test
    public void shouldUpdateBadRequest() {

        BookResponse expected = new BookResponse(String.format("Id: %s does not exist", 1));
        Book book = createDefaultBook();

        when(bookService.update(1L, book)).thenReturn(false);

        ResponseEntity responseEntity = bookController.update(1L, book);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());

        verify(bookService, times(1)).update(1L, book);
    }

    private Book createDefaultBook() {
        return createBook("Pan Tadeusz", "Adam Mickiewicz", "Poemat", "978-1-4637-225-00");
    }

    private Book createBook(String title, String author, String category, String isbn) {
        Book book = new Book();
        book.setAuthor(author);
        book.setTitle(title);
        book.setCategory(category);
        book.setIsbn(isbn);
        return book;
    }
}