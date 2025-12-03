package in.kenz.dashboard.service;

import in.kenz.dashboard.entity.Book;
import java.util.List;

public interface BookService {

    // Basic CRUD
    void save(Book book);
    Book findById(Long id);
    List<Book> findAll();
    void delete(Long id);

    // Dashboard-specific functionalities
    List<Book> findLoanedBooks();     // Books currently loaned out
    List<Book> findAvailableBooks();  // Books not currently loaned
}
