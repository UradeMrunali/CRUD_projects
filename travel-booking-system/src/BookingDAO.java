
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Create a booking and insert items, update total_amount
    public static Booking createBooking(Booking booking) {
        String insertBooking = "INSERT INTO bookings (customer_id, total_amount) VALUES (?, ?) RETURNING id, booking_date";
        String insertItem = "INSERT INTO booking_items (booking_id, item_type, item_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
        String updateTotal = "UPDATE bookings SET total_amount=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // insert booking
            try (PreparedStatement ps = conn.prepareStatement(insertBooking)) {
                ps.setInt(1, booking.getCustomerId());
                ps.setDouble(2, 0); // will update later
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    booking.setId(rs.getInt("id"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                }
            }

            double total = 0;
            for (BookingItem item : booking.getItems()) {
                try (PreparedStatement ps = conn.prepareStatement(insertItem)) {
                    ps.setInt(1, booking.getId());
                    ps.setString(2, item.getItemType());
                    ps.setInt(3, item.getItemId());
                    ps.setInt(4, item.getQuantity());
                    ps.setDouble(5, item.getPrice());
                    ps.executeUpdate();
                }
                total += item.getQuantity() * item.getPrice();

                // Decrease seats or rooms
                if ("FLIGHT".equalsIgnoreCase(item.getItemType())) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE flights SET seats_available = seats_available - ? WHERE id = ? AND seats_available >= ?")) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getItemId());
                        ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() == 0) {
                            conn.rollback();
                            throw new SQLException("Not enough seats available for flight " + item.getItemId());
                        }
                    }
                } else if ("HOTEL".equalsIgnoreCase(item.getItemType())) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE hotels SET rooms_available = rooms_available - ? WHERE id = ? AND rooms_available >= ?")) {
                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getItemId());
                        ps.setInt(3, item.getQuantity());
                        if (ps.executeUpdate() == 0) {
                            conn.rollback();
                            throw new SQLException("Not enough rooms available for hotel " + item.getItemId());
                        }
                    }
                }
            }

            // update total
            booking.setTotalAmount(total);
            try (PreparedStatement ps = conn.prepareStatement(updateTotal)) {
                ps.setDouble(1, total);
                ps.setInt(2, booking.getId());
                ps.executeUpdate();
            }

            conn.commit();
            return booking;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cancel booking and rollback availability
    public static boolean cancelBooking(int bookingId) {
        String getItems = "SELECT item_type, item_id, quantity FROM booking_items WHERE booking_id=?";
        String updateFlight = "UPDATE flights SET seats_available = seats_available + ? WHERE id=?";
        String updateHotel = "UPDATE hotels SET rooms_available = rooms_available + ? WHERE id=?";
        String deleteBooking = "DELETE FROM bookings WHERE id=?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(getItems)) {
                ps.setInt(1, bookingId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String type = rs.getString("item_type");
                    int itemId = rs.getInt("item_id");
                    int qty = rs.getInt("quantity");

                    if ("FLIGHT".equalsIgnoreCase(type)) {
                        try (PreparedStatement ps2 = conn.prepareStatement(updateFlight)) {
                            ps2.setInt(1, qty);
                            ps2.setInt(2, itemId);
                            ps2.executeUpdate();
                        }
                    } else if ("HOTEL".equalsIgnoreCase(type)) {
                        try (PreparedStatement ps2 = conn.prepareStatement(updateHotel)) {
                            ps2.setInt(1, qty);
                            ps2.setInt(2, itemId);
                            ps2.executeUpdate();
                        }
                    }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(deleteBooking)) {
                ps.setInt(1, bookingId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Booking findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("id"));
                b.setCustomerId(rs.getInt("customer_id"));
                b.setBookingDate(rs.getTimestamp("booking_date").toLocalDateTime());
                b.setTotalAmount(rs.getDouble("total_amount"));
                return b;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 1) Get all bookings of a customer (with flights/hotels + amount)
    public static void getBookingsByCustomer(int customerId) {
        String sql = "SELECT b.id AS booking_id, b.booking_date, b.total_amount, " +
                "bi.item_type, bi.item_id, bi.quantity, bi.price, " +
                "f.flight_no, f.origin, f.destination, " +
                "h.name AS hotel_name, h.location AS hotel_location " +
                "FROM bookings b " +
                "JOIN booking_items bi ON b.id = bi.booking_id " +
                "LEFT JOIN flights f ON bi.item_type = 'FLIGHT' AND bi.item_id = f.id " +
                "LEFT JOIN hotels h  ON bi.item_type = 'HOTEL'  AND bi.item_id = h.id " +
                "WHERE b.customer_id = ? " +
                "ORDER BY b.booking_date DESC, b.id, bi.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                int currentBookingId = -1;
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    int bookingId = rs.getInt("booking_id");
                    if (bookingId != currentBookingId) {
                        currentBookingId = bookingId;
                        Timestamp ts = rs.getTimestamp("booking_date");
                        String dateStr = ts != null ? ts.toString() : "N/A";
                        double total = rs.getDouble("total_amount");
                        System.out.println("\nBooking ID: " + bookingId + " | Date: " + dateStr + " | Total: " + total);
                        System.out.println("Items:");
                    }

                    String type = rs.getString("item_type");
                    int itemId = rs.getInt("item_id");
                    int qty = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    if ("FLIGHT".equalsIgnoreCase(type)) {
                        String flightNo = rs.getString("flight_no");
                        String origin = rs.getString("origin");
                        String destination = rs.getString("destination");
                        System.out.printf("  - FLIGHT: %s (ID:%d) %s → %s | Qty:%d | Price: %.2f%n",
                                flightNo != null ? flightNo : "-", itemId,
                                origin != null ? origin : "-", destination != null ? destination : "-", qty, price);
                    } else if ("HOTEL".equalsIgnoreCase(type)) {
                        String hotelName = rs.getString("hotel_name");
                        String hotelLoc = rs.getString("hotel_location");
                        System.out.printf("  - HOTEL: %s (ID:%d) %s | Qty:%d | Price: %.2f%n",
                                hotelName != null ? hotelName : "-", itemId,
                                hotelLoc != null ? "(" + hotelLoc + ")" : "", qty, price);
                    } else {
                        System.out.printf("  - UNKNOWN ITEM TYPE (%s) ID:%d | Qty:%d | Price: %.2f%n",
                                type, itemId, qty, price);
                    }
                }

                if (!any) {
                    System.out.println("No bookings found for customer id: " + customerId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings for customer " + customerId);
            e.printStackTrace();
        }
    }

    // 2) Top 5 customers who spent the most money
    public static void getTopCustomers() {
        String sql = "SELECT c.id, c.name, COALESCE(SUM(b.total_amount),0) AS spent " +
                "FROM customers c " +
                "LEFT JOIN bookings b ON c.id = b.customer_id " +
                "GROUP BY c.id, c.name " +
                "ORDER BY spent DESC " +
                "LIMIT 5";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nTop 5 customers by spending:");
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("Customer %d | %s | Spent: %.2f%n",
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("spent"));
            }
            if (!any) System.out.println("No customer spending data available.");
        } catch (SQLException e) {
            System.err.println("Error fetching top customers.");
            e.printStackTrace();
        }
    }

    // 3) Most popular flight route (origin -> destination with highest bookings)
    public static void getPopularFlightRoute() {
        String sql = "SELECT f.origin, f.destination, COUNT(*) AS bookings_count " +
                "FROM booking_items bi " +
                "JOIN flights f ON bi.item_type = 'FLIGHT' AND bi.item_id = f.id " +
                "GROUP BY f.origin, f.destination " +
                "ORDER BY bookings_count DESC " +
                "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.printf("\nMost popular route: %s → %s | Bookings: %d%n",
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt("bookings_count"));
            } else {
                System.out.println("No flight bookings found.");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching popular flight route.");
            e.printStackTrace();
        }
    }

    // 5) Monthly revenue report (total income grouped by month)
    public static void getMonthlyRevenue() {
        String sql = "SELECT TO_CHAR(booking_date, 'YYYY-MM') AS month, COALESCE(SUM(total_amount),0) AS revenue " +
                "FROM bookings " +
                "GROUP BY month " +
                "ORDER BY month";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nMonthly Revenue Report:");
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("%s | Revenue: %.2f%n", rs.getString("month"), rs.getDouble("revenue"));
            }
            if (!any) System.out.println("No revenue records found.");
        } catch (SQLException e) {
            System.err.println("Error fetching monthly revenue.");
            e.printStackTrace();
        }
    }
}
