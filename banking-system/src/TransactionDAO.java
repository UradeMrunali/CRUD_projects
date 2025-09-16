import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getLastNTransactions(int accountId, int n) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, account_id, type, amount, date " +
                "FROM transactions WHERE account_id = ? " +
                "ORDER BY date DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setInt(2, n);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getInt("id"),
                            rs.getInt("account_id"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("date")));
                }
            }
        }
        return transactions;
    }

    public List<Transaction> getTransactionsByDateRange(int accountId, String startDate, String endDate) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, account_id, type, amount, date FROM transactions " +
                "WHERE account_id = ? AND date BETWEEN ? AND ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setTimestamp(2, Timestamp.valueOf(startDate + " 00:00:00"));
            pstmt.setTimestamp(3, Timestamp.valueOf(endDate + " 23:59:59"));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                            rs.getInt("id"),
                            rs.getInt("account_id"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("date")));
                }
            }
        }
        return transactions;
    }
}
