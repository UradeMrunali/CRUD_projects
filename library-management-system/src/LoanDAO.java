
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class LoanDAO {
    public boolean borrowBook(int bookId, int memberId) throws SQLException {
        String check = "SELECT available FROM books WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && !rs.getBoolean("available")) return false;
        }

        String loan = "INSERT INTO loans (book_id, member_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(loan)) {
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            ps.executeUpdate();
        }

        String update = "UPDATE books SET available=FALSE WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
        return true;
    }

    public boolean returnBook(int loanId) throws SQLException {
        String sql = "UPDATE loans SET return_date=CURRENT_DATE WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            ps.executeUpdate();
        }

        String bookUpdate = "UPDATE books SET available=TRUE WHERE id=(SELECT book_id FROM loans WHERE id=?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(bookUpdate)) {
            ps.setInt(1, loanId);
            ps.executeUpdate();
        }
        return true;
    }

    public void extendLoan(int loanId, Date newReturnDate) throws SQLException {
        String sql = "UPDATE loans SET return_date=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, newReturnDate);
            ps.setInt(2, loanId);
            ps.executeUpdate();
        }
    }

    public List<Loan> borrowedBooks(int memberId) throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE member_id=? AND return_date IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Loan l = new Loan();
                l.setId(rs.getInt("id"));
                l.setBookId(rs.getInt("book_id"));
                l.setMemberId(rs.getInt("member_id"));
                l.setLoanDate(rs.getDate("loan_date"));
                list.add(l);
            }
        }
        return list;
    }

    public List<Loan> overdueBooks() throws SQLException {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE return_date < CURRENT_DATE";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Loan l = new Loan();
                l.setId(rs.getInt("id"));
                l.setBookId(rs.getInt("book_id"));
                l.setMemberId(rs.getInt("member_id"));
                l.setLoanDate(rs.getDate("loan_date"));
                l.setReturnDate(rs.getDate("return_date"));
                list.add(l);
            }
        }
        return list;
    }

    public String mostBorrowedBook() throws SQLException {
        String sql = """
            SELECT b.title, COUNT(*) as count
            FROM loans l
            JOIN books b ON l.book_id=b.id
            GROUP BY b.title
            ORDER BY count DESC LIMIT 1
        """;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getString("title");
        }
        return null;
    }

    public String mostActiveMember() throws SQLException {
        String sql = """
            SELECT m.name, COUNT(*) as count
            FROM loans l
            JOIN members m ON l.member_id=m.id
            GROUP BY m.name
            ORDER BY count DESC LIMIT 1
        """;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) return rs.getString("name");
        }
        return null;
    }

    public int calculateFine(int loanId) throws SQLException {
        String sql = "SELECT return_date FROM loans WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Date returnDate = rs.getDate("return_date");
                if (returnDate != null) {
                    long diff = (System.currentTimeMillis() - returnDate.getTime()) / (1000 * 60 * 60 * 24);
                    if (diff > 0) return (int) diff * 10;
                }
            }
        }
        return 0;
    }
}

