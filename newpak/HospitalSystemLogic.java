package newpak;

import java.util.*;

// =========================================================
// PATIENT CLASS (Linked List Node)
// =========================================================
class Patient {
    private int patientId;
    private String patientName;
    private int patientAge;
    private String gender;
    private String disease;
    private String contact;
    Patient next;

    public Patient(int id, String name, int age, String gender, String disease, String contact) {
        this.patientId = id;
        this.patientName = name;
        this.patientAge = age;
        this.gender = gender;
        this.disease = disease;
        this.contact = contact;
        this.next = null;
    }

    // Getters
    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getPatientAge() { return patientAge; }
    public String getGender() { return gender; }
    public String getDisease() { return disease; }
    public String getContact() { return contact; }

    // Setters
    public void setPatientName(String name) { this.patientName = name; }
    public void setPatientAge(int age) { this.patientAge = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setDisease(String disease) { this.disease = disease; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Age: %d | Gender: %s | Disease: %s | Contact: %s",
                patientId, patientName, patientAge, gender, disease, contact);
    }
}

// =========================================================
// PATIENT MANAGEMENT (Linked List)
// =========================================================
class PatientManagement {
    private Patient head;
    private Set<Integer> patientIds;

    public PatientManagement() {
        head = null;
        patientIds = new HashSet<>();
    }

    public boolean addPatient(int id, String name, int age, String gender, String disease, String contact) {
        if (patientIds.contains(id)) {
            return false; // ID already exists
        }

        Patient newPatient = new Patient(id, name, age, gender, disease, contact);
        patientIds.add(id);

        if (head == null) {
            head = newPatient;
        } else {
            Patient temp = head;
            while (temp.next != null)
                temp = temp.next;
            temp.next = newPatient;
        }
        return true;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        Patient temp = head;
        while (temp != null) {
            patients.add(temp);
            temp = temp.next;
        }
        return patients;
    }

    public Patient searchPatient(int id) {
        Patient temp = head;
        while (temp != null) {
            if (temp.getPatientId() == id) {
                return temp;
            }
            temp = temp.next;
        }
        return null;
    }

    public boolean updatePatient(int id, String name, int age, String gender, String disease, String contact) {
        Patient patient = searchPatient(id);
        if (patient != null) {
            patient.setPatientName(name);
            patient.setPatientAge(age);
            patient.setGender(gender);
            patient.setDisease(disease);
            patient.setContact(contact);
            return true;
        }
        return false;
    }

