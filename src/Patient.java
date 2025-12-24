/*
 * Hospital Patient Management System
 * Course: Data Structures
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient in the hospital system.
 * Patient data is stored in a Binary Search Tree structure.
 */
public class Patient implements Comparable<Patient> {
    private int patientID;
    private String name;
    private int age;
    private String contactInfo;
    private List<String> medicalHistory;
    private List<String> visitRecords;
    
    // BST pointers
    private Patient left;
    private Patient right;
    
    public Patient(int patientID, String name, int age, String contactInfo) {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.contactInfo = contactInfo;
        this.medicalHistory = new ArrayList<>();
        this.visitRecords = new ArrayList<>();
        this.left = null;
        this.right = null;
    }
    
    // Getters and Setters
    public int getPatientID() {
        return patientID;
    }
    
    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public String getContactInfo() {
        return contactInfo;
    }
    
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    
    public List<String> getMedicalHistory() {
        return medicalHistory;
    }
    
    public List<String> getVisitRecords() {
        return visitRecords;
    }
    
    public Patient getLeft() {
        return left;
    }
    
    public void setLeft(Patient left) {
        this.left = left;
    }
    
    public Patient getRight() {
        return right;
    }
    
    public void setRight(Patient right) {
        this.right = right;
    }
    
    /**
     * Updates the contact information of the patient.
     */
    public void updateContactInfo(String newContactInfo) {
        this.contactInfo = newContactInfo;
    }
    
    /**
     * Adds a visit record to the patient's history.
     */
    public void addVisitRecord(String visitRecord) {
        this.visitRecords.add(visitRecord);
    }
    
    /**
     * Gets comprehensive patient information.
     */
    public String getPatientInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Patient ID: ").append(patientID).append("\n");
        info.append("Name: ").append(name).append("\n");
        info.append("Age: ").append(age).append("\n");
        info.append("Contact Info: ").append(contactInfo).append("\n");
        info.append("Medical History: ").append(medicalHistory).append("\n");
        info.append("Visit Records: ").append(visitRecords).append("\n");
        return info.toString();
    }
    
    /**
     * Compares patients by ID for BST and Priority Queue operations.
     */
    @Override
    public int compareTo(Patient other) {
        return Integer.compare(this.patientID, other.patientID);
    }
    
    @Override
    public String toString() {
        return "Patient{ID=" + patientID + ", Name=" + name + ", Age=" + age + "}";
    }
}

