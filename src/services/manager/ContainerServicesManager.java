package services.manager;

import database.DatabaseHandler;
import exceptions.InputValidation;
import interfaces.CRUD.ContainerCRUD;
import interfaces.manager.ManagerContainerInterface;
import models.container.*;
import models.port.Port;
import models.user.PortManager;
import models.vehicle.Ship;
import models.vehicle.Vehicle;
import services.admin.ContainerServicesAdmin;
import services.admin.PortServicesAdmin;
import services.admin.VehicleServicesAdmin;
import utils.Constants;
import utils.CurrentUser;
import utils.UiUtils;

import java.util.*;

public class ContainerServicesManager extends ManagerBaseServices implements ContainerCRUD, ManagerContainerInterface {

    private final PortServicesAdmin portServicesAdmin = new PortServicesAdmin();
    private final ContainerServicesAdmin containerServicesAdmin = new ContainerServicesAdmin();
    private final VehicleServicesAdmin vehicleServicesAdmin = new VehicleServicesAdmin();
    private final Port managedPort;
    private final Scanner scanner = new Scanner(System.in);
    private final String CONTAINER_FILE_PATH = Constants.CONTAINER_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final UiUtils uiUtils = new UiUtils();
    private final InputValidation inputValidation = new InputValidation();
    private final VehicleServicesManager vehicleServicesManager = new VehicleServicesManager();

    public ContainerServicesManager() {
        if (CurrentUser.getUser() instanceof PortManager) {
            this.managedPort = ((PortManager) CurrentUser.getUser()).getManagedPort();
        } else {
            throw new IllegalStateException("The current user is not a Port Manager");
        }
    }

    // Modularized method to fetch containers from the database
    public List<Container> fetchContainersFromDatabase() {
        try {
            Container[] containersArray = (Container[]) dbHandler.readObjects(CONTAINER_FILE_PATH);
            return new ArrayList<>(Arrays.asList(containersArray));
        } catch (Exception e) {  // Catching a generic exception as a placeholder.
            uiUtils.printFailedMessage("Error reading containers or no containers exist.");
            return new ArrayList<>();
        }
    }

    public void loadContainerFlow() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("LOAD CONTAINER WIZARD", 100);
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();

        // Display all containers at the managed port
        uiUtils.printTopBorderWithTableName("ALL CONTAINERS AT YOUR PORT", 15, 20, 15);
        uiUtils.printHorizontalLine(15, 20 ,15);
        System.out.printf("| %-15s | %-20s | %-15s |\n",
                "Container ID", "Container Type", "Weight");
        uiUtils.printHorizontalLine(15, 20 ,15);
        for (Container container : containerList) {
            if (container.getCurrentPort() == null) {
                continue;
            }
            if (container.getCurrentVehicle() != null) {
                continue;
            }
            if (container.getCurrentPort().getName().equals(managedPort.getName())) {
                System.out.printf("| %-15s | %-20s | %-15.2f |\n", container.getContainerId(), container.getContainerType(), container.getWeight());
            }
        }
        uiUtils.printHorizontalLine(15, 20 ,15);
        System.out.println();

        // Get input for container id
        String containerId = inputValidation.idValidation("C", "Enter container ID you want to load: ");
        System.out.println();
        Container selectedContainer = containerServicesAdmin.getContainerById(containerId);
        if (selectedContainer.getCurrentPort() == null) {
            System.out.println("Container not found in the managed port.");
            return; // Exit or provide further options
        }
        if (!selectedContainer.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
            System.out.println("Container not found in the managed port.");
            return; // Exit or provide further options
        }

