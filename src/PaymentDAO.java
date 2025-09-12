
import java.sql.*;

public class PaymentDAO {

    public static Payment makePayment(Payment payment) {
        String insert = "INSERT INTO payments (booking_id, amount, status) VALUES (?, ?, ?) RETURNING id, payment_date";
        String updateBooking = "UPDATE bookings SET total_amount=? WHERE id=?";
        String rollbackBooking = "DELETE FROM bookings WHERE id=?"; // if failed

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // insert payment
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setInt(1, payment.getBookingId());
                ps.setDouble(2, payment.getAmount());
                ps.setString(3, payment.getStatus());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    payment.setId(rs.getInt("id"));
                    payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                }
            }

            if ("SUCCESS".equalsIgnoreCase(payment.getStatus())) {
                try (PreparedStatement ps = conn.prepareStatement(updateBooking)) {
                    ps.setDouble(1, payment.getAmount());
                    ps.setInt(2, payment.getBookingId());
                    ps.executeUpdate();
                }
            } else if ("FAILED".equalsIgnoreCase(payment.getStatus())) {
                try (PreparedStatement ps = conn.prepareStatement(rollbackBooking)) {
                    ps.setInt(1, payment.getBookingId());
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return payment;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment findById(int id) {
        String sql = "SELECT * FROM payments WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setBookingId(rs.getInt("booking_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                p.setStatus(rs.getString("status"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
