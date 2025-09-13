package crud;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App {
    public static void main(String[] args) {

        // DAOs
        PatientsDAO patientsDAO = new PatientsDAO();
        DoctorsDAO doctorsDAO = new DoctorsDAO();
        AppointmentsDAO appointmentsDAO = new AppointmentsDAO();

      /*  //  Hard-coded entries
        Patients p1 = new Patients("Mrunali", 24, "9876543210");
        Patients p2 = new Patients("Yashasvi", 25, "9123456789");
        patientsDAO.addPatients(p1);
        patientsDAO.addPatients(p2);

        Doctors d1 = new Doctors("Dr. Sharma", "Cardiologist");
        Doctors d2 = new Doctors("Dr. Singh", "Neurologist");
        doctorsDAO.addDoctors(d1);
        doctorsDAO.addDoctors(d2);

        Appointments a1 = new Appointments(1, 1, Date.valueOf("2025-09-15"), "Initial checkup");
        Appointments a2 = new Appointments(2, 2, Date.valueOf("2025-09-16"), "Follow-up");
        appointmentsDAO.bookAppointment(a1);
        appointmentsDAO.bookAppointment(a2);

        List<Appointments> upcoming = appointmentsDAO.getUpcomingAppointments();
        System.out.println("Upcoming appointments:");
        for (Appointments a : upcoming) {
            System.out.println(a);
        }
    }
       */


     //USER INPUT entries
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter patient name:");
    String pname = sc.nextLine();
        System.out.println("Enter patient age:");
    int page = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.println("Enter patient phone:");
    String phone = sc.nextLine();
    Patients p = new Patients(pname, page, phone);
        patientsDAO.addPatients(p);


        System.out.println("Enter doctor name:");
    String dname = sc.nextLine();
        System.out.println("Enter specialization:");
    String spec = sc.nextLine();
    Doctors d = new Doctors(dname, spec);
        doctorsDAO.addDoctors(d);


        System.out.println("Enter patient ID for appointment:");
    int pid = sc.nextInt();
        System.out.println("Enter doctor ID for appointment:");
    int did = sc.nextInt();
        sc.nextLine(); // consume newline
        System.out.println("Enter appointment date (YYYY-MM-DD):");
    String dateStr = sc.nextLine();
        System.out.println("Enter notes:");
    String notes = sc.nextLine();
    Appointments a = new Appointments(pid, did, Date.valueOf(dateStr), notes);
        appointmentsDAO.bookAppointment(a);


    List<Appointments> upcoming = appointmentsDAO.getUpcomingAppointments();
        System.out.println("Upcoming appointments:");
        for (Appointments app : upcoming) {
        System.out.println(app);
    }

        sc.close();
}
}