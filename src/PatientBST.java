/*
 * Hospital Patient Management System
 * Course: Data Structures
 * 
 * Binary Search Tree implementation for Patient storage
 */

import java.util.ArrayList;
import java.util.List;

/**
 * Binary Search Tree for storing and searching patients efficiently.
 */
public class PatientBST {
    private Patient root;
    
    public PatientBST() {
        this.root = null;
    }
    
    /**
     * Inserts a patient into the BST.
     */
    public void insert(Patient patient) {
        root = insertRec(root, patient);
    }
    
    private Patient insertRec(Patient root, Patient patient) {
        if (root == null) {
            return patient;
        }
        
        if (patient.getPatientID() < root.getPatientID()) {
            root.setLeft(insertRec(root.getLeft(), patient));
        } else if (patient.getPatientID() > root.getPatientID()) {
            root.setRight(insertRec(root.getRight(), patient));
        }
        
        return root;
    }
    
    /**
     * Searches for a patient by ID.
     */
    public Patient search(int patientID) {
        return searchRec(root, patientID);
    }
    
    private Patient searchRec(Patient root, int patientID) {
        if (root == null || root.getPatientID() == patientID) {
            return root;
        }
        
        if (patientID < root.getPatientID()) {
            return searchRec(root.getLeft(), patientID);
        }
        
        return searchRec(root.getRight(), patientID);
    }
    
    /**
     * Gets all patients in the BST (in-order traversal).
     */
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        inOrderTraversal(root, patients);
        return patients;
    }
    
    private void inOrderTraversal(Patient root, List<Patient> patients) {
        if (root != null) {
            inOrderTraversal(root.getLeft(), patients);
            patients.add(root);
            inOrderTraversal(root.getRight(), patients);
        }
    }
    
    /**
     * Deletes a patient from the BST.
     */
    public void delete(int patientID) {
        root = deleteRec(root, patientID);
    }
    
    private Patient deleteRec(Patient root, int patientID) {
        if (root == null) {
            return root;
        }
        
        if (patientID < root.getPatientID()) {
            root.setLeft(deleteRec(root.getLeft(), patientID));
        } else if (patientID > root.getPatientID()) {
            root.setRight(deleteRec(root.getRight(), patientID));
        } else {
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }
            
            Patient minNode = findMin(root.getRight());
            root.setPatientID(minNode.getPatientID());
            root.setName(minNode.getName());
            root.setAge(minNode.getAge());
            root.setContactInfo(minNode.getContactInfo());
            root.setRight(deleteRec(root.getRight(), minNode.getPatientID()));
        }
        
        return root;
    }
    
    private Patient findMin(Patient root) {
        while (root.getLeft() != null) {
            root = root.getLeft();
        }
        return root;
    }
    
    public boolean isEmpty() {
        return root == null;
    }
}

