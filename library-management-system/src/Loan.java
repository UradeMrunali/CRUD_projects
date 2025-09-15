import java.sql.Date;

public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private Date loanDate;
    private Date returnDate;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public Date getLoanDate() { return loanDate; }
    public void setLoanDate(Date loanDate) { this.loanDate = loanDate; }

    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }

    @Override
    public String toString() {
        return id + " | BookID: " + bookId + " | MemberID: " + memberId +
                " | LoanDate: " + loanDate + " | ReturnDate: " + returnDate;
    }
}

