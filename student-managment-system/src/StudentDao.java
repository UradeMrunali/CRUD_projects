import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {

    public void addStudent(Student st){
        String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){
        ps.setString(1, st.getName());
        ps.setInt(2, st.getAge());
        ps.setString(3, st.getEmail());
        ps.executeUpdate();

            System.out.println("Student added: " + st.getName());
    } catch (SQLException e) {
            e.printStackTrace();
        }
        }


        public List<Student> getAllStudents(){
           List<Student> st = new ArrayList<>();
           String sql = "SELECT * FROM students";
           try (Connection con = DBConnection.getConnection();
                Statement s = con.createStatement();
                ResultSet rs = s.executeQuery(sql)) {
               while (rs.next()){
                   st.add(new Student(
                           rs.getInt("id"),
                           rs.getString("name"),
                           rs.getInt("age"),
                           rs.getString("email")
                   ));
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }
           return st;
        }

    public Student getStudentById(int id) {
        String sql = "SELECT * FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Student> getStudentByName(String name) {
        List<Student> users = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE name=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateStudent(Student st) {
        String sql = "UPDATE students SET name=?, age=?, email=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, st.getName());
            ps.setInt(2, st.getAge());
            ps.setString(4, st.getEmail());
            ps.setInt(5, st.getId());
            ps.executeUpdate();
            System.out.println("Student updated: " + st.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudentById(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Student deleted by ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudentByName(String name) {
        String sql = "DELETE FROM students WHERE name=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
            System.out.println("Student deleted by name: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
