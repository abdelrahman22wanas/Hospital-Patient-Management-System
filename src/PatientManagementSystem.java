/*
 * Hospital Patient Management System
 * Course: Data Structures
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Main system class managing overall operations and interactions
 * between Patient, Appointment, WaitingList, Billing, and ReportGenerator.
 */
public class PatientManagementSystem {
    private PatientBST patientList;
    private Queue<Appointment> appointmentQueue;
    private WaitingList waitingList;
    private List<Billing> billingRecords;
    private ReportGenerator reportGenerator;
    private int nextAppointmentID;
    
    // Visit Plans
    private List<VisitPlan> visitPlans;
    private int nextVisitPlanID;
    
    public PatientManagementSystem() {
        this.patientList = new PatientBST();
        this.appointmentQueue = new LinkedList<>();
        this.waitingList = new WaitingList();
        this.billingRecords = new ArrayList<>();
        this.reportGenerator = new ReportGenerator();
        this.nextAppointmentID = 1;
        
        // Initialize visit plans
        this.visitPlans = new ArrayList<>();
        this.nextVisitPlanID = 1;
    }
    
    /**
     * Adds a new patient to the system.
     */
    public boolean addPatient(int patientID, String name, int age, String contactInfo) {
        if (patientList.search(patientID) != null) {
            return false; // Patient already exists
        }
        Patient patient = new Patient(patientID, name, age, contactInfo);
        patientList.insert(patient);
        // Create billing record for new patient
        billingRecords.add(new Billing(patientID));
        return true;
    }
    
    /**
     * Finds a patient by ID.
     */
    public Patient findPatient(int patientID) {
        return patientList.search(patientID);
    }
    
    /**
     * Gets all patients.
     */
    public List<Patient> getAllPatients() {
        return patientList.getAllPatients();
    }
    
    /**
     * Schedules an appointment for a patient.
     */
    public Appointment scheduleAppointment(int patientID, String date, String time) {
        Patient patient = findPatient(patientID);
        if (patient == null) {
            return null; // Patient not found
        }
        
        Appointment appointment = new Appointment(nextAppointmentID++, patient, date, time);
        appointment.schedule();
        appointmentQueue.offer(appointment);
        return appointment;
    }
    
    /**
     * Cancels an appointment.
     */
    public boolean cancelAppointment(int appointmentID) {
        for (Appointment appointment : appointmentQueue) {
            if (appointment.getAppointmentID() == appointmentID) {
                appointment.cancel();
                return true;
            }
        }
        return false;
    }
    
