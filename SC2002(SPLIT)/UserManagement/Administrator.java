package UserManagement;

import DataManagement.doctorManager;
import DataManagement.medicineManager;
import DataManagement.orderRequestManager;
import DataManagement.staffManager;
import DataManagement.appointmentOutcomeManager;
import DataManagement.appointmentManager;
import DataManagement.userManager;
import InventoryManagement.InventoryEditor;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Administrator extends Users implements InventoryEditor {

    private final staffManager staffManager;
    private final medicineManager medicineManager;
    private final orderRequestManager orderRequestManager;
    private final appointmentManager appointmentManager;
    private final appointmentOutcomeManager appointmentOutcomeManager;
    private final userManager userManager;

    // Constructor
    public Administrator(String userID, staffManager staffManager, medicineManager medicineManager,
                         orderRequestManager orderRequestManager, appointmentManager appointmentManager,
                         appointmentOutcomeManager appointmentOutcomeManager, userManager userManager) throws IOException {
        super(userID, userManager);  // Passes the userID and userManager to the superclass Users

        // Initialize fields with constructor parameters
        this.staffManager = staffManager;
        this.medicineManager = medicineManager;
        this.orderRequestManager = orderRequestManager;
        this.appointmentManager = appointmentManager;
        this.appointmentOutcomeManager = appointmentOutcomeManager;
        this.userManager = userManager;
    }




    @Override
    public void displayMenu() {
        Scanner sc = new Scanner(System.in); // Initialize Scanner only once

        while (true) {
            System.out.println("=========================================================");
            System.out.println("Administrator Menu:");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.println("Please select an option (1-5): ");
            System.out.println("=========================================================");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    manageHospitalStaff(sc);
                    break;
                case 2:
                    viewAppointmentDetails();
                    break;
                case 3:
                    manageMedicationInventory(sc);
                    break;
                case 4:
                    approveReplenishmentRequest(sc);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }

    // Manage Hospital Staff
    private void manageHospitalStaff(Scanner sc) {
        System.out.println("=== View and Manage Hospital Staff ===");
        System.out.println("1. View Staff");
        System.out.println("2. Manage Staff");
        System.out.print("Please select an option (1-2): ");

        int staffChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        if (staffChoice == 1) {
            viewStaff();
        } else if (staffChoice == 2) {
            manageStaff(sc); // Pass the scanner to manageStaff for input
        } else {
            System.out.println("Invalid choice.");
        }
    }

    // View staff
    private void viewStaff() {
        try {
            List<String[]> staffList = staffManager.getStaffList();

            if (staffList.isEmpty()) {
                System.out.println("No staff found.");
            } else {
                System.out.printf("%-10s %-10s %-20s %-20s %-8s %-5s %-20s %-10s%n",
                        "StaffID", "UserID", "Name", "Role", "Gender", "Age", "Specialization", "Contact");
                System.out.println("=============================================================================================================");

                // Loop through each staff member and print details, skipping the header row from CSV
                boolean isHeader = true;
                for (String[] staff : staffList) {
                    if (isHeader) {
                        isHeader = false; // Skip the first row (header from the CSV file)
                        continue;
                    }
                    System.out.printf("%-10s %-10s %-20s %-20s %-8s %-5s %-20s %-10s%n",
                            staff[0], staff[1], staff[2], staff[3], staff[4], staff[5], staff[6], staff[7]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving staff data: " + e.getMessage());
        }
    }

    // Manage Staff options
    private void manageStaff(Scanner sc) {
        System.out.println("Manage Staff Options:");
        System.out.println("1. Add New Staff");
        System.out.println("2. Update Existing Staff");
        System.out.println("3. Remove Staff");
        System.out.print("Please select an option (1-3): ");

        int manageChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (manageChoice) {
            case 1:
                try {
                    staffManager.addStaff();
                } catch (IOException e) {
                    System.err.println("Error updating staff information: " + e.getMessage());
                }
                break;
            case 2:
                updateStaff(sc);
                break;
            case 3:
                removeStaff(sc);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Update staff details
    private void updateStaff(Scanner sc) {
        System.out.print("Enter the UserID of the staff to update: ");
        String updateUserID = sc.nextLine();
        try {
            String[] staffToUpdate = staffManager.getStaffByUserID(updateUserID);
            if (staffToUpdate != null) {
                System.out.println("Enter new details for the staff (leave blank to keep current value):");
                System.out.print("Name [" + staffToUpdate[1] + "]: ");
                String newName = sc.nextLine();
                staffToUpdate[1] = newName.isEmpty() ? staffToUpdate[1] : newName;

                System.out.print("Role [" + staffToUpdate[2] + "]: ");
                String newRole = sc.nextLine();
                staffToUpdate[2] = newRole.isEmpty() ? staffToUpdate[2] : newRole;

                System.out.print("Gender [" + staffToUpdate[3] + "]: ");
                String newGender = sc.nextLine();
                staffToUpdate[3] = newGender.isEmpty() ? staffToUpdate[3] : newGender;

                System.out.print("Age [" + staffToUpdate[4] + "]: ");
                String newAge = sc.nextLine();
                staffToUpdate[4] = newAge.isEmpty() ? staffToUpdate[4] : newAge;

                System.out.print("Specialization [" + staffToUpdate[5] + "]: ");
                String newSpecialization = sc.nextLine();
                staffToUpdate[5] = newSpecialization.isEmpty() ? staffToUpdate[5] : newSpecialization;

                System.out.print("Contact [" + staffToUpdate[6] + "]: ");
                String newContact = sc.nextLine();
                staffToUpdate[6] = newContact.isEmpty() ? staffToUpdate[6] : newContact;

                staffManager.updateStaffInfo(updateUserID, staffToUpdate);
                System.out.println("Staff information updated successfully.");
            } else {
                System.out.println("Staff with UserID " + updateUserID + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error updating staff information: " + e.getMessage());
        }
    }

    // Remove staff
    private void removeStaff(Scanner sc) {
        System.out.print("Enter the UserID of the staff to remove: ");
        String removeUserID = sc.nextLine();

        try {
            boolean removed = staffManager.removeStaff(removeUserID);
            if (removed) {
                System.out.println("Staff removed successfully.");
            } else {
                System.out.println("Staff with UserID " + removeUserID + " not found.");
            }
        } catch (IOException e) {
            System.err.println("Error removing staff: " + e.getMessage());
        }
    }

    // Manage Medication Inventory
    private void manageMedicationInventory(Scanner sc) {
        System.out.println("=== View and Manage Medication Inventory ===");
        System.out.println("1. View Medication List");
        System.out.println("2. Update Stock Levels");
        System.out.println("3. Update Low Stock Level Alert");
        System.out.print("Please select an option (1-3): ");

        int medChoice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (medChoice) {
            case 1:
                getMedicationList();
                break;
            case 2:
                updateStockLevel(sc);
                break;
            case 3:
                updateLowStockLevel(sc);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Approve Replenishment Requests
    private void approveReplenishmentRequest(Scanner sc) {
        System.out.println("=== Approve Replenishment Requests ===");
        viewPendingRequests();
        System.out.print("Enter the requestID you want to approve: ");
        String requestID = sc.nextLine();
        approveRequest(requestID);
    }

    // View Medication List (from InventoryEditor interface)
    @Override
    public void getMedicationList() {
        try {
            List<String[]> inventoryData = medicineManager.getMedicineList();
            if (!inventoryData.isEmpty()) {
                System.out.println("=== Medication Inventory ===");
                for (String[] medData : inventoryData) {
                    System.out.println("Medicine ID: " + medData[0]);
                    System.out.println("Medicine Name: " + medData[1]);
                    System.out.println("Initial Stock: " + medData[2]);
                    System.out.println("Low Stock Level Alert: " + medData[3]);
                    System.out.println("Dosage Information: " + medData[4]);
                    System.out.println("Expiry Date: " + medData[5]);
                    System.out.println("--------------------------------------");
                }
            } else {
                System.out.println("No medication inventory data found.");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving medicine inventory: " + e.getMessage());
        }
    }

    // Update Stock Level (from InventoryEditor interface)
    @Override
    public void updateStockLevel(Scanner sc) {
        getMedicationList();
        System.out.print("Please enter the Medication ID to modify: ");
        String medID = sc.nextLine();

        System.out.println("What would you like to do?");
        System.out.println("1. Add stock");
        System.out.println("2. Remove stock");
        System.out.println("3. Change stock level");
        int choice = sc.nextInt();
        try {
            switch (choice) {
                case 1:
                    System.out.print("Enter the quantity to add: ");
                    int addQuantity = sc.nextInt();
                    medicineManager.updateMedicationStock(medID, addQuantity);
                    System.out.println("Stock added successfully.");
                    break;
                case 2:
                    System.out.print("Enter the quantity to remove: ");
                    int removeQuantity = sc.nextInt();
                    medicineManager.updateMedicationStock(medID, -removeQuantity);
                    System.out.println("Stock removed successfully.");
                    break;
                case 3:
                    System.out.print("Enter the new stock level: ");
                    int newStockLevel = sc.nextInt();
                    List<String[]> medicationsList = medicineManager.loadMedicationList();
                    String[] selectedMed = null;
                    for (String[] med : medicationsList) {
                        if (med[0].equals(medID)) {
                            selectedMed = med;
                            break;
                        }
                    }
                    int currentStock = Integer.parseInt(selectedMed[2].trim());
                    int difference = newStockLevel - currentStock;
                    medicineManager.updateMedicationStock(medID, difference);
                    System.out.println("Stock updated successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (IOException e) {
            System.err.println("Error updating stock level: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter a valid number.");
        }
    }

    // Update Low Stock Level (from InventoryEditor interface)
    @Override
    public void updateLowStockLevel(Scanner sc) {
        System.out.print("Enter Medication ID: ");
        String medID = sc.nextLine().trim();

        System.out.print("Enter New Low Stock Level: ");
        int newLowStockLevel;

        try {
            newLowStockLevel = sc.nextInt();
            sc.nextLine(); // Consume newline

            medicineManager.updateLowStockLevel(medID, newLowStockLevel);
            System.out.println("Low stock level updated successfully.");
        } catch (IOException e) {
            System.err.println("Error updating low stock level: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid stock level entered. Please enter a valid number.");
        }
    }

    // Approve replenishment request
    public void approveRequest(String requestID) {
        try {
            String[] request = orderRequestManager.getReplenishmentRequestByID(requestID);
            if (request == null) {
                System.out.println("Request with ID " + requestID + " not found.");
                return;
            }

            String currentStatus = request[4]; // Assuming Status is at index 4
            if (!"Pending".equalsIgnoreCase(currentStatus)) {
                System.out.println("Request is already " + currentStatus + ".");
                return;
            }

            String medID = request[1];
            int quantity;

            try {
                quantity = Integer.parseInt(request[3]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid quantity format for request " + requestID + ".");
                return;
            }

            medicineManager.updateMedicationStock(medID, quantity);
            System.out.println("Stock successfully updated for Medication ID: " + medID);

            orderRequestManager.updateReplenishmentRequestStatus(requestID, "Approved");
            System.out.println("Request " + requestID + " approved successfully.");

        } catch (IOException e) {
            System.err.println("Error approving request: " + e.getMessage());
        }
    }

    // View Appointment Details
    public void viewAppointmentDetails() {
        try {
            List<String[]> appointments = appointmentManager.getAppointments();
            Map<String, String[]> outcomeMap = appointmentOutcomeManager.mapAppointmentOutcomes();

            if (!appointments.isEmpty()) {
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                        "AppointmentID", "PatientID", "DoctorID", "Date", "Time", "Status");
                System.out.println("------------------------------------------------------------------------------------------------------------------");

                for (String[] appointment : appointments) {
                    System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                            appointment[0], appointment[1], appointment[2],
                            appointment[3], appointment[4], appointment[5]);
                }
            } else {
                System.out.println("No appointments available.");
            }
        } catch (IOException e) {
            System.err.println("Error retrieving appointment data: " + e.getMessage());
        }
    }

    // View pending replenishment requests
    public void viewPendingRequests() {
        try {
            List<String[]> pendingRequests = orderRequestManager.getPendingReplenishmentRequests();
            if (pendingRequests.isEmpty()) {
                System.out.println("No pending replenishment requests.");
            } else {
                System.out.println("Pending Replenishment Requests:");
                for (String[] request : pendingRequests) {
                    System.out.println("RequestID: " + request[0] +
                            ", MedID: " + request[1] +
                            ", MedicationName: " + request[2] +
                            ", Quantity: " + request[3] +
                            ", Status: " + request[4]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving pending requests: " + e.getMessage());
        }
    }
}