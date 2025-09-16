import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {
    public List<String> top5RichestCustomers() throws SQLException {
        List<String> result = new ArrayList<>();
        String sql = "SELECT c.name, SUM(a.balance) as total_balance " +
                "FROM customers c JOIN accounts a ON c.id = a.customer_id " +
                "GROUP BY c.name ORDER BY total_balance DESC LIMIT 5";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(rs.getString("name") + " - Balance: " + rs.getDouble("total_balance"));
            }
        }
        return result;
    }

    public List<Account> accountsWithNegativeBalance() throws SQLException {
        List<Account> result = new ArrayList<>();
        String sql = "SELECT id, customer_id, balance FROM accounts WHERE balance < 0";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(new Account(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getDouble("balance")));
            }
        }
        return result;
    }

    public double totalBankDeposits() throws SQLException {
        String sql = "SELECT SUM(balance) as total FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble("total");
        }
        return 0;
    }
}