    public boolean deletePatient(int id) {
        if (head == null) return false;

        if (head.getPatientId() == id) {
            head = head.next;
            patientIds.remove(id);
            return true;
        }

        Patient prev = head;
        Patient curr = head.next;
        while (curr != null) {
            if (curr.getPatientId() == id) {
                prev.next = curr.next;
                patientIds.remove(id);
                return true;
            }
            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    public int getTotalPatients() {
        return patientIds.size();
    }

    public boolean patientExists(int id) {
        return patientIds.contains(id);
    }
}

// =========================================================
// EMERGENCY PATIENT (Priority Queue Node)
// =========================================================
class EmergencyPatient implements Comparable<EmergencyPatient> {

    private int patientId;
    private String patientName;
    private int priority;        // 3 = HIGH, 2 = MEDIUM, 1 = LOW
    private String condition;
    private long arrivalTime;    // NEW (for same-priority ordering)

    public EmergencyPatient(int id, String name, int priority, String condition) {
        this.patientId = id;
        this.patientName = name;
        this.priority = priority;
        this.condition = condition;
        this.arrivalTime = System.currentTimeMillis(); // auto-set
    }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getPriority() { return priority; }
    public String getCondition() { return condition; }
    public long getArrivalTime() { return arrivalTime; }

    @Override
    public int compareTo(EmergencyPatient other) {
        // 1️⃣ Higher priority first
        int priorityCompare = Integer.compare(other.priority, this.priority);
        if (priorityCompare != 0) return priorityCompare;

        // 2️⃣ Same priority → earlier arrival first
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return String.format(
                "ID: %d | Name: %s | Priority: %d | Condition: %s | Arrival: %d",
                patientId, patientName, priority, condition, arrivalTime
        );
    }
}

// =========================================================
// EMERGENCY MANAGEMENT (Priority Queue)
// =========================================================
class EmergencyManagement {

    private PriorityQueue<EmergencyPatient> queue;
    private int capacity;

    public EmergencyManagement(int capacity) {
        this.capacity = capacity;
        this.queue = new PriorityQueue<>(capacity); // uses compareTo()
    }

    public boolean addEmergencyPatient(int id, String name, int priority, String condition) {
        if (queue.size() >= capacity) {
            return false;
        }
        queue.offer(new EmergencyPatient(id, name, priority, condition));
        return true;
    }

    public EmergencyPatient treatNextPatient() {
        return queue.poll(); // highest priority + earliest arrival
    }

    public EmergencyPatient peekNextPatient() {
        return queue.peek();
    }

    // Sorted list for JTable display
    public List<EmergencyPatient> getAllEmergencyPatients() {
        List<EmergencyPatient> list = new ArrayList<>(queue);
        list.sort((a, b) -> {
            int p = Integer.compare(b.getPriority(), a.getPriority());
            if (p != 0) return p;
            return Long.compare(a.getArrivalTime(), b.getArrivalTime());
        });
        return list;
    }

    public int getEmergencyCount() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// =========================================================
// OPD PATIENT (Circular Queue)
// =========================================================
class OPDPatient {
    private int patientId;
    private String patientName;
    private int tokenNumber;
    private String department;

    public OPDPatient(int id, String name, int token, String department) {
        this.patientId = id;
        this.patientName = name;
        this.tokenNumber = token;
        this.department = department;
    }

    public int getPatientId() { return patientId; }
    public String getPatientName() { return patientName; }
    public int getTokenNumber() { return tokenNumber; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("Token: %d | ID: %d | Name: %s | Department: %s",
                tokenNumber, patientId, patientName, department);
    }
}

// =========================================================
// OPD MANAGEMENT (Circular Queue)
// =========================================================
class OPDManagement {
    private OPDPatient[] queue;
    private int front, rear, size, capacity, tokenCounter;

    public OPDManagement(int capacity) {
        this.capacity = capacity;
        queue = new OPDPatient[capacity];
        front = 0;
        rear = -1;
        size = 0;
        tokenCounter = 1001; // Starting token number
    }

    public boolean addOPDPatient(int id, String name, String department) {
        if (size == capacity) {
            return false;
        }
        rear = (rear + 1) % capacity;
        queue[rear] = new OPDPatient(id, name, tokenCounter, department);
        size++;
        tokenCounter++;
        return true;
    }

    public OPDPatient treatNextPatient() {
        if (size == 0) {
            return null;
        }
        OPDPatient treated = queue[front];
        front = (front + 1) % capacity;
        size--;
        return treated;
    }

    public List<OPDPatient> getAllOPDPatients() {
        List<OPDPatient> patients = new ArrayList<>();
        if (size == 0) return patients;

        int i = front;
        for (int count = 0; count < size; count++) {
            patients.add(queue[i]);
            i = (i + 1) % capacity;
        }
        return patients;
    }

    public int getOPDCount() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public OPDPatient peekNextPatient() {
        if (size == 0) return null;
        return queue[front];
    }
}

// =========================================================
// MEDICAL HISTORY MANAGEMENT (Stack)
// =========================================================
class MedicalHistoryManagement {
    private Stack<String> historyStack;
    private int capacity;

    public MedicalHistoryManagement(int capacity) {
        this.capacity = capacity;
        this.historyStack = new Stack<>();
    }

    public boolean addMedicalRecord(String record) {
        if (historyStack.size() >= capacity) {
            return false;
        }
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        historyStack.push(timestamp + " - " + record);
        return true;
    }

    public String removeLatestRecord() {
        if (historyStack.isEmpty()) {
            return null;
        }
        return historyStack.pop();
    }

    public List<String> getAllRecords() {
        return new ArrayList<>(historyStack);
    }

    public int getRecordCount() {
        return historyStack.size();
    }

    public boolean isEmpty() {
        return historyStack.isEmpty();
    }

    public void clearHistory() {
        historyStack.clear();
    }
}
// =========================================================
// DEPARTMENT NODE (BST Node)
// =========================================================
class DepartmentNode {
    String deptName;
    int patientCount;
    DepartmentNode left, right;

    DepartmentNode(String name) {
        this.deptName = name;
        this.patientCount = 0;
        left = right = null;
    }
}

// =========================================================
// DEPARTMENT MANAGEMENT (Binary Search Tree)
// =========================================================
class DepartmentManagement {
    private DepartmentNode root;
    private Map<String, Integer> departmentStats;

    public DepartmentManagement() {
        root = null;
        departmentStats = new HashMap<>();
    }

    // ---------------- Add Department ----------------
    public boolean addDepartment(String name) {
        if (departmentStats.containsKey(name.toLowerCase())) {
            return false;
        }
        root = addRecursive(root, name);
        departmentStats.put(name.toLowerCase(), 0);
        return true;
    }

    private DepartmentNode addRecursive(DepartmentNode node, String name) {
        if (node == null) return new DepartmentNode(name);

        int comparison = name.compareToIgnoreCase(node.deptName);
        if (comparison < 0) {
            node.left = addRecursive(node.left, name);
        } else if (comparison > 0) {
            node.right = addRecursive(node.right, name);
        }
        return node;
    }

    // ---------------- Remove Department ----------------
    public boolean removeDepartment(String name) {
        if (!departmentStats.containsKey(name.toLowerCase())) return false;
        root = removeRecursive(root, name);
        departmentStats.remove(name.toLowerCase());
        return true;
    }

    private DepartmentNode removeRecursive(DepartmentNode node, String name) {
        if (node == null) return null;

        int cmp = name.compareToIgnoreCase(node.deptName);
        if (cmp < 0) node.left = removeRecursive(node.left, name);
        else if (cmp > 0) node.right = removeRecursive(node.right, name);
        else {
            // Node to remove found
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            // Node with two children: get inorder successor
            DepartmentNode successor = findMin(node.right);
            node.deptName = successor.deptName;
            node.patientCount = successor.patientCount;
            node.right = removeRecursive(node.right, successor.deptName);
        }
        return node;
    }

    private DepartmentNode findMin(DepartmentNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---------------- Search Department ----------------
    public boolean departmentExists(String name) {
        return searchRecursive(root, name);
    }

    private boolean searchRecursive(DepartmentNode node, String name) {
        if (node == null) return false;

        int cmp = name.compareToIgnoreCase(node.deptName);
        if (cmp == 0) return true;
        if (cmp < 0) return searchRecursive(node.left, name);
        return searchRecursive(node.right, name);
    }

    // ---------------- Update Department Name ----------------
    public boolean updateDepartmentName(String oldName, String newName) {
        if (!departmentStats.containsKey(oldName.toLowerCase()) ||
                departmentStats.containsKey(newName.toLowerCase())) {
            return false; // Old name must exist and new name must not exist
        }

        int count = departmentStats.get(oldName.toLowerCase());
        removeDepartment(oldName);
        addDepartment(newName);
        departmentStats.put(newName.toLowerCase(), count);
        return true;
    }

    // ---------------- Increment / Decrement Patient Count ----------------
    public boolean incrementPatientCount(String department) {
        if (!departmentStats.containsKey(department.toLowerCase())) return false;
        departmentStats.put(department.toLowerCase(),
                departmentStats.get(department.toLowerCase()) + 1);
        return true;
    }

    public boolean decrementPatientCount(String department) {
        if (!departmentStats.containsKey(department.toLowerCase())) return false;
        int count = departmentStats.get(department.toLowerCase());
        departmentStats.put(department.toLowerCase(), Math.max(0, count - 1));
        return true;
    }

    // ---------------- Get All Departments (BST in-order) ----------------
    public List<String> getAllDepartments() {
        List<String> departments = new ArrayList<>();
        inOrderTraversal(root, departments);
        return departments;
    }

    private void inOrderTraversal(DepartmentNode node, List<String> departments) {
        if (node != null) {
            inOrderTraversal(node.left, departments);
            departments.add(node.deptName);
            inOrderTraversal(node.right, departments);
        }
    }

    // ---------------- Get Departments with Stats ----------------
    public List<String> getDepartmentsWithStats() {
        List<String> departments = getAllDepartments();
        List<String> result = new ArrayList<>();
        for (String dept : departments) {
            int count = departmentStats.getOrDefault(dept.toLowerCase(), 0);
            result.add(dept + " (" + count + " patients)");
        }
        return result;
    }

    // ---------------- Count Departments ----------------
    public int countDepartments() {
        return departmentStats.size();
    }
}


// =========================================================
// MAIN SYSTEM INTEGRATION CLASS
// =========================================================
public class HospitalSystemLogic {
    // Singleton instance
    private static HospitalSystemLogic instance;

    // Core components
    private PatientManagement patientManagement;
    private EmergencyManagement emergencyManagement;
    private OPDManagement opdManagement;
    private MedicalHistoryManagement medicalHistory;
    private DepartmentManagement departmentManagement;

    // Configuration
    private static final int EMERGENCY_CAPACITY = 100;
    private static final int OPD_CAPACITY = 50;
    private static final int HISTORY_CAPACITY = 1000;

    private HospitalSystemLogic() {
        initialize();
    }

    public static synchronized HospitalSystemLogic getInstance() {
        if (instance == null) {
            instance = new HospitalSystemLogic();
        }
        return instance;
    }

    private void initialize() {
        patientManagement = new PatientManagement();
        emergencyManagement = new EmergencyManagement(EMERGENCY_CAPACITY);
        opdManagement = new OPDManagement(OPD_CAPACITY);
        medicalHistory = new MedicalHistoryManagement(HISTORY_CAPACITY);
        departmentManagement = new DepartmentManagement();

        // Initialize with some sample data
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Add sample departments
        String[] sampleDepts = {"Cardiology", "Neurology", "Orthopedics", "Pediatrics", "General"};
        for (String dept : sampleDepts) {
            departmentManagement.addDepartment(dept);
        }

        // Add sample patients
        addSamplePatients();
    }

    private void addSamplePatients() {
        patientManagement.addPatient(1001, "John Doe", 45, "Male", "Hypertension", "1234567890");
        patientManagement.addPatient(1002, "Jane Smith", 32, "Female", "Migraine", "0987654321");
        patientManagement.addPatient(1003, "Robert Brown", 67, "Male", "Arthritis", "1122334455");

        // Add sample medical records
        medicalHistory.addMedicalRecord("Patient 1001: Initial consultation for hypertension");
        medicalHistory.addMedicalRecord("Patient 1002: Follow-up for migraine treatment");
        medicalHistory.addMedicalRecord("Patient 1003: X-ray results reviewed for arthritis");
    }

    // ==================== PATIENT MANAGEMENT METHODS ====================

    public boolean addPatient(int id, String name, int age, String gender, String disease, String contact) {
        return patientManagement.addPatient(id, name, age, gender, disease, contact);
    }

    public List<Patient> getAllPatients() {
        return patientManagement.getAllPatients();
    }

    public Patient searchPatient(int id) {
        return patientManagement.searchPatient(id);
    }

    public boolean updatePatient(int id, String name, int age, String gender, String disease, String contact) {
        return patientManagement.updatePatient(id, name, age, gender, disease, contact);
    }

    public boolean deletePatient(int id) {
        return patientManagement.deletePatient(id);
    }

    public int getTotalPatients() {
        return patientManagement.getTotalPatients();
    }

    public boolean patientExists(int id) {
        return patientManagement.patientExists(id);
    }

    // ==================== EMERGENCY MANAGEMENT METHODS ====================

    public boolean addEmergencyPatient(int id, String name, int priority, String condition) {
        return emergencyManagement.addEmergencyPatient(id, name, priority, condition);
    }

    public EmergencyPatient treatNextEmergencyPatient() {
        return emergencyManagement.treatNextPatient();
    }

    public List<EmergencyPatient> getAllEmergencyPatients() {
        return emergencyManagement.getAllEmergencyPatients();
    }

    public int getEmergencyCount() {
        return emergencyManagement.getEmergencyCount();
    }

    public EmergencyPatient peekNextEmergencyPatient() {
        return emergencyManagement.peekNextPatient();
    }

    // ==================== OPD MANAGEMENT METHODS ====================

    public boolean addOPDPatient(int id, String name, String department) {
        if (departmentManagement.departmentExists(department)) {
            boolean success = opdManagement.addOPDPatient(id, name, department);
            if (success) {
                departmentManagement.incrementPatientCount(department);
            }
            return success;
        }
        return false;
    }

    public OPDPatient treatNextOPDPatient() {
        OPDPatient patient = opdManagement.treatNextPatient();
        if (patient != null) {
            departmentManagement.incrementPatientCount(patient.getDepartment());
        }
        return patient;
    }

    public List<OPDPatient> getAllOPDPatients() {
        return opdManagement.getAllOPDPatients();
    }

    public int getOPDCount() {
        return opdManagement.getOPDCount();
    }

    public OPDPatient peekNextOPDPatient() {
        return opdManagement.peekNextPatient();
    }

    // ==================== MEDICAL HISTORY METHODS ====================

    public boolean addMedicalRecord(String record) {
        return medicalHistory.addMedicalRecord(record);
    }

    public String removeLatestRecord() {
        return medicalHistory.removeLatestRecord();
    }

    public List<String> getAllMedicalRecords() {
        return medicalHistory.getAllRecords();
    }

    public int getMedicalRecordCount() {
        return medicalHistory.getRecordCount();
    }

    public void clearMedicalHistory() {
        medicalHistory.clearHistory();
    }

    // ==================== DEPARTMENT MANAGEMENT METHODS ====================

    public boolean addDepartment(String name) {
        return departmentManagement.addDepartment(name);
    }

    public List<String> getAllDepartments() {
        return departmentManagement.getAllDepartments();
    }

    public List<String> getDepartmentsWithStats() {
        return departmentManagement.getDepartmentsWithStats();
    }

    public int getDepartmentCount() {
        return departmentManagement.countDepartments();
    }

    public boolean departmentExists(String name) {
        return departmentManagement.departmentExists(name);
    }

    // Remove a department
    public boolean removeDepartment(String name) {
        return departmentManagement.removeDepartment(name);
    }

    // Update a department name
    public boolean updateDepartmentName(String oldName, String newName) {
        return departmentManagement.updateDepartmentName(oldName, newName);
    }



    // ==================== REPORT METHODS ====================

    public Map<String, Integer> getSystemReport() {
        Map<String, Integer> report = new HashMap<>();
        report.put("Total Patients", patientManagement.getTotalPatients());
        report.put("Emergency Patients", emergencyManagement.getEmergencyCount());
        report.put("OPD Patients", opdManagement.getOPDCount());
        report.put("Departments", departmentManagement.countDepartments());
        report.put("Medical Records", medicalHistory.getRecordCount());
        return report;
    }

    // ==================== VALIDATION METHODS ====================

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age <= 120;
    }

    public static boolean isValidPriority(int priority) {
        return priority >= 1 && priority <= 3;
    }

    // ==================== DATA EXPORT METHODS ====================

    public String exportPatientData() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== PATIENT DATA EXPORT =====\n");
        sb.append("Total Patients: ").append(getTotalPatients()).append("\n\n");

        for (Patient p : getAllPatients()) {
            sb.append(p.toString()).append("\n");
        }

        return sb.toString();
    }

    public String exportSystemReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== HOSPITAL SYSTEM REPORT =====\n");
        sb.append("Generated: ").append(new Date()).append("\n\n");

        Map<String, Integer> report = getSystemReport();
        for (Map.Entry<String, Integer> entry : report.entrySet()) {
            sb.append(String.format("%-20s: %d\n", entry.getKey(), entry.getValue()));
        }

        sb.append("\n===== DEPARTMENT STATISTICS =====\n");
        for (String dept : getDepartmentsWithStats()) {
            sb.append(dept).append("\n");
        }

        return sb.toString();
    }
}
