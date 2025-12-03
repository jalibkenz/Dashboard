package in.kenz.dashboard.dao;

import in.kenz.dashboard.entity.Book;
import in.kenz.dashboard.entity.Loan;
import in.kenz.dashboard.entity.Role;
import in.kenz.dashboard.entity.User;

import java.time.LocalDate;

public interface LoanDao {
    void save(Loan loan);
    Loan findByLoanId(Long roleId);
    Loan findByLoanAvailingUser(User user);
    Loan findByLoanAvailedBook(Book book);
    Loan findByLoanAvailingDate(LocalDate localdate);
    void deleteByLoanId(Long loanId);
}
