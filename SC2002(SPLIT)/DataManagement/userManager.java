package DataManagement;

import java.io.IOException;
import java.util.List;

public class userManager {
    private final String userFilePath;
    private final String staffFilePath; 

    public userManager(String userFilePath, String staffFilePath) {
        this.userFilePath = userFilePath;
        this.staffFilePath = staffFilePath;
    }

    private List<String[]> readCSV(String filePath) throws IOException {
        return CSVReader.readCSV(filePath);
    }

    private void writeCSV(String filePath, List<String[]> data) throws IOException {
        CSVWriter.writeCSV(filePath, data);
    }

    private void appendToCSV(String filePath, String[] data) throws IOException {
        CSVWriter.appendToCSV(filePath, data);
    }

    public String[] getUserById(String userID) throws IOException {
        List<String[]> users = readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].equalsIgnoreCase(userID)) { 
                return user;
            }
        }
        return null; 
    }

    public String getUserName(String userID) throws IOException {
        List<String[]> users = readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equalsIgnoreCase(userID.trim())) {  
                return user[1].trim();  
            }
        }
        return null;  
    }

    public boolean login(String userID, String password) throws IOException {
        String[] user = getUserById(userID);
        return user != null && user[2].equals(password); 
    }

    public boolean updateUserPassword(String userID, String newPassword) throws IOException {
        List<String[]> users = readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equals(userID.trim())) {
                user[2] = newPassword; 
                writeCSV(userFilePath, users);
                return true;
            }
        }
        return false; 
    }

    public boolean isUserExists(String userID) throws IOException {
        return getUserById(userID) != null;
    }

    public List<String[]> getUsersList() throws IOException {
        return readCSV(userFilePath);
    }

    public void addUser(String[] newUser) throws IOException {
        appendToCSV(userFilePath, newUser);
    }

    public void updateUsersList(List<String[]> updatedUsers) throws IOException {
        writeCSV(userFilePath, updatedUsers);
    }

    public String getRoleByUserID(String userID) throws IOException {
        List<String[]> users = readCSV(userFilePath);
        for (String[] user : users) {
            if (user[0].trim().equalsIgnoreCase(userID.trim())) { 
                return user[3].trim(); 
            }
        }
        return null; 
    }

    public void updateUserInfo(String userID, String[] updatedUserDetails) throws IOException {
        // Assuming CSVReader is a utility to read CSV files into a List of String arrays
        List<String[]> userList = CSVReader.readCSV(userFilePath);
    
        boolean updated = false;
        for (String[] user : userList) {
            if (user[0].equals(userID)) {
                // Update the user details
                System.arraycopy(updatedUserDetails, 0, user, 0, updatedUserDetails.length);
                updated = true;
                break;
            }
        }
    
        if (updated) {
            // Write back the updated list to the CSV
            writeCSV(userFilePath, userList);
            System.out.println("User information updated successfully.");
        } else {
            System.out.println("User with UserID " + userID + " not found.");
        }
    }
}
