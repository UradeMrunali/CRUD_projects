//package com.example.crud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDao {

    public void addCourse(Course course) throws Exception {
        String sql = "INSERT INTO courses (title, credits) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, course.getTitle());
            ps.setInt(2, course.getCredits());
            ps.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws Exception {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(new Course(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("credits")
                ));
            }
        }
        return courses;
    }
}
