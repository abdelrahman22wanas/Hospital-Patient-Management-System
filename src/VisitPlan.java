/*
 * Hospital Patient Management System
 * Course: Data Structures
 * 
 * Visit Plan - represents planned future visits for a patient
 */

/**
 * Represents a planned visit in the hospital system.
 */
public class VisitPlan {
    private int planID;
    private Patient patient;
    private String date;      // YYYY-MM-DD
    private String purpose;   // Reason for visit
    private String doctor;    // Assigned doctor (optional)
    private String status;    // Planned, Completed, Cancelled

    // Report fields
    private String diagnosis;       // Diagnosis given during the visit
    private String treatmentPlan;   // Treatment plan prescribed
    private String doctorNote;      // Additional doctor's note

    public VisitPlan(int planID, Patient patient, String date, String purpose, String doctor) {
        this.planID = planID;
        this.patient = patient;
        this.date = date;
        this.purpose = purpose != null ? purpose : "";
        this.doctor = doctor != null ? doctor : "";
        this.status = "Planned";
        this.diagnosis = "";
        this.treatmentPlan = "";
        this.doctorNote = "";
    }

    // Getters and setters
    public int getPlanID() { return planID; }
    public void setPlanID(int planID) { this.planID = planID; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Report fields getters/setters
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis != null ? diagnosis : ""; }

    public String getTreatmentPlan() { return treatmentPlan; }
    public void setTreatmentPlan(String treatmentPlan) { this.treatmentPlan = treatmentPlan != null ? treatmentPlan : ""; }

    public String getDoctorNote() { return doctorNote; }
    public void setDoctorNote(String doctorNote) { this.doctorNote = doctorNote != null ? doctorNote : ""; }

    public String getFormattedReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== VISIT REPORT ===\n");
        sb.append("Plan ID: ").append(planID).append("\n");
        sb.append("Patient: ").append(patient != null ? patient.getName() : "N/A").append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Doctor: ").append(doctor).append("\n\n");
        sb.append("Diagnosis: ").append(diagnosis == null || diagnosis.isEmpty() ? "N/A" : diagnosis).append("\n\n");
        sb.append("Treatment Plan:\n").append(treatmentPlan == null || treatmentPlan.isEmpty() ? "N/A" : treatmentPlan).append("\n\n");
        sb.append("Doctor Notes:\n").append(doctorNote == null || doctorNote.isEmpty() ? "N/A" : doctorNote).append("\n");
        return sb.toString();
    }

    // Status helpers
    public void markPlanned() { this.status = "Planned"; }
    public void markCompleted() { this.status = "Completed"; }
    public void markCancelled() { this.status = "Cancelled"; }

    @Override
    public String toString() {
        return "VisitPlan{" +
                "ID=" + planID +
                ", Patient=" + (patient != null ? patient.getName() : "N/A") +
                ", Date=" + date +
                ", Purpose=" + purpose +
                ", Doctor=" + doctor +
                ", Status=" + status +
                ", Diagnosis=" + (diagnosis != null ? diagnosis : "") +
                ", TreatmentPlan=" + (treatmentPlan != null ? treatmentPlan : "") +
                '}';
    }
}
