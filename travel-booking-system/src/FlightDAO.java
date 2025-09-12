

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    // Add new flight
    public static Flight addFlight(Flight flight) {
        String sql = "INSERT INTO flights (flight_no, origin, destination, seats_available, price) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flight.getFlightNo());
            ps.setString(2, flight.getOrigin());
            ps.setString(3, flight.getDestination());
            ps.setInt(4, flight.getSeatsAvailable());
            ps.setDouble(5, flight.getPrice());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                flight.setId(rs.getInt("id"));
            }
            return flight;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update seats or price
    public static boolean updateFlight(int id, int seatsAvailable, double price) {
        String sql = "UPDATE flights SET seats_available=?, price=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatsAvailable);
            ps.setDouble(2, price);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Search flights by origin/destination
    public static List<Flight> searchFlights(String origin, String destination) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE origin=? AND destination=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, origin);
            ps.setString(2, destination);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Flight f = new Flight();
                f.setId(rs.getInt("id"));
                f.setFlightNo(rs.getString("flight_no"));
                f.setOrigin(rs.getString("origin"));
                f.setDestination(rs.getString("destination"));
                f.setSeatsAvailable(rs.getInt("seats_available"));
                f.setPrice(rs.getDouble("price"));
                flights.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    // Book flight (reduce available seats)
    public boolean bookFlight(int flightId, int seats) {
        String sql = "UPDATE flights SET seats_available = seats_available - ? WHERE id=? AND seats_available >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seats);
            ps.setInt(2, flightId);
            ps.setInt(3, seats);
            return ps.executeUpdate() > 0; // success only if enough seats
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteFlight(int id) {
        String sql = "DELETE FROM flights WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println("Flight deleted successfully.");
            else System.out.println("Flight ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
