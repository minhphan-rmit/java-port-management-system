package services.manager;

import database.DatabaseHandler;
import exceptions.InputValidation;
import exceptions.TypeCheck;
import interfaces.manager.ManagerPortInterface;
import models.port.Port;
import models.user.PortManager;
import services.admin.PortServicesAdmin;
import utils.Constants;
import utils.CurrentUser;
import utils.UiUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PortServicesManager extends ManagerBaseServices implements ManagerPortInterface {

    private final Port managedPort;
    private final Scanner scanner = new Scanner(System.in);
    private final String PORT_FILE_PATH = Constants.PORT_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final UiUtils uiUtils = new UiUtils();
    private final InputValidation inputValidation = new InputValidation();
    private final TypeCheck typeCheck = new TypeCheck();
    private final PortServicesAdmin portServicesAdmin = new PortServicesAdmin();

    // Modularized method to fetch ports from the database
    private List<Port> fetchPortsFromDatabase() {
        try {
            Port[] portsArray = (Port[]) dbHandler.readObjects(PORT_FILE_PATH);
            return new ArrayList<>(Arrays.asList(portsArray));
        } catch (Exception e) {  // Catching a generic exception as a placeholder.
            uiUtils.printFailedMessage("Error reading ports or no ports exist.");
            return new ArrayList<>();
        }
    }

    public PortServicesManager() {
        if (CurrentUser.getUser() instanceof PortManager) {
            this.managedPort = ((PortManager) CurrentUser.getUser()).getManagedPort();
        } else {
            throw new IllegalStateException("The current user is not a Port Manager");
        }
    }

    public void viewPortInfo() {
        uiUtils.clearScreen();

        Port portToViewInfo = portServicesAdmin.getPortById(managedPort.getPortId());

        uiUtils.printFunctionName("YOUR PORT INFORMATION", 53);
        System.out.println();
        System.out.println("Port ID: " + portToViewInfo.getPortId());
        System.out.println("Port Name: " + portToViewInfo.getName());
        System.out.println("Port Latitude: " + portToViewInfo.getLatitude());
        System.out.println("Port Longitude: " + portToViewInfo.getLongitude());
        System.out.println("Port Storing Capacity: " + portToViewInfo.getStoringCapacity());
        System.out.println("Port Landing Ability: " + portToViewInfo.getLandingAbility());
        uiUtils.printHorizontalLine(53);
    }

    public void updatePortInfo() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("UPDATE PORT INFORMATION WIZARD", 53);
        System.out.println();

        String portIdToUpdate = managedPort.getPortId();

        List<Port> portsList = fetchPortsFromDatabase();

        Port portToUpdate = null;
        for (Port port : portsList) {
            if (port.getPortId().equals(portIdToUpdate)) {
                portToUpdate = port;
                break;
            }
        }

        if (portToUpdate != null) {
            System.out.print("Enter new port name (leave blank to keep unchanged): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                if (typeCheck.isString(name)) {
                    portToUpdate.setName(name.toUpperCase());
                }
            }

            System.out.print("Enter new port latitude (leave blank to keep unchanged): ");
            String latitudeStr = scanner.nextLine();
            if (!latitudeStr.isEmpty()) {
                if (typeCheck.isDouble(latitudeStr)) {
                    double latitude = Double.parseDouble(latitudeStr);
                    portToUpdate.setLatitude(latitude);
                }
            }

            System.out.print("Enter new port longitude (leave blank to keep unchanged): ");
            String longitudeStr = scanner.nextLine();
            if (!longitudeStr.isEmpty()) {
                if (typeCheck.isDouble(longitudeStr)) {
                    double longitude = Double.parseDouble(longitudeStr);
                    portToUpdate.setLongitude(longitude);
                }
            }

            System.out.print("Enter new port storing capacity (leave blank to keep unchanged): ");
            String capacityStr = scanner.nextLine();
            if (!capacityStr.isEmpty()) {
                if (typeCheck.isDouble(capacityStr)) {
                    double storingCapacity = Double.parseDouble(capacityStr);
                    portToUpdate.setStoringCapacity(storingCapacity);
                }
            }

            System.out.print("Enter new port landing ability (True/False, leave blank to keep unchanged): ");
            String landingAbilityStr = scanner.nextLine();
            if (!landingAbilityStr.isEmpty()) {
                if (typeCheck.isBoolean(landingAbilityStr)) {
                    boolean landingAbility = Boolean.parseBoolean(landingAbilityStr);
                    portToUpdate.setLandingAbility(landingAbility);
                }
            }

            dbHandler.writeObjects(PORT_FILE_PATH, portsList.toArray(new Port[0]));
            uiUtils.printSuccessMessage("Port with ID " + portIdToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No port found with the given ID.");
        }
    }
}
