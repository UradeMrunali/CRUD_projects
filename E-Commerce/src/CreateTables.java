import java.sql.Connection;
import java.sql.Statement;

public class CreateTables {
    public static void createTablesIfNotExist() {
        String createCustomers = """
            CREATE TABLE IF NOT EXISTS customers (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100),
                email VARCHAR(100),
                phone VARCHAR(15)
            );
            """;

        String createProducts = """
            CREATE TABLE IF NOT EXISTS products (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100),
                price DECIMAL(10,2),
                stock INT
            );
            """;

        String createOrders = """
            CREATE TABLE IF NOT EXISTS orders (
                id SERIAL PRIMARY KEY,
                customer_id INT REFERENCES customers(id) ON DELETE CASCADE,
                order_date TIMESTAMP DEFAULT NOW()
            );
            """;

        String createOrderItems = """
            CREATE TABLE IF NOT EXISTS order_items (
                id SERIAL PRIMARY KEY,
                order_id INT REFERENCES orders(id) ON DELETE CASCADE,
                product_id INT REFERENCES products(id),
                quantity INT
            );
            """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // execute in order
            stmt.execute(createCustomers);
            stmt.execute(createProducts);
            stmt.execute(createOrders);
            stmt.execute(createOrderItems);

            System.out.println("Schema initialization complete (tables created if not present).");

        } catch (Exception e) {
            System.err.println("Failed to initialize schema:");
            e.printStackTrace();
        }
    }
}
