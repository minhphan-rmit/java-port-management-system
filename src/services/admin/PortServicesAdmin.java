/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2023B
  Assessment: Group Assignment
  Group: Team Hi
  Members:
  Phan Nhat Minh - s3978598
  Huynh Duc Gia Tin - s3818078
  Nguyen Viet Ha - s3978128
  Vu Minh Ha - s3978681
  Created  date: 02/09/2023
  Acknowledgement: chat.openai.com, stackoverflow.com, geeksforgeeks.org, javatpoint.com, tutorialspoint.com, oracle.com, w3schools.com, github.com, codejava.net, baeldung.com, mkyong.com, javacodegeeks.com, journaldev.com
*/

package services.admin;

import database.DatabaseHandler;
import exceptions.TypeCheck;
import interfaces.CRUD.PortCRUD;
import models.port.Port;
import utils.Constants;
import exceptions.InputValidation;
import utils.UiUtils;

import java.util.*;

public class PortServicesAdmin extends AdminBaseServices implements PortCRUD {

    private final Scanner scanner = new Scanner(System.in);
    private final String PORT_FILE_PATH = Constants.PORT_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final InputValidation inputValidation = new InputValidation();
    private final UiUtils uiUtils = new UiUtils();
    private final TypeCheck typeCheck = new TypeCheck();


    // Modularized method to fetch ports from the database
    public List<Port> fetchPortsFromDatabase() {
        try {
            Port[] portsArray = (Port[]) dbHandler.readObjects(PORT_FILE_PATH);
            return new ArrayList<>(Arrays.asList(portsArray));
        } catch (Exception e) {  // Catching a generic exception as a placeholder.
            uiUtils.printFailedMessage("Error reading ports from the database.");
            return new ArrayList<>();
        }
    }

    // Modularized method to write ports to the database
    private void writePortsToDatabase(List<Port> portsList) {
        dbHandler.writeObjects(PORT_FILE_PATH, portsList.toArray(new Port[0]));
    }

    // Modularized method to find a port by its ID
    private Optional<Port> findPortById(String portId) {
        return fetchPortsFromDatabase().stream()
                .filter(port -> port.getPortId().equals(portId))
                .findFirst();
    }

    @Override
    public void createNewPort() {
        uiUtils.clearScreen();  // Optional: Clear the console for a clean UI. Implementation depends on the OS.

        uiUtils.printFunctionName("PORT CREATION WIZARD", 100);
        System.out.println();  // Blank line for spacing

        String portId = inputValidation.idValidation("P", "Enter port ID to create: ");
        System.out.println();  // Blank line for spacing

        String name = inputValidation.getString("Enter port name: ");
        System.out.println();  // Blank line for spacing

        double latitude = inputValidation.getDouble("Enter port latitude (e.g., 52.5200): ");
        System.out.println();  // Blank line for spacing

        double longitude = inputValidation.getDouble("Enter port longitude (e.g., 13.4050): ");
        System.out.println();  // Blank line for spacing

        double storingCapacity = inputValidation.getDouble("Enter port storing capacity (in tons): ");
        System.out.println();  // Blank line for spacing

        boolean landingAbility = inputValidation.getBoolean("Does the port have landing ability? (True/False): ");
        System.out.println();  // Blank line for spacing

        Port newPort = new Port(portId, name, latitude, longitude, storingCapacity, landingAbility);

        List<Port> portsList = fetchPortsFromDatabase();
        portsList.add(newPort);
        writePortsToDatabase(portsList);

        // Confirmation message
        uiUtils.printSuccessMessage("Port with ID " + portId + " created successfully.");
    }

    @Override
    public void findPort() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT SEARCH WIZARD", 100);
        System.out.println();

        String portIdToDisplay = inputValidation.idValidation("P", "Enter port ID to search: ");
        System.out.println();

        Optional<Port> optionalPort = findPortById(portIdToDisplay);