    /**
     * Reschedules an appointment.
     */
    public boolean rescheduleAppointment(int appointmentID, String newDate, String newTime) {
        for (Appointment appointment : appointmentQueue) {
            if (appointment.getAppointmentID() == appointmentID) {
                appointment.reschedule(newDate, newTime);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets all appointments.
     */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointmentQueue);
    }
    
    /**
     * Adds a patient to the waiting list.
     */
    public void addToWaitingList(int patientID) {
        Patient patient = findPatient(patientID);
        if (patient != null) {
            waitingList.addToWaitList(patient);
        }
    }
    
    /**
     * Removes a patient from the waiting list.
     */
    public Patient removeFromWaitingList() {
        return waitingList.removeFromWaitList();
    }
    
    /**
     * Gets all waiting patients.
     */
    public List<Patient> getWaitingPatients() {
        return waitingList.getAllWaitingPatients();
    }
    
    /**
     * Generates a bill for a patient.
     */
    public boolean generateBill(int patientID, double amount) {
        Billing billing = getBilling(patientID);
        if (billing != null) {
            billing.generateBill(amount);
            return true;
        }
        return false;
    }
    
    /**
     * Adds a payment for a patient.
     */
    public boolean addPayment(int patientID, double amount, String date) {
        Billing billing = getBilling(patientID);
        if (billing != null) {
            billing.addPayment(amount, date);
            return true;
        }
        return false;
    }
    
    /**
     * Gets billing record for a patient.
     */
    public Billing getBilling(int patientID) {
        for (Billing billing : billingRecords) {
            if (billing.getPatientID() == patientID) {
                return billing;
            }
        }
        return null;
    }
    
    /**
     * Gets all billing records.
     */
    public List<Billing> getAllBillingRecords() {
        return billingRecords;
    }
    
    /**
     * Generates a patient report.
     */
    public String generatePatientReport(int patientID) {
        Patient patient = findPatient(patientID);
        List<VisitPlan> plans = getVisitPlansForPatient(patientID);
        return reportGenerator.generatePatientReport(patient, plans);
    }
    
    /**
     * Generates an appointment report.
     */
    public String generateAppointmentReport() {
        return reportGenerator.generateAppointmentReport(getAllAppointments());
    }
    
    /**
     * Generates a revenue report.
     */
    public String generateRevenueReport() {
        return reportGenerator.generateRevenueReport(billingRecords);
    }
    
    /**
     * Generates a general report based on type.
     */
    public String generateReport(String reportType, int patientID) {
        switch (reportType.toLowerCase()) {
            case "patient":
                return generatePatientReport(patientID);
            case "appointment":
                return generateAppointmentReport();
            case "revenue":
                return generateRevenueReport();
            default:
                return "Invalid report type.";
        }
    }

    // Visit Plan management
    public VisitPlan createVisitPlan(int patientID, String date, String purpose, String doctor) {
        Patient patient = findPatient(patientID);
        if (patient == null) {
            return null;
        }
        VisitPlan plan = new VisitPlan(nextVisitPlanID++, patient, date, purpose, doctor);
        plan.markPlanned();
        visitPlans.add(plan);
        return plan;
    }
    
    public List<VisitPlan> getAllVisitPlans() {
        return visitPlans;
    }
    
    public List<VisitPlan> getVisitPlansForPatient(int patientID) {
        List<VisitPlan> result = new ArrayList<>();
        for (VisitPlan vp : visitPlans) {
            if (vp.getPatient() != null && vp.getPatient().getPatientID() == patientID) {
                result.add(vp);
            }
        }
        return result;
    }
    
    public boolean setVisitPlanStatus(int planID, String status) {
        VisitPlan plan = findVisitPlan(planID);
        if (plan == null) return false;
        if (status == null) return false;
        switch (status) {
            case "Planned":
                plan.markPlanned();
                break;
            case "Completed":
                plan.markCompleted();
                // Add visit date to patient's visit records
                if (plan.getPatient() != null) {
                    String record = plan.getDate() != null ? plan.getDate() : "";
                    if (record != null && !record.isEmpty()) {
                        plan.getPatient().addVisitRecord(record);
                    }
                }
                break;
            case "Cancelled":
                plan.markCancelled();
                break;
            default:
                plan.setStatus(status);
        }
        return true;
    }
    
    private VisitPlan findVisitPlan(int planID) {
        for (VisitPlan vp : visitPlans) {
            if (vp.getPlanID() == planID) return vp;
        }
        return null;
    }

    // Visit Plan report updates
    public boolean updateVisitPlanReport(int planID, String diagnosis, String treatmentPlan, String doctorNote) {
        VisitPlan vp = findVisitPlan(planID);
        if (vp == null) return false;
        if (diagnosis != null) vp.setDiagnosis(diagnosis);
        if (treatmentPlan != null) vp.setTreatmentPlan(treatmentPlan);
        if (doctorNote != null) vp.setDoctorNote(doctorNote);
        return true;
    }

    public String getVisitPlanFormattedReport(int planID) {
        VisitPlan vp = findVisitPlan(planID);
        return vp != null ? vp.getFormattedReport() : "Visit plan not found.";
    }

    // Helpers to access plan report fields
    public String getVisitPlanDiagnosis(int planID) {
        VisitPlan vp = findVisitPlan(planID);
        return vp != null ? vp.getDiagnosis() : null;
    }

    public String getVisitPlanTreatmentPlan(int planID) {
        VisitPlan vp = findVisitPlan(planID);
        return vp != null ? vp.getTreatmentPlan() : null;
    }
}

