
import java.sql.Connection;
import java.sql.Statement;

public class TableInitializer {

    public static void init() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            // Customers
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS customers (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    phone VARCHAR(15)
                )
            """);

            // Flights
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS flights (
                    id SERIAL PRIMARY KEY,
                    flight_no VARCHAR(20) UNIQUE,
                    origin VARCHAR(50),
                    destination VARCHAR(50),
                    seats_available INT,
                    price DECIMAL(10,2)
                )
            """);

            // Hotels
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS hotels (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100),
                    location VARCHAR(50),
                    rooms_available INT,
                    price_per_night DECIMAL(10,2)
                )
            """);

            // Bookings
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bookings (
                    id SERIAL PRIMARY KEY,
                    customer_id INT REFERENCES customers(id) ON DELETE CASCADE,
                    booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    total_amount DECIMAL(12,2)
                )
            """);

            // Booking items
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS booking_items (
                    id SERIAL PRIMARY KEY,
                    booking_id INT REFERENCES bookings(id) ON DELETE CASCADE,
                    item_type VARCHAR(10) CHECK (item_type IN ('FLIGHT', 'HOTEL')),
                    item_id INT,
                    quantity INT,
                    price DECIMAL(10,2)
                )
            """);

            // Payments
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS payments (
                    id SERIAL PRIMARY KEY,
                    booking_id INT REFERENCES bookings(id) ON DELETE CASCADE,
                    amount DECIMAL(12,2),
                    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    status VARCHAR(20) CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED'))
                )
            """);

            System.out.println("✅ All tables initialized successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error while initializing tables!");
            e.printStackTrace();
        }
    }
}
