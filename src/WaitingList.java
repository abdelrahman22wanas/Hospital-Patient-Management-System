/*
 * Hospital Patient Management System
 * Course: Data Structures
 * 
 * Priority Queue implementation for managing waiting list
 */

import java.util.PriorityQueue;

/**
 * Manages the queue of patients waiting for available appointments.
 * Uses Priority Queue to prioritize patients (by age - older patients first).
 */
public class WaitingList {
    private PriorityQueue<WaitingPatient> waitingQueue;
    
    public WaitingList() {
        // Priority queue with custom comparator - older patients have higher priority
        this.waitingQueue = new PriorityQueue<>((p1, p2) -> {
            // Higher age = higher priority (negative for reverse order)
            return Integer.compare(p2.getPatient().getAge(), p1.getPatient().getAge());
        });
    }
    
    /**
     * Adds a patient to the waiting list with priority.
     */
    public void addToWaitList(Patient patient, int priority) {
        WaitingPatient waitingPatient = new WaitingPatient(patient, priority);
        waitingQueue.offer(waitingPatient);
    }
    
    /**
     * Adds a patient to the waiting list (priority based on age).
     */
    public void addToWaitList(Patient patient) {
        WaitingPatient waitingPatient = new WaitingPatient(patient, patient.getAge());
        waitingQueue.offer(waitingPatient);
    }
    
    /**
     * Removes and returns the highest priority patient from the waiting list.
     */
    public Patient removeFromWaitList() {
        WaitingPatient waitingPatient = waitingQueue.poll();
        return waitingPatient != null ? waitingPatient.getPatient() : null;
    }
    
    /**
     * Peeks at the highest priority patient without removing.
     */
    public Patient peekNext() {
        WaitingPatient waitingPatient = waitingQueue.peek();
        return waitingPatient != null ? waitingPatient.getPatient() : null;
    }
    
    /**
     * Checks if the waiting list is empty.
     */
    public boolean isEmpty() {
        return waitingQueue.isEmpty();
    }
    
    /**
     * Gets the size of the waiting list.
     */
    public int size() {
        return waitingQueue.size();
    }
    
    /**
     * Gets all patients in the waiting list (for display purposes).
     */
    public java.util.List<Patient> getAllWaitingPatients() {
        java.util.List<Patient> patients = new java.util.ArrayList<>();
        for (WaitingPatient wp : waitingQueue) {
            patients.add(wp.getPatient());
        }
        return patients;
    }
    
    /**
     * Inner class to represent a patient in the waiting list with priority.
     */
    private static class WaitingPatient {
        private Patient patient;
        private int priority;
        
        public WaitingPatient(Patient patient, int priority) {
            this.patient = patient;
            this.priority = priority;
        }
        
        public Patient getPatient() {
            return patient;
        }
        
        public int getPriority() {
            return priority;
        }
    }
}

