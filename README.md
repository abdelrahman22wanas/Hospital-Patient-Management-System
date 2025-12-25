# Hospital Patient Management System

## Project Description
The Patient Management System organizes patient records and manages appointment scheduling for a hospital. This system provides efficient access and modification of patient details, ensuring smooth operations for healthcare providers.

**This is a Data Structures project** that demonstrates the practical application of various data structures and algorithms in a real-world healthcare management system.

## Features Implemented

### Core Functionalities
1. **Patient Management** - Binary Search Tree (BST) for efficient patient storage and search
2. **Appointment Scheduling** - Schedule, cancel, and reschedule appointments
3. **Waiting List** - Priority Queue implementation (older patients have higher priority)
4. **Billing System** - Track billing and payment history
5. **Report Generation** - Generate patient, appointment, and revenue reports with sorting algorithms
6. **Visit Plans (New)**
   - Create and manage planned visits per patient
   - Track status: Planned, Completed, Cancelled
   - Add Diagnosis, Treatment Plan, and Doctor Notes to each plan
   - Completing a plan automatically adds the plan date to the patient’s Visit Records
7. **CSV Import (New)**
   - Import large healthcare datasets from CSV into patients, including billing
   - Missing values are stored as `N/A`
   - Medical history auto-populated (Gender, Blood Type, Diagnosis, Doctor, Hospital, Insurance, Room, Admission Type, Discharge Date, Medication, Test Results)

### GUI Features
- ✅ **Modern JavaFX GUI** - Professional interface with sidebar navigation, tables, and forms
- ✅ **Priority Queue** - For managing patients in waiting list
- ✅ **Smart Forms** - Validation, auto-save, and date pickers
- ✅ **Real-time Search** - Filter patients as you type
- ✅ **Keyboard Shortcuts** - Full keyboard navigation support
- ✅ **Visit Plans View (New)** - Create plans, set diagnosis/treatment, save/view report, complete/cancel
- ✅ **Import CSV Button (New)** - In Reports view, one-click import of `healthcare_dataset.csv`
- ✅ **UI Polish** - Exit button stays red; asterisks removed from labels

## Project Structure
```
DATAStructure project/
├── src/                          # Source code folder
│   ├── Patient.java
│   ├── PatientBST.java
│   ├── Appointment.java
│   ├── WaitingList.java
│   ├── Billing.java
│   ├── ReportGenerator.java
│   ├── PatientManagementSystem.java
│   └── HospitalFXApp.java        # JavaFX GUI
├── target/                       # Compiled class files (Maven)
├── pom.xml                       # Maven configuration
├── .idea/                        # IntelliJ IDEA configuration
├── HospitalPatientManagementSystem.iml  # IntelliJ module file
├── run-javafx.bat                # Windows batch file to run JavaFX GUI
├── .gitignore                    # Git ignore file
└── README.md                     # This file
```

## How to Compile and Run

### Using Maven (Recommended)

