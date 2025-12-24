/*
 * Hospital Patient Management System
 * Course: Data Structures
 */

/**
 * Represents an appointment in the hospital system.
 */
public class Appointment {
    private int appointmentID;
    private Patient patient;
    private String date;
    private String time;
    private String status; // Scheduled, Completed, Cancelled
    
    public Appointment(int appointmentID, Patient patient, String date, String time) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.date = date;
        this.time = time;
        this.status = "Scheduled";
    }
    
    // Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }
    
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Schedules the appointment.
     */
    public void schedule() {
        this.status = "Scheduled";
    }
    
    /**
     * Cancels the appointment.
     */
    public void cancel() {
        this.status = "Cancelled";
    }
    
    /**
     * Reschedules the appointment to a new date and time.
     */
    public void reschedule(String newDate, String newTime) {
        this.date = newDate;
        this.time = newTime;
        this.status = "Scheduled";
    }
    
    @Override
    public String toString() {
        return "Appointment{ID=" + appointmentID + ", Patient=" + 
               (patient != null ? patient.getName() : "N/A") + 
               ", Date=" + date + ", Time=" + time + ", Status=" + status + "}";
    }
}

