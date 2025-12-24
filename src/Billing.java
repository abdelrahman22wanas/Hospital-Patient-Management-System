/*
 * Hospital Patient Management System
 * Course: Data Structures
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks billing and payment history for each patient.
 */
public class Billing {
    private int patientID;
    private double billingAmount;
    private List<Payment> paymentHistory;
    
    public Billing(int patientID) {
        this.patientID = patientID;
        this.billingAmount = 0.0;
        this.paymentHistory = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getPatientID() {
        return patientID;
    }
    
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    
    public double getBillingAmount() {
        return billingAmount;
    }
    
    public void setBillingAmount(double billingAmount) {
        this.billingAmount = billingAmount;
    }
    
    public List<Payment> getPaymentHistory() {
        return paymentHistory;
    }
    
    /**
     * Generates a bill for the patient.
     */
    public void generateBill(double amount) {
        this.billingAmount += amount;
    }
    
    /**
     * Adds a payment to the payment history.
     */
    public void addPayment(double amount, String date) {
        Payment payment = new Payment(amount, date);
        paymentHistory.add(payment);
        this.billingAmount -= amount;
        if (this.billingAmount < 0) {
            this.billingAmount = 0; // No negative balance
        }
    }
    
    /**
     * Gets the payment status of the patient.
     */
    public String getPaymentStatus() {
        if (billingAmount == 0) {
            return "Paid";
        } else if (billingAmount > 0) {
            return "Pending: $" + String.format("%.2f", billingAmount);
        } else {
            return "Overpaid";
        }
    }
    
    /**
     * Inner class to represent a payment.
     */
    public static class Payment {
        private double amount;
        private String date;
        
        public Payment(double amount, String date) {
            this.amount = amount;
            this.date = date;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public String getDate() {
            return date;
        }
        
        @Override
        public String toString() {
            return "Payment{Amount=$" + String.format("%.2f", amount) + ", Date=" + date + "}";
        }
    }
    
    @Override
    public String toString() {
        return "Billing{PatientID=" + patientID + ", Amount=$" + 
               String.format("%.2f", billingAmount) + ", Status=" + getPaymentStatus() + "}";
    }
}

