package crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsDAO {

    public void bookAppointment(Appointments a) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, date, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getPatientId());
            ps.setInt(2, a.getDoctorId());
            ps.setDate(3, a.getDate());
            ps.setString(4, a.getNotes());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAppointmentNotes(int id, String notes) {
        String sql = "UPDATE appointments SET notes = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, notes);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Appointments> getAppointmentsByDoctor(int doctorId) {
        List<Appointments> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ps.setInt(1, doctorId);
            while (rs.next()) {
                list.add(new Appointments(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("date"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointments> getAppointmentsByPatient(int patientId) {
        List<Appointments> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointments(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("date"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Appointments> getAppointmentsByDate(Date date) {
        List<Appointments> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE date = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointments(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("date"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }



    // REPORTS

    public int getDoctorWithMostPatients() {
        String sql = "SELECT doctor_id, COUNT(DISTINCT patient_id) AS patient_count " +
                "FROM appointments " +
                "GROUP BY doctor_id " +
                "ORDER BY patient_count DESC " +
                "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("doctor_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // if no data
    }


    public int getPatientWithMostVisits() {
        String sql = "SELECT patient_id, COUNT(*) AS visit_count " +
                "FROM appointments " +
                "GROUP BY patient_id " +
                "ORDER BY visit_count DESC " +
                "LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("patient_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // if no data
    }


    public List<Appointments> getUpcomingAppointments() {
        List<Appointments> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments " +
                "WHERE date >= CURRENT_DATE AND date <= CURRENT_DATE + INTERVAL '7 days' " +
                "ORDER BY date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Appointments(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("date"),
                        rs.getString("notes")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
