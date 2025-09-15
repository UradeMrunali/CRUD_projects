
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreator {

    public static void createTables() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Books table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS books (
                    id SERIAL PRIMARY KEY,
                    title VARCHAR(100),
                    author VARCHAR(100),
                    available BOOLEAN DEFAULT TRUE
                )
            """);

            // Members table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS members (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100)
                )
            """);

            // Loans table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS loans (
                    id SERIAL PRIMARY KEY,
                    book_id INT REFERENCES books(id),
                    member_id INT REFERENCES members(id),
                    loan_date DATE DEFAULT CURRENT_DATE,
                    return_date DATE
                )
            """);

            System.out.println("✅ Tables are ready!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

