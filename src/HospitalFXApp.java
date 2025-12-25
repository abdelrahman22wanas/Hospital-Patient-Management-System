/*
 * Hospital Patient Management System
 * Course: Data Structures
 * 
 * Modern JavaFX GUI Application
 * Features: Navigation, Forms, Tables, Search, Validation, Animations
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Modern JavaFX Application for Hospital Patient Management System
 */
public class HospitalFXApp extends Application {
    private PatientManagementSystem system;
    private Stage primaryStage;
    
    // UI Components
    private BorderPane root;
    private VBox sidebar;
    private StackPane contentArea;
    
    // Patient Table
    private TableView<PatientTableModel> patientTable;
    private ObservableList<PatientTableModel> patientData;
    private FilteredList<PatientTableModel> filteredData;
    private TextField searchField;
    
    // Form Fields
    private TextField patientIDField;
    private TextField nameField;
    private TextField ageField;
    private TextField contactField;
    private ComboBox<String> diagnosisCombo;
    private DatePicker appointmentDatePicker;
    private TextArea medicalHistoryArea;
    
    // Current view
    private String currentView = "view";
    
    // Edit mode state
    private boolean isEditMode = false;
    private int editingPatientID = -1;
    
    // Hospital color scheme
    private static final String PRIMARY_BLUE = "#2C5F8D";
    private static final String LIGHT_BLUE = "#4A90E2";
    private static final String WHITE = "#FFFFFF";
    private static final String LIGHT_GRAY = "#F5F5F5";
    private static final String DARK_GRAY = "#333333";
    private static final String SUCCESS_GREEN = "#4CAF50";
    private static final String ERROR_RED = "#F44336";
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.system = new PatientManagementSystem();
        
        // Initialize UI
        createUI();
        loadSampleData();
        
        // Show initial view
        showViewPatients();
        
        // Set up stage
        primaryStage.setTitle("🏥 Hospital Patient Management System");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
        
