/*
 * CSV Importer for healthcare dataset
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvImporter {

    public static int importHealthcareCsv(PatientManagementSystem system, String csvPath) throws IOException {
        Path path = Paths.get(csvPath);
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String headerLine = br.readLine();
            if (headerLine == null) return 0;
            List<String> headers = parseCsvLine(headerLine);
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                idx.put(headers.get(i).trim().toLowerCase(), i);
            }

            int created = 0;
            int autoIdBase = 10000;
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                row++;
                if (line.trim().isEmpty()) continue;
                List<String> cols = parseCsvLine(line);

                String name = get(cols, idx, "name");
                if (name == null || name.trim().isEmpty()) name = "N/A";
                String ageStr = get(cols, idx, "age");
                int age = safeParseInt(ageStr, 30);
                if (age < 1 || age > 120) age = Math.min(Math.max(age, 1), 120);

                String gender = defaultNA(get(cols, idx, "gender"));
                String bloodType = defaultNA(get(cols, idx, "blood type"));
                String medCondition = defaultNA(get(cols, idx, "medical condition"));
                String doctor = defaultNA(get(cols, idx, "doctor"));
                String hospital = defaultNA(get(cols, idx, "hospital"));
                String insurance = defaultNA(get(cols, idx, "insurance provider"));
                String billingAmountStr = get(cols, idx, "billing amount");
                double billingAmount = safeParseDouble(billingAmountStr, 0.0);
                if (billingAmount < 0) billingAmount = 0.0;
                String roomNumber = defaultNA(get(cols, idx, "room number"));
                String admissionType = defaultNA(get(cols, idx, "admission type"));
                String dischargeDate = defaultNA(get(cols, idx, "discharge date"));
                String medication = defaultNA(get(cols, idx, "medication"));
                String testResults = defaultNA(get(cols, idx, "test results"));

                // Generate unique patient ID if needed
                int pid = autoIdBase + row;
                while (system.findPatient(pid) != null) {
                    pid++;
                }

                String contact = insurance.equals("N/A") ? (hospital.equals("N/A") ? "N/A" : hospital) : insurance;
                boolean added = system.addPatient(pid, normalizeName(name), age, contact);
                if (!added) {
                    continue; // skip duplicates
                }
                created++;

                Patient p = system.findPatient(pid);
                if (p != null) {
                    p.getMedicalHistory().add("Gender: " + gender);
                    p.getMedicalHistory().add("Blood Type: " + bloodType);
                    p.getMedicalHistory().add("Diagnosis: " + medCondition);
                    p.getMedicalHistory().add("Doctor: " + doctor);
                    p.getMedicalHistory().add("Hospital: " + hospital);
                    p.getMedicalHistory().add("Insurance: " + insurance);
                    p.getMedicalHistory().add("Room Number: " + roomNumber);
                    p.getMedicalHistory().add("Admission Type: " + admissionType);
                    p.getMedicalHistory().add("Discharge Date: " + dischargeDate);
                    p.getMedicalHistory().add("Medication: " + medication);
                    p.getMedicalHistory().add("Test Results: " + testResults);

                    // Add visit record based on discharge date if exists
                    String visitRecord = ("N/A".equals(dischargeDate) ? "N/A" : (dischargeDate + " - Discharge"))
                            + ("N/A".equals(admissionType) ? "" : (" (" + admissionType + ")"));
                    if (!visitRecord.equals("N/A")) {
                        p.addVisitRecord(visitRecord);
                    }
                }

                // Billing
                system.generateBill(pid, billingAmount);
            }
            return created;
        }
    }

    private static String get(List<String> cols, Map<String, Integer> idx, String key) {
        if (key == null) return null;
        Integer i = idx.get(key.toLowerCase());
        if (i == null) return null;
        if (i < 0 || i >= cols.size()) return null;
        String v = cols.get(i);
        return v != null ? v.trim() : null;
    }

    private static String defaultNA(String s) {
        if (s == null) return "N/A";
        String t = s.trim();
        return t.isEmpty() ? "N/A" : t;
    }

    private static int safeParseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }
    private static double safeParseDouble(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }

    private static String normalizeName(String name) {
        String[] parts = name.trim().toLowerCase().split("\\s+");
        List<String> out = new ArrayList<>();
        for (String part : parts) {
            if (part.isEmpty()) continue;
            char first = Character.toUpperCase(part.charAt(0));
            String rest = part.length() > 1 ? part.substring(1) : "";
            out.add(first + rest);
        }
        return String.join(" ", out);
    }

    // Simple CSV parser that respects quotes
    private static List<String> parseCsvLine(String line) {
        List<String> res = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    // Escaped quote
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                res.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        res.add(cur.toString());
        return res;
    }
}
