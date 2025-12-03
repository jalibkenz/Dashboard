package in.kenz.dashboard.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Loan")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long loanId;

    /**
     * The user who availed (borrowed) the book.
     * No cascade by default — deleting a loan must not delete the user.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User loanAvailingUser;

    /**
     * The book being loaned.
     * No cascade here as well.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book loanAvailedBook;

    // When the loan was made
    private LocalDate loanAvailingDate;

    // When the book is due back
    private LocalDate dueDate;

    // When the book was actually returned (null if still on loan)
    private LocalDate returnedDate;

    // Optional status field (ACTIVE, RETURNED, EXTENDED, etc.)
    private String status;

    public Loan() {}

    public Loan(User loanAvailingUser, Book loanAvailedBook, LocalDate loanAvailingDate, LocalDate dueDate) {
        this.loanAvailingUser = loanAvailingUser;
        this.loanAvailedBook = loanAvailedBook;
        this.loanAvailingDate = loanAvailingDate;
        this.dueDate = dueDate;
        this.status = "ACTIVE";
    }

    // --- getters / setters ---
    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public User getLoanAvailingUser() { return loanAvailingUser; }
    public void setLoanAvailingUser(User loanAvailingUser) { this.loanAvailingUser = loanAvailingUser; }

    public Book getLoanAvailedBook() { return loanAvailedBook; }
    public void setLoanAvailedBook(Book loanAvailedBook) { this.loanAvailedBook = loanAvailedBook; }

    public LocalDate getLoanAvailingDate() { return loanAvailingDate; }
    public void setLoanAvailingDate(LocalDate loanAvailingDate) { this.loanAvailingDate = loanAvailingDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalDate getReturnedDate() { return returnedDate; }
    public void setReturnedDate(LocalDate returnedDate) { this.returnedDate = returnedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // convenience: active loan = not returned and status active
    @Transient
    public boolean isActive() {
        return returnedDate == null && (status == null || "ACTIVE".equalsIgnoreCase(status));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return Objects.equals(loanId, loan.loanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", loanAvailingUser=" + (loanAvailingUser != null ? loanAvailingUser.getUserUsername() : "null") +
                ", loanAvailedBook=" + (loanAvailedBook != null ? loanAvailedBook.getBookName() : "null") +
                ", loanAvailingDate=" + loanAvailingDate +
                ", dueDate=" + dueDate +
                ", returnedDate=" + returnedDate +
                ", status='" + status + '\'' +
                '}';
    }
}
