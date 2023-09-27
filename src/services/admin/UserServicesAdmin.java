/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2023B
  Assessment: Group Assignment
  Group: Team Hi
  Members:
  Phan Nhat Minh - s3978598
  Huynh Duc Gia Tin - s3962053
  Nguyen Viet Ha - s3978128
  Vu Minh Ha - s3978681
  Created  date: 02/09/2023
  Acknowledgement: chat.openai.com, stackoverflow.com, geeksforgeeks.org, javatpoint.com, tutorialspoint.com, oracle.com, w3schools.com, github.com
*/

package services.admin;

import exceptions.InputValidation;
import interfaces.CRUD.UserCRUD;
import models.port.Port;
import models.user.SystemAdmin;
import utils.Constants;
import database.DatabaseHandler;
import models.user.User;
import models.user.PortManager;
import utils.UiUtils;

import java.util.*;

public class UserServicesAdmin extends AdminBaseServices implements UserCRUD {

    private final Scanner scanner = new Scanner(System.in);
    private final String USER_FILE_PATH = Constants.USER_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final UiUtils uiUtils = new UiUtils();
    private final InputValidation inputValidation = new InputValidation();

    public List<PortManager> fetchManagersFromDatabase() {
        try {
            PortManager[] managersArray = (PortManager[]) dbHandler.readObjects(USER_FILE_PATH);
            return new ArrayList<>(Arrays.asList(managersArray));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void createNewUser() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT MANAGER CREATION WIZARD", 100);
        System.out.println();

        String username = inputValidation.getUserInfo("Enter username: ");
        System.out.println();

        String password = inputValidation.getUserInfo("Enter password: ");
        System.out.println();

        String managedPortId = inputValidation.idValidation("P", "Enter managed port ID:");
        System.out.println();

        PortServicesAdmin portController = new PortServicesAdmin();
        Port managedPort = portController.getPortById(managedPortId);

        if (managedPort == null) {
            System.out.println("There is no port with ID " + managedPortId + " in the system, please try again.");
            return;
        }

        List<PortManager> managersList = fetchManagersFromDatabase();
        for (PortManager manager : managersList) {
            if (manager.getManagedPort().getPortId().equals(managedPortId)) {
                System.out.println("The port with ID " + managedPortId + " already has a manager assigned. Please try again.");
                return;
            }
        }

        PortManager newPortManager = new PortManager(username, password, managedPort);
        managersList.add(newPortManager);
        dbHandler.writeObjects(USER_FILE_PATH, managersList.toArray(new PortManager[0]));
        uiUtils.printSuccessMessage("Port Manager for port " + portController.getPortById(managedPortId).getName() + " created successfully.");
    }


    @Override
    public void findUser() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT MANAGER SEARCH WIZARD", 100);
        System.out.println();

        String usernameToDisplay = inputValidation.getUserInfo("Enter username to search: ");
        System.out.println();

        List<PortManager> managersList = fetchManagersFromDatabase();

        PortManager managerToDisplay = null;
        for (PortManager manager : managersList) {
            if (manager.getUsername().equals(usernameToDisplay)) {
                managerToDisplay = manager;
                break;
            }
        }

        if (managerToDisplay != null) {
            uiUtils.printTopBorderWithTableName("PORT MANAGER INFO", 15, 15, 25);
            System.out.printf("| %-15s | %-15s | %-25s |\n", "Username", "Password", "Managed Port ID");
            uiUtils.printHorizontalLine(15, 15, 25);
            System.out.printf("| %-15s | %-15s | %-25s |\n", managerToDisplay.getUsername(),
                    managerToDisplay.getPassword(), managerToDisplay.getManagedPort().getPortId());
            uiUtils.printHorizontalLine(15, 15, 25);
        } else {
            uiUtils.printFailedMessage("No PortManager found with the given username.");
        }
    }

    @Override
    public void displayAllUsers() {
        List<PortManager> managersList = fetchManagersFromDatabase();

        uiUtils.printTopBorderWithTableName("PORT MANAGERS INFORMATION", 15, 15, 25);
        System.out.printf("| %-15s | %-15s | %-25s |\n", "Username", "Password", "Managed Port ID");
        uiUtils.printHorizontalLine(15, 15, 25);
        for (PortManager manager : managersList) {
            System.out.printf("| %-15s | %-15s | %-25s |\n", manager.getUsername(), manager.getPassword(),
                    manager.getManagedPort().getPortId());
        }
        uiUtils.printHorizontalLine(15, 15, 25);
    }

    @Override
    public void updateUser() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT MANAGER UPDATE WIZARD", 100);
        System.out.println();

        String usernameToUpdate = inputValidation.getUserInfo("Enter username to update: ");

        List<PortManager> managersList = fetchManagersFromDatabase();

        PortManager managerToUpdate = null;
        for (PortManager manager : managersList) {
            if (manager.getUsername().equals(usernameToUpdate)) {
                managerToUpdate = manager;
                break;
            }
        }

        if (managerToUpdate != null) {
            System.out.print("Enter new password (leave blank to keep unchanged): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) {
                managerToUpdate.setPassword(password);
            }

            dbHandler.writeObjects(USER_FILE_PATH, managersList.toArray(new PortManager[0]));
            uiUtils.printSuccessMessage("PortManager with username " + usernameToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No PortManager found with the given username.");
        }
    }

    @Override
    public void deleteUser() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT MANAGER DELETION WIZARD", 100);
        System.out.println();

        String usernameToDelete = inputValidation.getUserInfo("Enter username to delete: ");
        System.out.println();

        List<PortManager> managersList = fetchManagersFromDatabase();

        boolean isDeleted = false;
        Iterator<PortManager> iterator = managersList.iterator();
        while (iterator.hasNext()) {
            PortManager manager = iterator.next();
            if (manager.getUsername().equals(usernameToDelete)) {
                iterator.remove();
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            dbHandler.writeObjects(USER_FILE_PATH, managersList.toArray(new PortManager[0]));
            uiUtils.printSuccessMessage("PortManager with username " + usernameToDelete + " deleted successfully.");
        } else {
            uiUtils.printFailedMessage("No PortManager found with the given username.");
        }
    }

    public User loginValidation(String username, String password) {
        if (adminValidation(username, password)) {
            return SystemAdmin.getInstance();  // Assuming SystemAdmin uses Singleton pattern as discussed
        } else {
            List<User> usersList;
            try {
                User[] usersArray = (User[]) dbHandler.readObjects(USER_FILE_PATH);
                usersList = new ArrayList<>(Arrays.asList(usersArray));
            } catch (Exception e) {
                uiUtils.printFailedMessage("Error reading users or no users exist.");
                return null;
            }

            for (User user : usersList) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    return user;  // Return the matched user
                }
            }
            return null;  // No user matched
        }
    }


    public boolean adminValidation(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }
}