        // Show all ports except manager's port
        List<Port> portsList = portServicesAdmin.fetchPortsFromDatabase();
        uiUtils.printTopBorderWithTableName("AVAILABLE DESTINATION PORTS", 10, 30, 10, 10, 25, 25);
        System.out.printf("| %-10s | %-30s | %-10s | %-10s | %-25s | %-25s |\n",
                "Port ID", "Name", "Latitude", "Longitude", "Storing Capacity (kg)", "Landing Ability (T/F)");
        uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);
        for (Port port : portsList) {
            if (port.getPortId().equals(managedPort.getPortId())) {
                continue;
            }
            System.out.printf("| %-10s | %-30s | %-10.4f | %-10.4f | %,-25.2f | %-25b |\n",
                    port.getPortId(), port.getName(), port.getLatitude(), port.getLongitude(),
                    port.getStoringCapacity(), port.getLandingAbility());
        }
        uiUtils.printHorizontalLine(10, 30, 10, 10, 25, 25);

        // Get input for destination port
        String portId = inputValidation.idValidation("P", "Enter the destination port ID: ");
        System.out.println();
        Port destinationPort = portServicesAdmin.getPortById(portId);

        // Based on chosen container and port, display possible vehicles
        List<Vehicle> vehicleList = vehicleServicesAdmin.fetchVehiclesFromDatabase();
        uiUtils.printTopBorderWithTableName("AVAILABLE VEHICLES AT YOUR PORT", 15, 20);
        vehicleServicesManager.displayAllVehicles();
        uiUtils.printHorizontalLine(15, 20);

        // Get input for vehicle choice
        // Get input for vehicle id
        String vehicleId = inputValidation.idValidation("V", "Enter the vehicle ID you want to deploy:");
        System.out.println();
        Vehicle selectedVehicle = vehicleServicesAdmin.getVehicleById(vehicleId);

        if (selectedVehicle == null) {
            System.out.println("Vehicle with the provided ID not found.");
            return; // Exit or provide further options
        }

        if (selectedVehicle.canMoveToPort(destinationPort)) {
            if (selectedVehicle.canCarry(selectedContainer)) {
                if (selectedVehicle.getCurrentFuel() < selectedVehicle.calculateFuelNeeded(destinationPort)) {
                    while (true) { // Keep asking until a valid choice is made
                        System.out.println("Vehicle needs refueling. Do you want to refuel? (y/n)");
                        String refuelChoice = scanner.nextLine();
                        if ("y".equalsIgnoreCase(refuelChoice)) {
                            selectedVehicle.refuel();
                            break;
                        } else if ("n".equalsIgnoreCase(refuelChoice)) {
                            return; // Exit the function without refueling or doing anything further
                        } else {
                            System.out.println("Invalid choice. Please enter 'y' or 'n'.");
                        }
                    }
                }
            } else {
                System.out.println("Vehicle cannot carry the container.");
                return; // Exit or provide further options
            }
        } else {
            System.out.println("Vehicle cannot move to the destination port.");
            return; // Exit or provide further options
        }

        selectedVehicle.loadContainer(selectedContainer);
        selectedContainer.setCurrentVehicle(selectedVehicle);

        vehicleServicesAdmin.updateVehicleInDatabase(selectedVehicle);
        containerServicesAdmin.updateContainerInDatabase(selectedContainer);

        uiUtils.printSuccessMessage("Container loaded successfully!");
    }

    public void unloadContainerFlow() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("UNLOAD CONTAINER WIZARD", 100);
        System.out.println();


        // Display all loaded containers at the managed port
        List<Container> containerList = fetchContainersFromDatabase();
        uiUtils.printTopBorderWithTableName("LOADED CONTAINERS", 15, 20, 15);
        System.out.printf("| %-15s | %-20s | %-15s |\n", "Container ID", "Container Type", "Weight");
        uiUtils.printHorizontalLine(15, 20, 15);
        for (Container container : containerList) {
            if (container.getCurrentPort() == null && container.getCurrentVehicle() != null) {
                if (container.getCurrentVehicle().getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                    System.out.printf("| %-15s | %-20s | %-15.2f |\n", container.getContainerId(), container.getContainerType(), container.getWeight());
                }
            }
        }
        uiUtils.printHorizontalLine(15, 20, 15);
        System.out.println();

        // Get input for container id
        String containerId = inputValidation.idValidation("C", "Enter container ID you want to unload: ");
        System.out.println();
        Container selectedContainer = containerServicesAdmin.getContainerById(containerId);

        Vehicle vehicle = selectedContainer.getCurrentVehicle();

        vehicle.unloadContainer(selectedContainer);
        selectedContainer.setCurrentVehicle(null);
        selectedContainer.setCurrentPort(managedPort);

        vehicleServicesAdmin.updateVehicleInDatabase(vehicle);
        containerServicesAdmin.updateContainerInDatabase(selectedContainer);
        uiUtils.printSuccessMessage("Container unloaded successfully!");
    }

    public void addExistingContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("ADD EXISTING CONTAINER WIZARD", 100);
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();

        uiUtils.printTopBorderWithTableName("UNOCCUPIED CONTAINERS", 15, 20, 15);
        System.out.printf("| %-15s | %-20s | %-15s |\n", "Container ID", "Container Type", "Weight");
        uiUtils.printHorizontalLine(15, 20, 15);
        for (Container container : containerList) {
            if (container.getCurrentPort() == null) {
                System.out.printf("| %-15s | %-20s | %-15.2f |\n", container.getContainerId(), container.getContainerType(), container.getWeight());
            }
        }
        uiUtils.printHorizontalLine(15, 20, 15);

        String containerId = inputValidation.idValidation("C", "Enter container ID to add: ");
        System.out.println();

        Container containerToAdd = findUnoccupiedContainerById(containerList, containerId);

        if (containerToAdd == null) {
            uiUtils.printFailedMessage("No container found with the given ID.");
            return;
        }

        managedPort.addContainer(containerToAdd);
        containerToAdd.setCurrentPort(managedPort);

        containerServicesAdmin.updateContainerInDatabase(containerToAdd);
        portServicesAdmin.updatePortInDatabase(managedPort);

        uiUtils.printSuccessMessage("Container with ID " + containerId + " added successfully.");
    }

    private Container findUnoccupiedContainerById(List<Container> containerList, String containerId) {
        for (Container container : containerList) {
            if (container.getContainerId().equals(containerId) && container.getCurrentPort() == null) {
                return container;
            }
        }
        return null;
    }


    public void createNewContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CREATE NEW CONTAINER WIZARD", 100);
        System.out.println();

        String containerId = inputValidation.idValidation("C", "Enter container ID: ");
        System.out.println();

        double weight = inputValidation.getDouble("Enter container weight: ");
        System.out.println();

        System.out.println("Select container type:");
        System.out.println("1. Dry storage");
        System.out.println("2. Open Side");
        System.out.println("3. Open Top");
        System.out.println("4. Liquid");
        System.out.println("5. Refrigerated");
        int choice = inputValidation.getChoice("Enter your choice (1-5): ", 1, 5);

        Container newContainer = null;
        switch (choice) {
            case 1 -> newContainer = new DryStorage(containerId, weight, managedPort);
            case 2 -> newContainer = new OpenSide(containerId, weight, managedPort);
            case 3 -> newContainer = new OpenTop(containerId, weight, managedPort);
            case 4 -> newContainer = new Liquid(containerId, weight, managedPort);
            case 5 -> newContainer = new Refrigerated(containerId, weight, managedPort);
            default -> {
                uiUtils.printFailedMessage("Invalid choice.");
                return;
            }
        }

        // Write container to the database
        List<Container> containerList = fetchContainersFromDatabase();
        containerList.add(newContainer);
        dbHandler.writeObjects(CONTAINER_FILE_PATH, containerList.toArray(new Container[0]));

        // Update port in the database
        managedPort.addContainer(newContainer);
        portServicesAdmin.updatePortInDatabase(managedPort);
    }

    @Override
    public void findContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER SEARCH WIZARD", 100);
        System.out.println();

        String containerIdToDisplay = inputValidation.idValidation("C", "Enter container ID to search:");
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();

        Container containerToDisplay = null;
        for (Container container : containerList) {
            if (container.getCurrentPort() == null) {
                continue;
            }
            if (container.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                if (container.getContainerId().equals(containerIdToDisplay)) {
                    containerToDisplay = container;
                }
            }
        }

        if (containerToDisplay != null) {
            uiUtils.printTopBorderWithTableName("CONTAINER INFORMATION", 15, 20, 15);
            System.out.printf("| %-15s | %-20s | %-15s |\n", "Container ID", "Container Type", "Weight");
            uiUtils.printHorizontalLine(15, 20, 15);
            System.out.printf("| %-15s | %-20s | %-15.2f |\n", containerToDisplay.getContainerId(), containerToDisplay.getContainerType(), containerToDisplay.getWeight());
            uiUtils.printHorizontalLine(15, 20, 15);
        } else {
            uiUtils.printFailedMessage("No container found with the given ID.");
        }
    }

    @Override
    public void displayAllContainers() {
        System.out.println("DISPLAY ALL CONTAINERS INFO");

        List<Container> containerList = fetchContainersFromDatabase();

        System.out.println(managedPort.getPortId() + " containers:" + containerList.size());

        uiUtils.printHorizontalLine(15, 20, 15);
        System.out.printf("| %-15s | %-20s | %-15s |\n", "Container ID", "Container Type", "Weight");
        uiUtils.printHorizontalLine(15, 20, 15);
        for (Container container : containerList) {
            if (container.getCurrentPort() == null) {
                continue;
            }
            if (container.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                System.out.printf("| %-15s | %-20s | %-15.2f |\n", container.getContainerId(), container.getContainerType(), container.getWeight());
            }
        }
        uiUtils.printHorizontalLine(15, 20, 15);
    }

    public void updateContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER UPDATE WIZARD", 100);
        System.out.println();

        String containerIdToUpdate = inputValidation.idValidation("C", "Enter container ID to update: ");
        System.out.println();

        // Check if the container is in the managedPort
        Container managedPortContainer = null;
        for (Container container : managedPort.getCurrentContainers()) {
            if (container.getContainerId().equals(containerIdToUpdate)) {
                managedPortContainer = container;
                break;
            }
        }

        if (managedPortContainer == null) {
            System.out.println("Container not found in the managed port.");
            return;
        }

        List<Container> containerList = fetchContainersFromDatabase();

        Container containerToUpdate = null;
        int indexToUpdate = -1;  // new variable to keep track of the index in the list
        for (int i = 0; i < containerList.size(); i++) {
            if (containerList.get(i).getContainerId().equals(containerIdToUpdate)) {
                containerToUpdate = containerList.get(i);
                indexToUpdate = i;
                break;
            }
        }

        if (containerToUpdate != null) {
            System.out.print("Enter new container weight (leave blank to keep unchanged): ");
            String weightStr = scanner.nextLine();
            double newWeight = !weightStr.isEmpty() ? Double.parseDouble(weightStr) : containerToUpdate.getWeight();

            System.out.println("Select new container type (or leave blank to keep unchanged):");
            System.out.println("1. Dry storage");
            System.out.println("2. Open Side");
            System.out.println("3. Open Top");
            System.out.println("4. Liquid");
            System.out.println("5. Refrigerated storage");
            System.out.print("Enter your choice: ");
            String typeChoiceStr = scanner.nextLine();

            if (!typeChoiceStr.isEmpty()) {
                int typeChoice = Integer.parseInt(typeChoiceStr);
                switch (typeChoice) {
                    case 1 -> containerToUpdate = new DryStorage(containerIdToUpdate, newWeight);
                    case 2 -> containerToUpdate = new OpenSide(containerIdToUpdate, newWeight);
                    case 3 -> containerToUpdate = new OpenTop(containerIdToUpdate, newWeight);
                    case 4 -> containerToUpdate = new Liquid(containerIdToUpdate, newWeight);
                    case 5 -> containerToUpdate = new Refrigerated(containerIdToUpdate, newWeight);
                    default -> {
                        System.out.println("Invalid choice.");
                        return;
                    }
                }
                containerList.set(indexToUpdate, containerToUpdate);  // Replace the old container with the updated one
            } else if (!weightStr.isEmpty()) {
                containerToUpdate.setWeight(newWeight);
            }

            dbHandler.writeObjects(CONTAINER_FILE_PATH, containerList.toArray(new Container[0]));
            uiUtils.printSuccessMessage("Container with ID " + containerIdToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No container found with the given ID.");
        }
    }

    public void deleteContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER DELETE WIZARD", 100);
        System.out.println();

        String containerIdToDelete = inputValidation.idValidation("C", "Enter container ID to delete: ");
        System.out.println();

        // Check if the container is in the managedPort
        boolean existsInManagedPort = false;
        for (Container container : managedPort.getCurrentContainers()) {
            if (container.getContainerId().equals(containerIdToDelete)) {
                existsInManagedPort = true;
                break;
            }
        }

        if (!existsInManagedPort) {
            System.out.println("Container not found in the managed port.");
            return;
        }

        List<Container> containerList = fetchContainersFromDatabase();

        boolean isDeleted = false;
        Iterator<Container> iterator = containerList.iterator();
        while (iterator.hasNext()) {
            Container container = iterator.next();
            if (container.getContainerId().equals(containerIdToDelete)) {
                iterator.remove();
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            dbHandler.writeObjects(CONTAINER_FILE_PATH, containerList.toArray(new Container[0]));
            System.out.println("Container with ID " + containerIdToDelete + " deleted successfully.");
        } else {
            System.out.println("No container found with the given ID.");
        }
    }
}
