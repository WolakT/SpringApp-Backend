package book.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import book.dto.Book;
import book.dto.BookResponse;
import book.service.BookService;

@RestController
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/book", method = RequestMethod.POST)
    public ResponseEntity<BookResponse> createBook(@RequestBody Book book) {
        Long id = bookService.create(book);
        return new ResponseEntity<>(new BookResponse(String.format("Book with id: %s has been created", id)),
                HttpStatus.CREATED);
    }

    @RequestMapping("/book/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Book book) {
        boolean updated = bookService.update(id, book);
        return updated ? new ResponseEntity<>(new BookResponse("Successfully updated"), HttpStatus.OK)
                : createNonExisting(id);
    }

    @RequestMapping(value = "/book/all", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Book>> getAll() {
        Iterable<Book> allBook = bookService.getAll();
        return new ResponseEntity<>(allBook, HttpStatus.OK);
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.GET)
    public ResponseEntity getBook(@PathVariable("id") Long id) {
        Book book = bookService.get(id);
        return Objects.isNull(book) ? createNonExisting(id) : new ResponseEntity<Book>(book, HttpStatus.OK);
    }

    @RequestMapping(value = "/book/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<BookResponse> delete(@PathVariable("id") Long id) {
        boolean deleted = bookService.delete(id);
        return deleted ? new ResponseEntity<>(new BookResponse("Successfully deleted"), HttpStatus.OK)
                : createNonExisting(id);
    }

    private ResponseEntity<BookResponse> createNonExisting(Long id) {
        return new ResponseEntity<>(new BookResponse(String.format("Id: %s does not exist", id)),
                HttpStatus.BAD_REQUEST);
    }
}
