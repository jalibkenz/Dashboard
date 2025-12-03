package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Book;

public interface BookDao {
    void save(Book book);
    Book findByBookId(Long bookId);
    Book findByBookName(String bookName);
    Book findByBookCategory(String bookCategory);
    Book findByBookAuthor(String bookAuthor);
    void deleteByBookId(Long bookId);
}
