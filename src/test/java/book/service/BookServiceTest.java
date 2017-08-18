package book.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import book.dao.BookRepository;
import book.dto.Book;
import book.entity.BookEntity;
import book.transformer.BookTransformer;

public class BookServiceTest {

    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookTransformer transformer;

    @Before
    public void setUp() {
        initMocks(this);
        bookService = new BookService(bookRepository, transformer);
    }

    @Test
    public void shouldCreateBook() {

        Long expectedId = 1L;
        Book book = createSampleBook();
        BookEntity bookEntity = createSampleBookEntity();
        BookEntity persistedBook = createPersistedBook();

        when(transformer.transformToBookEntity(book)).thenReturn(bookEntity);
        when(bookRepository.save(bookEntity)).thenReturn(persistedBook);

        Long actualId = bookService.create(book);

        assertEquals(expectedId, actualId);
        verify(transformer, times(1)).transformToBookEntity(book);
        verify(bookRepository, times(1)).save(bookEntity);
    }

    private BookEntity createPersistedBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);
        return bookEntity;
    }

    private BookEntity createSampleBookEntity() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setAuthor("Sienkiewicz");
        bookEntity.setTitle("W pustyni i w puszczy");
        bookEntity.setCategory("Przygodowa");
        bookEntity.setIsbn("978-83-240-2959-4");
        return bookEntity;
    }

    private Book createSampleBook() {
        Book book = new Book();
        book.setAuthor("Sienkiewicz");
        book.setTitle("W pustyni i w puszczy");
        book.setCategory("Przygodowa");
        book.setIsbn("978-83-240-2959-4");
        return book;
    }
}