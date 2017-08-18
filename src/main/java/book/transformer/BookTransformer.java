package book.transformer;

import org.springframework.stereotype.Component;

import book.dto.Book;
import book.entity.BookEntity;

@Component
public class BookTransformer {

    public BookEntity transformToBookEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setAuthor(book.getAuthor());
        bookEntity.setCategory(book.getCategory());
        bookEntity.setIsbn(book.getIsbn());
        bookEntity.setTitle(book.getTitle());
        return bookEntity;
    }

    public Book transformToBook(BookEntity bookEntity) {
        Book book = new Book();
        book.setAuthor(bookEntity.getAuthor());
        book.setCategory(bookEntity.getCategory());
        book.setIsbn(bookEntity.getIsbn());
        book.setTitle(bookEntity.getTitle());
        return book;
    }
}
