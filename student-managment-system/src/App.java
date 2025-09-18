import java.util.List;

public class App {
    public static void main(String[] args) {
        StudentDao StudentDao = new StudentDao();
        CourseDao CourseDao = new CourseDao();
        EnrollmentDao EnrollmentDao = new EnrollmentDao();

        try {
            Student s1 = new Student(0, "John Doe", 20, "john@example.com");
            Student s2 = new Student(0, "Alice Smith", 22, "alice@example.com");

            StudentDao.addStudent(s1);
            StudentDao.addStudent(s2);
            System.out.println("Students added.");

            Course c1 = new Course(0, "Mathematics", 4);
            Course c2 = new Course(0, "Physics", 3);

            CourseDao.addCourse(c1);
            CourseDao.addCourse(c2);
            System.out.println("Courses added.");

            EnrollmentDao.enrollStudent(new Enrollment(0, 1, 1, "A")); // John in Math
            EnrollmentDao.enrollStudent(new Enrollment(0, 2, 2, "A")); // Alice in Physics
            System.out.println("Enrollments added.");

            System.out.println("\n--- All Students ---");
            List<Student> st = StudentDao.getAllStudents();
            st.forEach(System.out::println);

            System.out.println("\n--- All Courses ---");
            List<Course> courses = CourseDao.getAllCourses();
            courses.forEach(System.out::println);

            System.out.println("\n--- Enrollments for Student ID 1 ---");
            List<Enrollment> enrollments = EnrollmentDao.getEnrollmentsByStudent(1);
            enrollments.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
