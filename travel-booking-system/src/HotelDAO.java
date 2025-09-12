
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

    // Add new hotel
    public static Hotel addHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (name, location, rooms_available, price_per_night) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setInt(3, hotel.getRoomsAvailable());
            ps.setDouble(4, hotel.getPricePerNight());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hotel.setId(rs.getInt("id"));
            }
            return hotel;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update rooms or price
    public static boolean updateHotel(int id, int roomsAvailable, double pricePerNight) {
        String sql = "UPDATE hotels SET rooms_available=?, price_per_night=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomsAvailable);
            ps.setDouble(2, pricePerNight);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search hotels by location
    public static List<Hotel> searchHotels(String location) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE location=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, location);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hotel h = new Hotel();
                h.setId(rs.getInt("id"));
                h.setName(rs.getString("name"));
                h.setLocation(rs.getString("location"));
                h.setRoomsAvailable(rs.getInt("rooms_available"));
                h.setPricePerNight(rs.getDouble("price_per_night"));
                hotels.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    // Book hotel (reduce available rooms)
    public boolean bookHotel(int hotelId, int rooms) {
        String sql = "UPDATE hotels SET rooms_available = rooms_available - ? WHERE id=? AND rooms_available >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, rooms);
            ps.setInt(2, hotelId);
            ps.setInt(3, rooms);
            return ps.executeUpdate() > 0; // success only if enough rooms
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4) Hotel with maximum occupancy
    public static void getMaxOccupancyHotel() {
        String sql = "SELECT h.id, h.name, h.location, COALESCE(SUM(bi.quantity),0) AS rooms_booked " +
                "FROM booking_items bi " +
                "JOIN hotels h ON bi.item_type = 'HOTEL' AND bi.item_id = h.id " +
                "GROUP BY h.id, h.name, h.location " +
                "ORDER BY rooms_booked DESC " +
                "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.printf("\nHotel with maximum occupancy: %s (%s) | Rooms booked: %d%n",
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getInt("rooms_booked"));
            } else {
                System.out.println("No hotel bookings found.");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching hotel occupancy.");
            e.printStackTrace();
        }
    }

    public static void deleteHotel(int id) {
        String sql = "DELETE FROM hotels WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Hotel deleted successfully.");
            else System.out.println("Hotel ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
