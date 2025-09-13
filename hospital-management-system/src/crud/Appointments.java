package crud;

import java.sql.Date;

public class Appointments {
    private int id;
    private int patientId;
    private int doctorId;
    private Date date;
    private String notes;

    public Appointments() {}

    public Appointments(int id, int patientId, int doctorId, Date date, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.notes = notes;
    }

    public Appointments(int patientId, int doctorId, Date date, String notes) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointments{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", date=" + date +
                ", notes='" + notes + '\'' +
                '}';
    }
}
