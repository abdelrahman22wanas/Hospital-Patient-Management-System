/*
 * Hospital Patient Management System
 * Course: Data Structures
 * 
 * Report Generator with Merge Sort and Quick Sort algorithms
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Generates various reports for the hospital system.
 * Uses sorting algorithms (Merge Sort and Quick Sort) for organizing data.
 */
public class ReportGenerator {
    private String reportType;
    private Object data;
    
    public ReportGenerator() {
        this.reportType = "";
        this.data = null;
    }
    
    /**
     * Generates a patient report with sorted visit history.
     */
    public String generatePatientReport(Patient patient) {
        if (patient == null) {
            return "Patient not found.";
        }
        
        StringBuilder report = new StringBuilder();
        report.append("=== PATIENT REPORT ===\n");
        report.append(patient.getPatientInfo());
        report.append("\nVisit Records (Sorted by Date):\n");
        
        // Sort visit records using Quick Sort
        List<String> sortedVisits = new ArrayList<>(patient.getVisitRecords());
        quickSort(sortedVisits, 0, sortedVisits.size() - 1);
        
        for (String visit : sortedVisits) {
            report.append("- ").append(visit).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Generates an appointment report with sorted appointments.
     */
    public String generateAppointmentReport(List<Appointment> appointments) {
        StringBuilder report = new StringBuilder();
        report.append("=== APPOINTMENT REPORT ===\n");
        report.append("Total Appointments: ").append(appointments.size()).append("\n\n");
        
        // Sort appointments by date using Merge Sort
        List<Appointment> sortedAppointments = new ArrayList<>(appointments);
        mergeSortAppointments(sortedAppointments, 0, sortedAppointments.size() - 1);
        
        report.append("Appointments (Sorted by Date):\n");
        for (Appointment appointment : sortedAppointments) {
            report.append(appointment.toString()).append("\n");
        }
        
        // Statistics
        long scheduled = sortedAppointments.stream().filter(a -> a.getStatus().equals("Scheduled")).count();
        long completed = sortedAppointments.stream().filter(a -> a.getStatus().equals("Completed")).count();
        long cancelled = sortedAppointments.stream().filter(a -> a.getStatus().equals("Cancelled")).count();
        
        report.append("\nStatistics:\n");
        report.append("Scheduled: ").append(scheduled).append("\n");
        report.append("Completed: ").append(completed).append("\n");
        report.append("Cancelled: ").append(cancelled).append("\n");
        
        return report.toString();
    }
    
    /**
     * Generates a revenue report with sorted billing records.
     */
    public String generateRevenueReport(List<Billing> billingRecords) {
        StringBuilder report = new StringBuilder();
        report.append("=== REVENUE REPORT ===\n");
        report.append("Total Patients with Billing: ").append(billingRecords.size()).append("\n\n");
        
        // Sort billing records by amount using Merge Sort
        List<Billing> sortedBilling = new ArrayList<>(billingRecords);
        mergeSortBilling(sortedBilling, 0, sortedBilling.size() - 1);
        
        double totalRevenue = 0;
        report.append("Billing Records (Sorted by Amount):\n");
        for (Billing billing : sortedBilling) {
            report.append(billing.toString()).append("\n");
            totalRevenue += billing.getBillingAmount();
        }
        
        report.append("\nTotal Outstanding Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        
        return report.toString();
    }
    
    /**
     * Quick Sort implementation for sorting strings.
     */
    private void quickSort(List<String> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }
    
    private int partition(List<String> list, int low, int high) {
        String pivot = list.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++;
                String temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        
        String temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        
        return i + 1;
    }
    
    /**
     * Merge Sort implementation for sorting appointments by date.
     */
    private void mergeSortAppointments(List<Appointment> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortAppointments(list, left, mid);
            mergeSortAppointments(list, mid + 1, right);
            mergeAppointments(list, left, mid, right);
        }
    }
    
    private void mergeAppointments(List<Appointment> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        List<Appointment> leftList = new ArrayList<>();
        List<Appointment> rightList = new ArrayList<>();
        
        for (int i = 0; i < n1; i++) {
            leftList.add(list.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(list.get(mid + 1 + j));
        }
        
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            if (leftList.get(i).getDate().compareTo(rightList.get(j).getDate()) <= 0) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }
        
        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
    
    /**
     * Merge Sort implementation for sorting billing records by amount.
     */
    private void mergeSortBilling(List<Billing> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortBilling(list, left, mid);
            mergeSortBilling(list, mid + 1, right);
            mergeBilling(list, left, mid, right);
        }
    }
    
    private void mergeBilling(List<Billing> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        List<Billing> leftList = new ArrayList<>();
        List<Billing> rightList = new ArrayList<>();
        
        for (int i = 0; i < n1; i++) {
            leftList.add(list.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            rightList.add(list.get(mid + 1 + j));
        }
        
        int i = 0, j = 0, k = left;
        
        while (i < n1 && j < n2) {
            if (leftList.get(i).getBillingAmount() >= rightList.get(j).getBillingAmount()) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }
        
        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }
        
        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
}

