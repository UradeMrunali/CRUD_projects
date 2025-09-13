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