1. **Install Maven** (if not already installed):
   - Download from [maven.apache.org](https://maven.apache.org/download.cgi)
   - Add to your system PATH

2. **Navigate to project directory**:
   ```bash
   cd "D:\Important\DATAStructure project"
   ```

3. **Compile and run JavaFX GUI**:
   ```bash
   mvn clean compile javafx:run
   ```

### Using Batch File (Windows)
```bash
run-javafx.bat
```
This will run the JavaFX GUI using Maven.

### Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Right-click `pom.xml` → "Add as Maven Project"
3. Build: `Build` → `Build Project` (or `Ctrl+F9`)
4. Run: Right-click `HospitalFXApp.java` → `Run 'HospitalFXApp.main()'`

## JavaFX Setup (If Maven Doesn't Work)

### Download JavaFX SDK
- Go to [openjfx.io](https://openjfx.io/)
- Download JavaFX SDK for Windows
- Extract to a folder (e.g., `C:\javafx-sdk-21.0.1`)

### Configure IntelliJ IDEA
1. `File` → `Project Structure` (`Ctrl+Alt+Shift+S`)
2. Click `Libraries` → `+` → `Java`
3. Navigate to: `C:\javafx-sdk-21.0.1\lib` → Select it → `OK`
4. In Run Configuration for `HospitalFXApp`:
   - VM options: `--module-path "C:\javafx-sdk-21.0.1\lib" --add-modules javafx.controls,javafx.fxml`

## Usage

1. Launch the application using `mvn javafx:run` or `run-javafx.bat`
2. Use the sidebar navigation to access features:
   - **View Patients**: Table with search, sort, and edit/delete actions
   - **Add Patient**: Form with validation
   - **Search Patient**: Find patients by ID
   - **Appointments**: View scheduled appointments
   - **Visit Plans (New)**:
     - Create a plan for an existing patient ID
     - Actions per plan: Set Diagnosis, Set Treatment, Save Report (Diagnosis + Treatment + Doctor Note), View Report, Complete/Cancel
     - When Completed, the plan date is added to the patient’s Visit Records
   - **Reports**:
     - Patient Report: shows patient info, Visit Records (sorted), and Clinical Summary (Diagnosis/Treatment from latest visit plan)
     - Appointment Report: sorted appointments + statistics
     - Revenue Report: outstanding totals across patients
     - Import CSV (New): import `healthcare_dataset.csv`
3. Keyboard shortcuts: Ctrl+V (View), Ctrl+A (Add), Ctrl+F (Search), Ctrl+L (Visit Plans), Ctrl+S (Save on forms), F5 (Refresh patients table)

### CSV Import
- File: `healthcare_dataset.csv` (place at project root or provide a full path)
- Path prompt appears under Reports -> Import CSV (pre-filled to the root CSV)
- On import:
  - New patients are created with generated IDs (10000+row)
  - Names normalized to Title Case, contacts set from Insurance or Hospital (or N/A)
  - Medical history populated from CSV columns
  - Discharge Date adds a Visit Records entry
  - Billing Amount added (negative values clamped to 0)
  - Missing/empty values stored as `N/A`

### Patient Report
- Sections: Patient Info, Visit Records (sorted by date), Clinical Summary
- Clinical Summary sources Diagnosis and Treatment Plan from the latest visit plan

## GUI Features

- **Professional UI**: Clean sidebar navigation with hospital theme
- **Patient Table**: Sortable columns with real-time search
- **Smart Forms**: Validation, auto-save, and date pickers
- **Responsive Design**: Adapts to different screen sizes
- **Keyboard Shortcuts**: Full keyboard navigation support

## System Requirements
- **Java JDK 11 or higher**
- **Maven** (for dependency management)
- Any operating system that supports Java
- IntelliJ IDEA (optional, for IDE support)

## Data Structures Used
- **Binary Search Tree (BST)** - For patient storage and search
  - In-order traversal implemented iteratively to prevent stack overflows on large/skewed trees (after big imports)
- **Priority Queue** - For waiting list management
- **Queue** - For appointment management
- **ArrayList** - For various collections

## Sorting Algorithms
- **Merge Sort** - Used for sorting appointments and billing records
- **Quick Sort** - Used for sorting visit records

## Performance & Stability Improvements
- Replaced recursive BST traversal with an iterative approach to avoid stack overflows after large imports
- Optimized patients table refresh by pre-building a `patientID -> appointmentDate` map (O(P + A))
- Diagnosis column now parses medical history for the first `Diagnosis:` entry (import-friendly)
- Robust error dialogs on refresh failures (View Patients)

## Notes
- Remember to add your names and IDs in the comment section at the top of each Java file before submission
- The system includes sample data for demonstration purposes
- Use Maven for easiest setup and automatic JavaFX dependency management
- Compiled classes are stored in the `target/` folder (Maven)
- The `.idea/` folder contains IntelliJ IDEA project settings

## Project Files
- **Source Code**: All `.java` files in `src/` folder
- **Compiled Classes**: `target/classes` (Maven, auto-generated)
- **IDE Configuration**: `.idea/` folder and `.iml` file for IntelliJ IDEA
- **Maven Configuration**: `pom.xml` for dependency management
