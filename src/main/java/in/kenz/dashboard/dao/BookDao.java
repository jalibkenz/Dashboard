package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Book;

import java.util.List;

public interface BookDao {
    void save(Book book);
    Book findByBookId(Long bookId);
    Book findByBookName(String bookName);
    Book findByBookCategory(String bookCategory);
    Book findByBookAuthor(String bookAuthor);
    List<Book> findAll();
    void deleteByBookId(Long bookId);
}
