package book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import book.dao.BookRepository;
import book.dto.Book;
import book.entity.BookEntity;
import book.transformer.BookTransformer;

@Service
public class BookService {

    private BookRepository bookRepository;
    private BookTransformer bookTransformer;

    @Autowired
    public BookService(BookRepository bookRepository, BookTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
    }

    public Long create(Book book) {
        BookEntity bookEntity = bookTransformer.transformToBookEntity(book);
        BookEntity entity = bookRepository.save(bookEntity);
        return entity.getId();
    }

    public Iterable<Book> getAll() {
        Iterable<BookEntity> all = bookRepository.findAll();
        List<Book> result = new ArrayList<>();
        for (BookEntity bookEntity : all) {
            result.add(bookTransformer.transformToBook(bookEntity));
        }
        return result;
    }

    public boolean delete(Long id) {
        boolean exists = bookRepository.exists(id);
        if (!exists) {
            return false;
        } else {
            bookRepository.delete(id);
            return true;
        }
    }

    public Book get(Long id) {
        BookEntity bookEntity = bookRepository.findOne(id);
        return Objects.isNull(bookEntity) ? null : bookTransformer.transformToBook(bookEntity);
    }

    public boolean update(Long id, Book book) {
        boolean exist = bookRepository.exists(id);
        if (!exist) {
            return false;
        } else {
            BookEntity bookEntity = bookTransformer.transformToBookEntity(book);
            bookEntity.setId(id);
            bookRepository.save(bookEntity);
            return true;
        }
    }
}