        // Setup keyboard shortcuts after scene is set
        setupKeyboardShortcuts();
        
        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }
    
    /**
     * Creates the main UI structure
     */
    private void createUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: " + LIGHT_GRAY + ";");
        
        // Create sidebar navigation
        createSidebar();
        
        // Create content area
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));
        
        root.setLeft(sidebar);
        root.setCenter(contentArea);
    }
    
    /**
     * Creates the navigation sidebar
     */
    private void createSidebar() {
        sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(15);
        sidebar.setStyle("-fx-background-color: " + PRIMARY_BLUE + ";");
        sidebar.setPrefWidth(220);
        
        // Logo/Title
        Label title = new Label("Hospital\nManagement");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);
        title.setPadding(new Insets(0, 0, 20, 0));
        
        // Navigation buttons
        Button viewPatientsBtn = createNavButton("View Patients", "view");
        Button addPatientBtn = createNavButton("Add Patient", "add");
        Button searchPatientBtn = createNavButton("Search Patient", "search");
        Button appointmentsBtn = createNavButton("Appointments", "appointments");
        Button visitPlansBtn = createNavButton("Visit Plans", "visitplans");
        Button reportsBtn = createNavButton("Reports", "reports");
        
        Button exitBtn = createNavButton("Exit", "exit");
        exitBtn.setStyle("-fx-background-color: " + ERROR_RED + "; -fx-background-radius: 8;");
        
        // Add components to sidebar
        sidebar.getChildren().addAll(
            title,
            viewPatientsBtn,
            addPatientBtn,
            searchPatientBtn,
            appointmentsBtn,
            visitPlansBtn,
            reportsBtn,
            new Separator(),
            exitBtn
        );
        
        // Add spacer to push exit button down
        VBox.setVgrow(exitBtn, Priority.ALWAYS);
    }
    
    /**
     * Creates a navigation button with styling and animations
     */
    private Button createNavButton(String text, String view) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(45);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(
            "-fx-background-color: " + LIGHT_BLUE + ";" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        btn.setOnMouseEntered(e -> {
            if ("Exit".equals(text)) {
                btn.setStyle(
                    "-fx-background-color: " + ERROR_RED + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;"
                );
            } else {
                btn.setStyle(
                    "-fx-background-color: #5BA3E8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;"
                );
            }
            addScaleAnimation(btn, 1.05);
        });
        
        btn.setOnMouseExited(e -> {
            if ("Exit".equals(text)) {
                btn.setStyle(
                    "-fx-background-color: " + ERROR_RED + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;"
                );
            } else {
                btn.setStyle(
                    "-fx-background-color: " + LIGHT_BLUE + ";" +
                    "-fx-background-radius: 8;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-cursor: hand;"
                );
            }
            addScaleAnimation(btn, 1.0);
        });
        
        // Click action
        btn.setOnAction(e -> {
            if ("exit".equals(view)) {
                Platform.exit();
            } else {
                switchView(view);
            }
        });
        
        // Tooltip
        String tooltipText = getTooltipText(view);
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle("-fx-font-size: 12px;");
        btn.setTooltip(tooltip);
        
        return btn;
    }
    
    private String getTooltipText(String view) {
        switch (view) {
            case "view": return "View all patients (Ctrl+V)";
            case "add": return "Add a new patient (Ctrl+A)";
            case "search": return "Search patients (Ctrl+F)";
            case "appointments": return "Manage appointments (Ctrl+P)";
            case "visitplans": return "Manage visit plans (Ctrl+L)";
            case "reports": return "View reports (Ctrl+R)";
            default: return "";
        }
    }
    
    /**
     * Switches between different views
     */
    private void switchView(String view) {
        currentView = view;
        
        switch (view) {
            case "view":
                showViewPatients();
                break;
            case "add":
                // Enter add mode explicitly
                isEditMode = false;
                editingPatientID = -1;
                showAddPatient();
                break;
            case "search":
                showSearchPatient();
                break;
            case "appointments":
                showAppointments();
                break;
            case "visitplans":
                showVisitPlans();
                break;
            case "reports":
                showReports();
                break;
        }
    }
    
    /**
     * Shows the view patients screen with table and search
     */
    private void showViewPatients() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        
        // Title
        Label title = createTitle("View All Patients");
        
        // Search bar
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        
        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        
        searchField = new TextField();
        searchField.setPromptText("Search by name, ID, or diagnosis...");
        searchField.setPrefWidth(400);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 8px;" +
            "-fx-font-size: 14px;"
        );
        
        // Auto-suggestions (filter as you type)
        // setupSearchFilter();  // Moved after createPatientTable
        
        searchBox.getChildren().addAll(searchLabel, searchField);
        
        // Create patient table
        createPatientTable();
        
        // Setup search filter after table is created
        setupSearchFilter();
        
        // Refresh button
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(
            "-fx-background-color: " + LIGHT_BLUE + ";" +
            "-fx-background-radius: 5;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 20px;"
        );
        refreshBtn.setOnAction(e -> refreshTable());
        refreshBtn.setTooltip(new Tooltip("Refresh the patient list (F5)"));
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(refreshBtn);
        
        container.getChildren().addAll(title, searchBox, patientTable, buttonBox);
        VBox.setVgrow(patientTable, Priority.ALWAYS);
        
        // Animate content change
        animateContentChange(container);
    }
    
    /**
     * Sets up search filtering with auto-suggestions
     */
    private void setupSearchFilter() {
        filteredData = new FilteredList<>(patientData, p -> true);
        
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patient -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                String lowerCaseFilter = newValue.toLowerCase();
                
                // Search in name, ID, and diagnosis
                return patient.getName().toLowerCase().contains(lowerCaseFilter) ||
                       String.valueOf(patient.getPatientID()).contains(lowerCaseFilter) ||
                       patient.getDiagnosis().toLowerCase().contains(lowerCaseFilter);
            });
        });
        
        SortedList<PatientTableModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(patientTable.comparatorProperty());
        patientTable.setItems(sortedData);
    }
    
    /**
     * Creates the patient table with sortable columns
     */
    private void createPatientTable() {
        patientTable = new TableView<>();
        patientTable.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        // Columns
        TableColumn<PatientTableModel, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        idCol.setPrefWidth(80);
        
        TableColumn<PatientTableModel, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        
        TableColumn<PatientTableModel, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(80);
        
        TableColumn<PatientTableModel, String> diagnosisCol = new TableColumn<>("Diagnosis");
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        diagnosisCol.setPrefWidth(200);
        
        TableColumn<PatientTableModel, String> appointmentCol = new TableColumn<>("Appointment");
        appointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        appointmentCol.setPrefWidth(150);
        
        TableColumn<PatientTableModel, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        contactCol.setPrefWidth(200);
        
        // Action column
        TableColumn<PatientTableModel, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(param -> new TableCell<PatientTableModel, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            
            {
                editBtn.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: white; -fx-background-radius: 5;");
                deleteBtn.setStyle("-fx-background-color: " + ERROR_RED + "; -fx-text-fill: white; -fx-background-radius: 5;");
                
                editBtn.setOnAction(e -> {
                    PatientTableModel patient = getTableView().getItems().get(getIndex());
                    editPatient(patient.getPatientID());
                });
                
                deleteBtn.setOnAction(e -> {
                    PatientTableModel patient = getTableView().getItems().get(getIndex());
                    deletePatient(patient.getPatientID());
                });
                
                editBtn.setTooltip(new Tooltip("Edit patient"));
                deleteBtn.setTooltip(new Tooltip("Delete patient"));
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5);
                    buttons.getChildren().addAll(editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });
        
        patientTable.getColumns().addAll(idCol, nameCol, ageCol, diagnosisCol, appointmentCol, contactCol, actionCol);
        patientTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Load data
        refreshTable();
    }
    
    /**
     * Shows the add patient form
     */
    private void showAddPatient() {
        VBox form = new VBox(20);
        form.setPadding(new Insets(30));
        form.setMaxWidth(600);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label title = createTitle("Add New Patient");
        
        // Form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setPadding(new Insets(20));
        
        // Patient ID
        Label idLabel = createFormLabel("Patient ID");
        patientIDField = createFormTextField();
        patientIDField.setPromptText("Enter unique patient ID");
        formGrid.add(idLabel, 0, 0);
        formGrid.add(patientIDField, 1, 0);
        
        // Name
        Label nameLabel = createFormLabel("Name");
        nameField = createFormTextField();
        nameField.setPromptText("Enter patient name");
        formGrid.add(nameLabel, 0, 1);
        formGrid.add(nameField, 1, 1);
        
        // Age
        Label ageLabel = createFormLabel("Age");
        ageField = createFormTextField();
        ageField.setPromptText("Enter age (1-120)");
        formGrid.add(ageLabel, 0, 2);
        formGrid.add(ageField, 1, 2);
        
        // Contact
        Label contactLabel = createFormLabel("Contact Info");
        contactField = createFormTextField();
        contactField.setPromptText("Email or phone number");
        formGrid.add(contactLabel, 0, 3);
        formGrid.add(contactField, 1, 3);
        
        // Diagnosis (ComboBox)
        Label diagnosisLabel = createFormLabel("Diagnosis");
        diagnosisCombo = new ComboBox<>();
        diagnosisCombo.getItems().addAll(
            "General Checkup", "Fever", "Cough", "Headache", "Injury",
            "Chronic Disease", "Surgery", "Emergency", "Other"
        );
        diagnosisCombo.setPromptText("Select or type diagnosis");
        diagnosisCombo.setEditable(true);
        diagnosisCombo.setPrefWidth(300);
        diagnosisCombo.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
        formGrid.add(diagnosisLabel, 0, 4);
        formGrid.add(diagnosisCombo, 1, 4);
        
        // Appointment Date
        Label dateLabel = createFormLabel("Appointment Date");
        appointmentDatePicker = new DatePicker();
        appointmentDatePicker.setValue(LocalDate.now());
        appointmentDatePicker.setPrefWidth(300);
        appointmentDatePicker.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
        formGrid.add(dateLabel, 0, 5);
        formGrid.add(appointmentDatePicker, 1, 5);
        
        // Medical History
        Label historyLabel = createFormLabel("Medical History");
        medicalHistoryArea = new TextArea();
        medicalHistoryArea.setPromptText("Enter medical history notes...");
        medicalHistoryArea.setPrefRowCount(4);
        medicalHistoryArea.setWrapText(true);
        medicalHistoryArea.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
        formGrid.add(historyLabel, 0, 6);
        formGrid.add(medicalHistoryArea, 1, 6);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveBtn = createActionButton(isEditMode ? "✅ Update Patient" : "💾 Save Patient", SUCCESS_GREEN);
        saveBtn.setOnAction(e -> {
            if (isEditMode) {
                updatePatient();
            } else {
                savePatient();
            }
        });
        saveBtn.setTooltip(new Tooltip(isEditMode ? "Update patient (Ctrl+S)" : "Save patient (Ctrl+S)"));
        
        Button clearBtn = createActionButton("Clear Form", DARK_GRAY);
        clearBtn.setOnAction(e -> clearForm());
        clearBtn.setTooltip(new Tooltip("Clear all fields"));
        
        Button cancelBtn = createActionButton("Cancel", ERROR_RED);
        cancelBtn.setOnAction(e -> {
            isEditMode = false;
            editingPatientID = -1;
            showViewPatients();
        });
        
        buttonBox.getChildren().addAll(saveBtn, clearBtn, cancelBtn);
        
        form.getChildren().addAll(title, formGrid, buttonBox);
        
        // Add validation feedback
        setupFormValidation();
        
        animateContentChange(form);
    }
    
    /**
     * Sets up form validation with real-time feedback
     */
    private void setupFormValidation() {
        // Patient ID validation
        patientIDField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                patientIDField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            validateField(patientIDField, !newVal.isEmpty() && Integer.parseInt(newVal) > 0);
        });
        
        // Name validation
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            validateField(nameField, !newVal.trim().isEmpty() && newVal.length() >= 2);
        });
        
        // Age validation
        ageField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                ageField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (!newVal.isEmpty()) {
                try {
                    int age = Integer.parseInt(newVal);
                    validateField(ageField, age >= 1 && age <= 120);
                } catch (NumberFormatException e) {
                    validateField(ageField, false);
                }
            }
        });
        
        // Contact validation
        contactField.textProperty().addListener((obs, oldVal, newVal) -> {
            boolean isValid = !newVal.trim().isEmpty() && 
                             (newVal.contains("@") || newVal.matches(".*\\d.*"));
            validateField(contactField, isValid);
        });
    }
    
    /**
     * Validates a field and shows visual feedback
     */
    private void validateField(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + SUCCESS_GREEN + ";" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;"
            );
        } else {
            field.setStyle(
                "-fx-background-color: #FFEBEE;" +
                "-fx-border-color: " + ERROR_RED + ";" +
                "-fx-border-width: 2px;" +
                "-fx-border-radius: 5px;" +
                "-fx-background-radius: 5px;"
            );
        }
    }
    
    /**
     * Saves a new patient with validation
     */
    private void savePatient() {
        // Validate all required fields
        if (patientIDField.getText().isEmpty() || 
            nameField.getText().trim().isEmpty() ||
            ageField.getText().isEmpty() ||
            contactField.getText().trim().isEmpty()) {
            
            showError("Validation Error", "Please fill in all required fields (*)");
            return;
        }
        
        try {
            int patientID = Integer.parseInt(patientIDField.getText());
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText());
            String contact = contactField.getText().trim();
            
            // Validate age range
            if (age < 1 || age > 120) {
                showError("Invalid Age", "Age must be between 1 and 120");
                return;
            }
            
            // Check if patient already exists
            if (system.findPatient(patientID) != null) {
                showError("Duplicate Patient", "Patient with ID " + patientID + " already exists!");
                return;
            }
            
            // Add patient
            if (system.addPatient(patientID, name, age, contact)) {
                // Add diagnosis to medical history if provided
                if (diagnosisCombo.getValue() != null && !diagnosisCombo.getValue().isEmpty()) {
                    Patient patient = system.findPatient(patientID);
                    if (patient != null) {
                        patient.getMedicalHistory().add("Diagnosis: " + diagnosisCombo.getValue());
                    }
                }
                
                // Add medical history if provided
                if (medicalHistoryArea.getText() != null && !medicalHistoryArea.getText().trim().isEmpty()) {
                    Patient patient = system.findPatient(patientID);
                    if (patient != null) {
                        patient.getMedicalHistory().add(medicalHistoryArea.getText().trim());
                    }
                }
                
                // Schedule appointment if date is selected
                if (appointmentDatePicker.getValue() != null) {
                    String date = appointmentDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    system.scheduleAppointment(patientID, date, "10:00");
                }
                
                showSuccess("Success", "Patient added successfully!");
                clearForm();
                refreshTable();
                showViewPatients();
            } else {
                showError("Error", "Failed to add patient");
            }
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid numeric values for ID and Age");
        }
    }
    
    /**
     * Updates an existing patient (edit mode)
     */
    private void updatePatient() {
        // Validate required fields (except ID which is fixed in edit mode)
        if (nameField.getText().trim().isEmpty() ||
            ageField.getText().isEmpty() ||
            contactField.getText().trim().isEmpty()) {
            
            showError("Validation Error", "Please fill in all required fields (*)");
            return;
        }
        
        try {
            int patientID = editingPatientID;
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText());
            String contact = contactField.getText().trim();
            
            if (age < 1 || age > 120) {
                showError("Invalid Age", "Age must be between 1 and 120");
                return;
            }
            
            Patient patient = system.findPatient(patientID);
            if (patient == null) {
                showError("Not Found", "Original patient not found for update.");
                return;
            }
            
            // Update fields
            patient.setName(name);
            patient.setAge(age);
            patient.setContactInfo(contact);
            
            // Optionally add new medical notes if provided
            if (diagnosisCombo.getValue() != null && !diagnosisCombo.getValue().isEmpty()) {
                patient.getMedicalHistory().add("Diagnosis: " + diagnosisCombo.getValue());
            }
            if (medicalHistoryArea.getText() != null && !medicalHistoryArea.getText().trim().isEmpty()) {
                patient.getMedicalHistory().add(medicalHistoryArea.getText().trim());
            }
            
            showSuccess("Updated", "Patient updated successfully!");
            isEditMode = false;
            editingPatientID = -1;
            refreshTable();
            showViewPatients();
        } catch (NumberFormatException e) {
            showError("Invalid Input", "Please enter valid numeric values for Age");
        }
    }
    
    /**
     * Clears the form
     */
    private void clearForm() {
        patientIDField.clear();
        nameField.clear();
        ageField.clear();
        contactField.clear();
        diagnosisCombo.setValue(null);
        appointmentDatePicker.setValue(LocalDate.now());
        medicalHistoryArea.clear();
    }
    
    /**
     * Shows search patient view
     */
    private void showSearchPatient() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setMaxWidth(600);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label title = createTitle("Search Patient");
        
        TextField searchIDField = new TextField();
        searchIDField.setPromptText("Enter Patient ID");
        searchIDField.setPrefWidth(300);
        searchIDField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 8px;");
        
        Button searchBtn = createActionButton("🔍 Search", LIGHT_BLUE);
        
        VBox resultBox = new VBox(10);
        resultBox.setPadding(new Insets(20));
        resultBox.setStyle("-fx-background-color: " + LIGHT_GRAY + "; -fx-background-radius: 5;");
        
        searchBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(searchIDField.getText());
                Patient patient = system.findPatient(id);
                
                if (patient != null) {
                    resultBox.getChildren().clear();
                    resultBox.getChildren().add(createPatientInfoCard(patient));
                } else {
                    showError("Not Found", "Patient with ID " + id + " not found");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Please enter a valid Patient ID");
            }
        });
        
        container.getChildren().addAll(title, searchIDField, searchBtn, resultBox);
        animateContentChange(container);
    }
    
    /**
     * Shows appointments view
     */
    private void showAppointments() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        
        Label title = createTitle("Appointments");
        
        // Create appointments table
        TableView<Appointment> appointmentsTable = new TableView<>();
        appointmentsTable.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        TableColumn<Appointment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        
        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cellData -> {
            Patient p = cellData.getValue().getPatient();
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getName() : "N/A");
        });
        
        TableColumn<Appointment, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        appointmentsTable.getColumns().addAll(idCol, patientCol, dateCol, timeCol, statusCol);
        appointmentsTable.getItems().addAll(system.getAllAppointments());
        
        container.getChildren().addAll(title, appointmentsTable);
        VBox.setVgrow(appointmentsTable, Priority.ALWAYS);
        
        animateContentChange(container);
    }
    
    /**
     * Shows visit plans view
     */
    private void showVisitPlans() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        Label title = createTitle("Visit Plans");

        // Create form for new visit plan
        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        Label pidLabel = createFormLabel("Patient ID");
        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");
        patientIdField.setPrefWidth(150);

        Label dateLabel = createFormLabel("Date");
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(160);

        Label purposeLabel = createFormLabel("Purpose");
        TextField purposeField = new TextField();
        purposeField.setPromptText("Reason (e.g., Follow-up)");
        purposeField.setPrefWidth(220);

        Label doctorLabel = createFormLabel("Doctor");
        TextField doctorField = new TextField();
        doctorField.setPromptText("Doctor name");
        doctorField.setPrefWidth(200);

        final TableView<VisitPlan> plansTable = new TableView<>();
        Button createBtn = createActionButton("Create Plan", SUCCESS_GREEN);
        createBtn.setOnAction(e -> {
            try {
                int pid = Integer.parseInt(patientIdField.getText().trim());
                String date = datePicker.getValue() != null ? datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                String purpose = purposeField.getText() != null ? purposeField.getText().trim() : "";
                String doctor = doctorField.getText() != null ? doctorField.getText().trim() : "";
                if (date.isEmpty() || purpose.isEmpty()) {
                    showError("Validation Error", "Date and Purpose are required.");
                    return;
                }
                VisitPlan vp = system.createVisitPlan(pid, date, purpose, doctor);
                if (vp == null) {
                    showError("Not Found", "Patient with ID " + pid + " not found.");
                    return;
                }
                showSuccess("Created", "Visit plan created.");
                // Refresh table
                plansTable.getItems().setAll(system.getAllVisitPlans());
            } catch (NumberFormatException ex) {
                showError("Invalid Input", "Patient ID must be numeric.");
            }
        });

        form.add(pidLabel, 0, 0);
        form.add(patientIdField, 1, 0);
        form.add(dateLabel, 2, 0);
        form.add(datePicker, 3, 0);
        form.add(purposeLabel, 0, 1);
        form.add(purposeField, 1, 1, 3, 1);
        form.add(doctorLabel, 0, 2);
        form.add(doctorField, 1, 2);
        form.add(createBtn, 3, 2);

        // Table for visit plans
        plansTable.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        TableColumn<VisitPlan, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("planID"));
        idCol.setPrefWidth(70);

        TableColumn<VisitPlan, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(cellData -> {
            Patient p = cellData.getValue().getPatient();
            return new javafx.beans.property.SimpleStringProperty(p != null ? p.getName() : "N/A");
        });
        patientCol.setPrefWidth(180);

        TableColumn<VisitPlan, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(120);

        TableColumn<VisitPlan, String> purposeCol = new TableColumn<>("Purpose");
        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        purposeCol.setPrefWidth(240);

        TableColumn<VisitPlan, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        doctorCol.setPrefWidth(160);

        TableColumn<VisitPlan, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(110);

        TableColumn<VisitPlan, String> diagCol = new TableColumn<>("Diagnosis");
        diagCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        diagCol.setPrefWidth(220);

        TableColumn<VisitPlan, String> treatCol = new TableColumn<>("Treatment Plan");
        treatCol.setCellValueFactory(new PropertyValueFactory<>("treatmentPlan"));
        treatCol.setPrefWidth(260);

        TableColumn<VisitPlan, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(520);
        actionCol.setCellFactory(param -> new TableCell<VisitPlan, Void>() {
            private final Button completeBtn = new Button("Complete");
            private final Button cancelBtn = new Button("Cancel");
            private final Button setDiagBtn = new Button("Set Diagnosis");
            private final Button setTreatBtn = new Button("Set Treatment");
            private final Button saveReportBtn = new Button("Save Report");
            private final Button viewReportBtn = new Button("View Report");
            {
                completeBtn.setStyle("-fx-background-color: " + SUCCESS_GREEN + "; -fx-text-fill: white; -fx-background-radius: 5;");
                cancelBtn.setStyle("-fx-background-color: " + ERROR_RED + "; -fx-text-fill: white; -fx-background-radius: 5;");
                setDiagBtn.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: white; -fx-background-radius: 5;");
                setTreatBtn.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: white; -fx-background-radius: 5;");
                saveReportBtn.setStyle("-fx-background-color: " + LIGHT_BLUE + "; -fx-text-fill: white; -fx-background-radius: 5;");
                viewReportBtn.setStyle("-fx-background-color: " + DARK_GRAY + "; -fx-text-fill: white; -fx-background-radius: 5;");

                completeBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        system.setVisitPlanStatus(vp.getPlanID(), "Completed");
                        plansTable.getItems().setAll(system.getAllVisitPlans());
                    }
                });
                cancelBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        system.setVisitPlanStatus(vp.getPlanID(), "Cancelled");
                        plansTable.getItems().setAll(system.getAllVisitPlans());
                    }
                });
                setDiagBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        TextInputDialog dialog = new TextInputDialog(vp.getDiagnosis());
                        dialog.setTitle("Set Diagnosis");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Diagnosis:");
                        dialog.showAndWait().ifPresent(val -> {
                            system.updateVisitPlanReport(vp.getPlanID(), val, null, null);
                            plansTable.getItems().setAll(system.getAllVisitPlans());
                        });
                    }
                });
                setTreatBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Set Treatment Plan");
                        GridPane gp = new GridPane();
                        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(10));
                        TextArea treatArea = new TextArea(vp.getTreatmentPlan());
                        treatArea.setPromptText("Treatment plan...");
                        treatArea.setPrefRowCount(5);
                        gp.add(new Label("Treatment Plan"), 0, 0);
                        gp.add(treatArea, 1, 0);
                        dialog.getDialogPane().setContent(gp);
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                        dialog.showAndWait().ifPresent(bt -> {
                            if (bt == ButtonType.OK) {
                                system.updateVisitPlanReport(vp.getPlanID(), null, treatArea.getText(), null);
                                plansTable.getItems().setAll(system.getAllVisitPlans());
                            }
                        });
                    }
                });
                saveReportBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        // Prompt to enter Diagnosis, Treatment, and Note
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setTitle("Save Visit Report");
                        GridPane gp = new GridPane();
                        gp.setHgap(10); gp.setVgap(10); gp.setPadding(new Insets(10));
                        TextField diagField = new TextField(vp.getDiagnosis());
                        diagField.setPromptText("Diagnosis");
                        TextArea treatArea = new TextArea(vp.getTreatmentPlan());
                        treatArea.setPromptText("Treatment plan..."); treatArea.setPrefRowCount(4);
                        TextArea noteArea = new TextArea(vp.getDoctorNote());
                        noteArea.setPromptText("Doctor's note..."); noteArea.setPrefRowCount(3);
                        gp.add(new Label("Diagnosis"), 0, 0); gp.add(diagField, 1, 0);
                        gp.add(new Label("Treatment Plan"), 0, 1); gp.add(treatArea, 1, 1);
                        gp.add(new Label("Doctor Note"), 0, 2); gp.add(noteArea, 1, 2);
                        dialog.getDialogPane().setContent(gp);
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                        dialog.showAndWait().ifPresent(bt -> {
                            if (bt == ButtonType.OK) {
                                system.updateVisitPlanReport(vp.getPlanID(), diagField.getText(), treatArea.getText(), noteArea.getText());
                                plansTable.getItems().setAll(system.getAllVisitPlans());
                            }
                        });
                    }
                });
                viewReportBtn.setOnAction(e -> {
                    VisitPlan vp = getTableView().getItems().get(getIndex());
                    if (vp != null) {
                        String content = system.getVisitPlanFormattedReport(vp.getPlanID());
                        showReportDialog("Visit Plan Report", content);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(8);
                    box.getChildren().addAll(completeBtn, cancelBtn, setDiagBtn, setTreatBtn, saveReportBtn, viewReportBtn);
                    setGraphic(box);
                }
            }
        });

        plansTable.getColumns().addAll(idCol, patientCol, dateCol, purposeCol, doctorCol, statusCol, diagCol, treatCol, actionCol);
        plansTable.getItems().addAll(system.getAllVisitPlans());

        container.getChildren().addAll(title, form, plansTable);
        VBox.setVgrow(plansTable, Priority.ALWAYS);
        
        animateContentChange(container);
    }
    
    /**
     * Shows reports view
     */
    private void showReports() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        
        Label title = createTitle("Reports");
        
        // Input for patient report
        TextField reportPatientIDField = new TextField();
        reportPatientIDField.setPromptText("Enter Patient ID for report");
        reportPatientIDField.setPrefWidth(300);
        reportPatientIDField.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 8px;");
        
        VBox reportsBox = new VBox(15);
        reportsBox.setSpacing(15);
        
        Button patientReportBtn = createActionButton("👤 Patient Report", LIGHT_BLUE);
        patientReportBtn.setPrefWidth(300);
        patientReportBtn.setOnAction(e -> {
            String input = reportPatientIDField.getText() != null ? reportPatientIDField.getText().trim() : "";
            if (input.isEmpty()) {
                showError("No Patient ID", "Please enter a Patient ID first");
                return;
            }
            try {
                int id = Integer.parseInt(input);
                String report = system.generatePatientReport(id);
                showReportDialog("Patient Report", report);
            } catch (NumberFormatException ex) {
                showError("Invalid ID", "Please enter a valid numeric Patient ID");
            }
        });
        
        Button appointmentReportBtn = createActionButton("📅 Appointment Report", LIGHT_BLUE);
        appointmentReportBtn.setPrefWidth(300);
        appointmentReportBtn.setOnAction(e -> {
            String report = system.generateAppointmentReport();
            showReportDialog("Appointment Report", report);
        });
        
        Button revenueReportBtn = createActionButton("💰 Revenue Report", SUCCESS_GREEN);
        revenueReportBtn.setPrefWidth(300);
        revenueReportBtn.setOnAction(e -> {
            String report = system.generateRevenueReport();
            showReportDialog("Revenue Report", report);
        });

        Button importCsvBtn = createActionButton("📥 Import CSV", PRIMARY_BLUE);
        importCsvBtn.setPrefWidth(300);
        importCsvBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("d:/Important/DATAStructure project/healthcare_dataset.csv");
            dialog.setTitle("Import CSV");
            dialog.setHeaderText(null);
            dialog.setContentText("CSV Path:");
            dialog.showAndWait().ifPresent(path -> {
                try {
                    int count = CsvImporter.importHealthcareCsv(system, path);
                    showSuccess("Import Complete", "Imported " + count + " patients from CSV.");
                    refreshTable();
                } catch (Exception ex) {
                    showError("Import Failed", ex.getMessage());
                }
            });
        });
        
        reportsBox.getChildren().addAll(patientReportBtn, appointmentReportBtn, revenueReportBtn, importCsvBtn);
        container.getChildren().addAll(title, reportPatientIDField, reportsBox);
        
        animateContentChange(container);
    }
    
    /**
     * Creates a patient info card for display
     */
    private VBox createPatientInfoCard(Patient patient) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        
        Label nameLabel = new Label("Name: " + patient.getName());
        Label idLabel = new Label("ID: " + patient.getPatientID());
        Label ageLabel = new Label("Age: " + patient.getAge());
        Label contactLabel = new Label("Contact: " + patient.getContactInfo());
        
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        
        card.getChildren().addAll(nameLabel, idLabel, ageLabel, contactLabel);
        
        return card;
    }
    
    /**
     * Edits a patient
     */
    private void editPatient(int patientID) {
        Patient patient = system.findPatient(patientID);
        if (patient != null) {
            // Enter edit mode and reuse the form
            isEditMode = true;
            editingPatientID = patientID;
            showAddPatient(); // Reuse add form
            patientIDField.setText(String.valueOf(patient.getPatientID()));
            patientIDField.setEditable(false);
            nameField.setText(patient.getName());
            ageField.setText(String.valueOf(patient.getAge()));
            contactField.setText(patient.getContactInfo());
        } else {
            showError("Not Found", "Patient with ID " + patientID + " not found");
        }
    }
    
    /**
     * Deletes a patient with confirmation
     */
    private void deletePatient(int patientID) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Patient");
        confirmAlert.setContentText("Are you sure you want to delete patient ID " + patientID + "?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Note: PatientBST doesn't have a delete method exposed, so we'd need to add it
            // For now, just show a message
            showInfo("Delete", "Delete functionality requires backend update");
            refreshTable();
        }
    }
    
    /**
     * Refreshes the patient table
     */
    private void refreshTable() {
        try {
            if (patientData == null) {
                patientData = FXCollections.observableArrayList();
            }
            patientData.clear();

            List<Patient> patients = system.getAllPatients();

            // Build a patient -> appointment date map once (avoid O(P*A))
            java.util.Map<Integer, String> apptMap = new java.util.HashMap<>();
            for (Appointment apt : system.getAllAppointments()) {
                if (apt != null && apt.getPatient() != null) {
                    int pid = apt.getPatient().getPatientID();
                    String date = apt.getDate();
                    if (date != null && !date.isEmpty()) {
                        // keep first encountered date per patient
                        apptMap.putIfAbsent(pid, date);
                    }
                }
            }

            for (Patient p : patients) {
                // Extract the first Diagnosis entry from medical history
                String diagnosis = "No diagnosis";
                for (String entry : p.getMedicalHistory()) {
                    if (entry != null && entry.toLowerCase().startsWith("diagnosis")) {
                        String cleaned = entry.replaceFirst("(?i)diagnosis:\\s*", "").trim();
                        diagnosis = cleaned.isEmpty() ? "No diagnosis" : cleaned;
                        break;
                    }
                }

                String appointmentDate = apptMap.getOrDefault(p.getPatientID(), "No appointment");

                patientData.add(new PatientTableModel(
                    p.getPatientID(),
                    p.getName(),
                    p.getAge(),
                    diagnosis,
                    appointmentDate,
                    p.getContactInfo()
                ));
            }
        } catch (Exception ex) {
            showError("Refresh Failed", ex.getMessage() != null ? ex.getMessage() : String.valueOf(ex));
        }
    }
    
    /**
     * Sets up keyboard shortcuts
     */
    private void setupKeyboardShortcuts() {
        Scene scene = primaryStage.getScene();
        
        // Ctrl+V: View Patients
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN),
            () -> switchView("view")
        );
        
        // Ctrl+A: Add Patient
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN),
            () -> switchView("add")
        );
        
        // Ctrl+F: Search
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
            () -> switchView("search")
        );
        
        // Ctrl+L: Visit Plans
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN),
            () -> switchView("visitplans")
        );
        
        // Ctrl+S: Save or Update (when in add/edit form)
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
            () -> {
                if ("add".equals(currentView)) {
                    if (isEditMode) {
                        updatePatient();
                    } else {
                        savePatient();
                    }
                }
            }
        );
        
        // F5: Refresh
        scene.getAccelerators().put(
            new KeyCodeCombination(KeyCode.F5),
            () -> refreshTable()
        );
    }
    
    /**
     * Helper methods for UI creation
     */
    private Label createTitle(String text) {
        Label title = new Label(text);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.web(PRIMARY_BLUE));
        return title;
    }
    
    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        return label;
    }
    
    private TextField createFormTextField() {
        TextField field = new TextField();
        field.setPrefWidth(300);
        field.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-padding: 8px;");
        return field;
    }
    
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 8;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10px 20px;" +
            "-fx-cursor: hand;"
        );
        
        btn.setOnMouseEntered(e -> addScaleAnimation(btn, 1.05));
        btn.setOnMouseExited(e -> addScaleAnimation(btn, 1.0));
        
        return btn;
    }
    
    /**
     * Animation helpers
     */
    private void animateContentChange(javafx.scene.Node newContent) {
        // Temporarily disable animation to fix disappearing content issue
        contentArea.getChildren().clear();
        contentArea.getChildren().add(newContent);
    }
    
    private void addScaleAnimation(javafx.scene.Node node, double scale) {
        ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }
    
    /**
     * Dialog helpers
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showReportDialog(String title, String content) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setPrefRowCount(20);
        textArea.setPrefColumnCount(60);
        
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
    
    /**
     * Loads sample data
     */
    private void loadSampleData() {
        system.addPatient(101, "John Doe", 45, "john.doe@email.com");
        system.addPatient(102, "Jane Smith", 30, "jane.smith@email.com");
        system.addPatient(103, "Bob Johnson", 65, "bob.j@email.com");
        
        Patient p1 = system.findPatient(101);
        if (p1 != null) {
            p1.getMedicalHistory().add("Diagnosis: General Checkup");
        }
        
        system.scheduleAppointment(101, "2024-12-25", "10:00");
        
        patientData = FXCollections.observableArrayList();
        refreshTable();
    }
    
    /**
     * Table model for patient display
     */
    public static class PatientTableModel {
        private final javafx.beans.property.IntegerProperty patientID;
        private final javafx.beans.property.StringProperty name;
        private final javafx.beans.property.IntegerProperty age;
        private final javafx.beans.property.StringProperty diagnosis;
        private final javafx.beans.property.StringProperty appointmentDate;
        private final javafx.beans.property.StringProperty contactInfo;
        
        public PatientTableModel(int patientID, String name, int age, 
                                String diagnosis, String appointmentDate, String contactInfo) {
            this.patientID = new javafx.beans.property.SimpleIntegerProperty(patientID);
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.age = new javafx.beans.property.SimpleIntegerProperty(age);
            this.diagnosis = new javafx.beans.property.SimpleStringProperty(diagnosis);
            this.appointmentDate = new javafx.beans.property.SimpleStringProperty(appointmentDate);
            this.contactInfo = new javafx.beans.property.SimpleStringProperty(contactInfo);
        }
        
        // Getters
        public int getPatientID() { return patientID.get(); }
        public String getName() { return name.get(); }
        public int getAge() { return age.get(); }
        public String getDiagnosis() { return diagnosis.get(); }
        public String getAppointmentDate() { return appointmentDate.get(); }
        public String getContactInfo() { return contactInfo.get(); }
        
        // Property getters for TableView
        public javafx.beans.property.IntegerProperty patientIDProperty() { return patientID; }
        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.IntegerProperty ageProperty() { return age; }
        public javafx.beans.property.StringProperty diagnosisProperty() { return diagnosis; }
        public javafx.beans.property.StringProperty appointmentDateProperty() { return appointmentDate; }
        public javafx.beans.property.StringProperty contactInfoProperty() { return contactInfo; }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

