package in.kenz.dashboard.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Loan {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    @ManyToOne
    private User loanAvailingUser;

    @ManyToOne
    private Book loanAvailedBook;

    private LocalDate loanAvailingDate;





    public Loan() {
    }

    public Loan(Long loanId, User loanAvailingUser, Book loanAvailedBook, LocalDate loanAvailingDate) {
        this.loanId = loanId;
        this.loanAvailingUser = loanAvailingUser;
        this.loanAvailedBook = loanAvailedBook;
        this.loanAvailingDate = loanAvailingDate;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", loanAvailingUser=" + loanAvailingUser +
                ", loanAvailedBook=" + loanAvailedBook +
                ", loanAvailingDate=" + loanAvailingDate +
                '}';
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public User getLoanAvailingUser() {
        return loanAvailingUser;
    }

    public void setLoanAvailingUser(User loanAvailingUser) {
        this.loanAvailingUser = loanAvailingUser;
    }

    public Book getLoanAvailedBook() {
        return loanAvailedBook;
    }

    public void setLoanAvailedBook(Book loanAvailedBook) {
        this.loanAvailedBook = loanAvailedBook;
    }

    public LocalDate getLoanAvailingDate() {
        return loanAvailingDate;
    }

    public void setLoanAvailingDate(LocalDate loanAvailingDate) {
        this.loanAvailingDate = loanAvailingDate;
    }
}

