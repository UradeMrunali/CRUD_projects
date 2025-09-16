import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {
    public void openAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, balance) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, account.getCustomerId());
            pstmt.setDouble(2, account.getBalance());
            pstmt.executeUpdate();
        }
    }

    public Account getAccountById(int id) throws SQLException {
        String sql = "SELECT id, customer_id, balance FROM accounts WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(rs.getInt("id"),
                            rs.getInt("customer_id"),
                            rs.getDouble("balance"));
                }
            }
        }
        return null;
    }

    public void deposit(int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }
        recordTransaction(accountId, "deposit", amount);
    }

    public void withdraw(int accountId, double amount) throws SQLException {
        String checkBalance = "SELECT balance FROM accounts WHERE id = ?";
        String updateBalance = "UPDATE accounts SET balance = balance - ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(checkBalance)) {
                pstmt.setInt(1, accountId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next() && rs.getDouble("balance") >= amount) {
                        try (PreparedStatement upd = conn.prepareStatement(updateBalance)) {
                            upd.setDouble(1, amount);
                            upd.setInt(2, accountId);
                            upd.executeUpdate();
                        }
                        recordTransaction(accountId, "withdraw", amount);
                        conn.commit();
                    } else {
                        conn.rollback();
                        System.out.println("Insufficient balance!");
                    }
                }
            }
        }
    }

    public void transfer(int fromAcc, int toAcc, double amount) throws SQLException {
        String withdrawSql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        String depositSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSql);
                 PreparedStatement depositStmt = conn.prepareStatement(depositSql)) {
                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setInt(2, fromAcc);
                withdrawStmt.executeUpdate();

                depositStmt.setDouble(1, amount);
                depositStmt.setInt(2, toAcc);
                depositStmt.executeUpdate();

                recordTransaction(fromAcc, "transfer", amount);
                recordTransaction(toAcc, "transfer", amount);

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void deleteAccount(int accountId) throws SQLException {
        String checkSql = "SELECT balance FROM accounts WHERE id = ?";
        String deleteSql = "DELETE FROM accounts WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, accountId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getDouble("balance") == 0) {
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                        deleteStmt.setInt(1, accountId);
                        deleteStmt.executeUpdate();
                    }
                } else {
                    System.out.println("Account cannot be deleted. Balance must be 0.");
                }
            }
        }
    }

    private void recordTransaction(int accountId, String type, double amount) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        }
    }
}
