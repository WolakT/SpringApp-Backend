package book.dao;

import org.springframework.data.repository.CrudRepository;

import book.entity.BookEntity;

public interface BookRepository extends CrudRepository<BookEntity, Long> {
}