        if (optionalPort.isPresent()) {
            Port portToDisplay = optionalPort.get();
            String name = portToDisplay.getName();
            uiUtils.printTopBorderWithTableName( name + " INFORMATION", 10, 30, 10, 10, 25, 25);
            System.out.printf("| %-10s | %-30s | %-10s | %-10s | %-25s | %-25s |\n",
                    "Port ID", "Name", "Latitude", "Longitude", "Storing Capacity (kg)", "Landing Ability (T/F)");
            uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);
            displayPortTableRow(portToDisplay);
            uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);
        } else {
            uiUtils.printFailedMessage("No port found with the given ID.");
        }
    }

    @Override
    public void displayAllPorts() {
        uiUtils.clearScreen();

        List<Port> portsList = fetchPortsFromDatabase();

        uiUtils.printTopBorderWithTableName("PORTS INFORMATION", 10, 30, 10, 10, 25, 25);
        System.out.printf("| %-10s | %-30s | %-10s | %-10s | %-25s | %-25s |\n",
                "Port ID", "Name", "Latitude", "Longitude", "Storing Capacity (kg)", "Landing Ability (T/F)");
        uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);
        for (Port port : portsList) {
            displayPortTableRow(port);
        }
        uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);
    }

    public void displayPortTableRow(Port port) {
        System.out.printf("| %-10s | %-30s | %-10.4f | %-10.4f | %,-25.2f | %-25b |\n",
                port.getPortId(), port.getName(), port.getLatitude(), port.getLongitude(),
                port.getStoringCapacity(), port.getLandingAbility());
    }

    @Override
    public void updatePort() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT UPDATE WIZARD", 100);
        System.out.println();

        String portIdToUpdate = inputValidation.idValidation("P", "Enter port ID to update: ");
        System.out.println();

        List<Port> portsList = fetchPortsFromDatabase();

        Optional<Port> portToUpdateOpt = findPortById(portIdToUpdate);

        if (portToUpdateOpt.isPresent()) {
            Port portToUpdate = portToUpdateOpt.get();

            System.out.println("Enter new port name (leave blank to keep unchanged): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                if (typeCheck.isString(name)) {
                    portToUpdate.setName(name.toUpperCase());
                }
            }

            System.out.println("Enter new port latitude (leave blank to keep unchanged): ");
            String latitudeStr = scanner.nextLine();
            if (!latitudeStr.isEmpty()) {
                if (typeCheck.isDouble(latitudeStr)) {
                    double latitude = Double.parseDouble(latitudeStr);
                    portToUpdate.setLatitude(latitude);
                }
            }

            System.out.println("Enter new port longitude (leave blank to keep unchanged): ");
            String longitudeStr = scanner.nextLine();
            if (!longitudeStr.isEmpty()) {
                if (typeCheck.isDouble(longitudeStr)) {
                    double longitude = Double.parseDouble(longitudeStr);
                    portToUpdate.setLongitude(longitude);
                }
            }

            System.out.println("Enter new port storing capacity in kilograms (leave blank to keep unchanged): ");
            String capacityStr = scanner.nextLine();
            if (!capacityStr.isEmpty()) {
                if (typeCheck.isDouble(capacityStr)) {
                    double storingCapacity = Double.parseDouble(capacityStr);
                    portToUpdate.setStoringCapacity(storingCapacity);
                }
            }

            System.out.println("Enter new port landing ability (True/False), leave blank to keep unchanged): ");
            String landingAbilityStr = scanner.nextLine();
            if (!landingAbilityStr.isEmpty()) {
                if (typeCheck.isBoolean(landingAbilityStr)) {
                    boolean landingAbility = Boolean.parseBoolean(landingAbilityStr);
                    portToUpdate.setLandingAbility(landingAbility);
                }
            }

            updatePortInDatabase(portToUpdate);
            uiUtils.printSuccessMessage("Port with ID " + portIdToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No port found with the given ID.");
        }
    }


    @Override
    public void deletePort() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("PORT DELETION WIZARD", 100);
        System.out.println();

        String portIdToDelete = inputValidation.idValidation("P", "Enter port ID to delete: ");
        System.out.println();

        List<Port> portsList = fetchPortsFromDatabase();
        boolean isDeleted = portsList.removeIf(port -> port.getPortId().equals(portIdToDelete));
        if (isDeleted) {
            writePortsToDatabase(portsList);
            uiUtils.printSuccessMessage("Port with ID " + portIdToDelete + " deleted successfully.");
        } else {
            uiUtils.printFailedMessage("No port found with the given ID.");
        }
    }


    public Port getPortById(String portId) {
        return findPortById(portId).orElse(null);
    }

    public void updatePortInDatabase(Port portToUpdate) {
        // Load all ports from the database
        List<Port> portList = fetchPortsFromDatabase();

        // Find the index of the port to update
        int indexToUpdate = -1;
        for (int i = 0; i < portList.size(); i++) {
            if (portList.get(i).getPortId().equals(portToUpdate.getPortId())) {
                indexToUpdate = i;
                break;
            }
        }

        // If port was found in the list, replace it with the updated one and save back to the database
        if (indexToUpdate != -1) {
            portList.set(indexToUpdate, portToUpdate);

            // Assuming you have a method to save a list of ports to the database, something like:
            writePortsToDatabase(portList);
        }
    }
}
