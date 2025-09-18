//package com.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDao {

    public void enrollStudent(Enrollment enrollment) throws Exception {
        String sql = "INSERT INTO enrollments (student_id, course_id, grade) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getCourseId());
            ps.setString(3, enrollment.getGrade());
            ps.executeUpdate();
        }
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) throws Exception {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                        rs.getInt("id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("grade")
                ));
            }
        }
        return list;
    }

    public void updateGrade(int enrollmentId, String grade) throws Exception {
        String sql = "UPDATE enrollments SET grade=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, grade);
            ps.setInt(2, enrollmentId);
            ps.executeUpdate();
        }
    }
}

