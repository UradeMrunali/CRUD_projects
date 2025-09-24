import java.sql.*;

public class TableInitializer {
    private static boolean checkIfTableExists(Connection conn, String tableName) throws SQLException {
        String sql = "SELECT EXISTS (" +
                "SELECT 1 FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableName.toLowerCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getBoolean(1);
            }
        }
        return false;
    }

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        if (!checkIfTableExists(conn, "customers")) {
            stmt.executeUpdate("CREATE TABLE customers (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "email VARCHAR(100))");
        }

        if (!checkIfTableExists(conn, "accounts")) {
            stmt.executeUpdate("CREATE TABLE accounts (" +
                    "id SERIAL PRIMARY KEY, " +
                    "customer_id INT REFERENCES customers(id), " +
                    "balance DECIMAL(12,2))");
        }

    
        if (!checkIfTableExists(conn, "transactions")) {
            stmt.executeUpdate("CREATE TABLE transactions (" +
                    "id SERIAL PRIMARY KEY, " +
                    "account_id INT REFERENCES accounts(id), " +
                    "type VARCHAR(10), " +
                    "amount DECIMAL(12,2), " +
                    "date TIMESTAMP DEFAULT NOW())");
        }
    }
}

