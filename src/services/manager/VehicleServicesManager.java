package services.manager;

import database.DatabaseHandler;
import exceptions.InputValidation;
import exceptions.TypeCheck;
import interfaces.CRUD.VehicleCRUD;
import interfaces.manager.ManagerVehicleInterface;
import models.container.Container;
import models.port.Port;
import models.trip.Trip;
import models.user.PortManager;
import models.vehicle.Ship;
import models.vehicle.Truck;
import models.vehicle.Vehicle;
import services.admin.PortServicesAdmin;
import services.admin.TripServicesAdmin;
import services.admin.VehicleServicesAdmin;
import utils.Constants;
import utils.CurrentUser;
import utils.DateUtils;
import utils.UiUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class VehicleServicesManager extends ManagerBaseServices implements VehicleCRUD, ManagerVehicleInterface {

    private final Port managedPort;
    private final Scanner scanner = new Scanner(System.in);
    private final String TRIP_FILE_PATH = Constants.TRIP_FILE_PATH;
    private final String VEHICLE_FILE_PATH = Constants.VEHICLE_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private final VehicleServicesAdmin vehicleController = new VehicleServicesAdmin();
    private final PortServicesAdmin portController = new PortServicesAdmin();
    private final TripServicesAdmin tripController = new TripServicesAdmin();
    private final DateUtils dateUtils = new DateUtils();
    private final InputValidation inputValidation = new InputValidation();
    private final UiUtils uiUtils = new UiUtils();
    private final TypeCheck typeCheck = new TypeCheck();

    public VehicleServicesManager() {
        if (CurrentUser.getUser() instanceof PortManager) {
            this.managedPort = ((PortManager) CurrentUser.getUser()).getManagedPort();
        } else {
            throw new IllegalStateException("The current user is not a Port Manager");
        }
    }

    public void refuelVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("REFUEL VEHICLE", 82);
        System.out.println();
        System.out.println();

        // Display all vehicles at the managed port
        List<Vehicle> vehicles = vehicleController.fetchVehiclesFromDatabase();
        uiUtils.printTopBorderWithTableName("VEHICLES AT YOUR PORT", 15, 15, 20, 20);
        System.out.printf("| %-15s | %-15s | %-20s | %-20s |\n", "Vehicle ID", "Licensed Plate", "Current Fuel", "Fuel Capacity");
        uiUtils.printHorizontalLine(15, 15, 20, 20);
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getCurrentPort() == null) {
                continue;
            }
            if (vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                System.out.printf("| %-15s | %-15s | %-20.2f | %-20.2f |\n", vehicle.getVehicleId(), vehicle.getName(), vehicle.getCurrentFuel(), vehicle.getFuelCapacity());
            }
        }
        uiUtils.printHorizontalLine(15, 15, 20, 20);
        System.out.println();


        // Get input for vehicle id
        String vehicleId = inputValidation.idValidation("V", "Enter the vehicle ID you want to refuel:");
        System.out.println();
        Vehicle selectedVehicle = null;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                if (vehicle.getCurrentPort() == null) {
                    uiUtils.printFailedMessage("This vehicle is not at your port!");
                    return;
                }
                if (vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                    selectedVehicle = vehicleController.getVehicleById(vehicleId);
                    break;  // Use break here instead of return
                }
            }
        }

        if (selectedVehicle != null) {
            // Process the fueling
            selectedVehicle.refuel();
            vehicleController.updateVehicleInDatabase(selectedVehicle);
            uiUtils.printSuccessMessage("Refuel successful!");
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }
    }

    public void deployVehicle() throws ParseException {

        // Display all vehicles at the managed port
        List<Vehicle> vehicleList = vehicleController.fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("IDLE VEHICLES", 15, 20, 20, 25, 20, 15, 15);
        System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                "Vehicle ID", "Name", "Current Fuel (l)", "Carrying Capacity (kg)",
                "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);
        for (Vehicle vehicle : vehicleList) {
            if (tripController.vehicleAvailCheck(vehicle)) {
                if (vehicle.getCurrentPort() == null) {
                    continue;
                }
                if (vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                    String vehicleType = "";
                    String truckType = "N/A";

                    if (vehicle instanceof Truck) {
                        vehicleType = "Truck";
                        truckType = ((Truck) vehicle).getType();
                    } else if (vehicle instanceof Ship) {
                        vehicleType = "Ship";
                    }

                    System.out.printf("| %-15s | %-20s | %-20.2f | %-25.2f | %-20.2f | %-15s | %-15s |\n",
                            vehicle.getVehicleId(), vehicle.getName(), vehicle.getCurrentFuel(),
                            vehicle.getCarryingCapacity(), vehicle.getFuelCapacity(), vehicleType, truckType);
                }
            }
        }
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

        // Get input for vehicle id
        String vehicleId = inputValidation.idValidation("V", "Enter the vehicle ID you want to deploy:");
        System.out.println();
        Vehicle selectedVehicle = vehicleController.getVehicleById(vehicleId);

        if (selectedVehicle != null) {
            // Ask for destination port
            System.out.println("Enter the destination port ID:");
            String destinationPortId = inputValidation.idValidation("P", "Enter the destination port ID:");
            Port selectedDestinationPort = portController.getPortById(destinationPortId);

            // Ask for departure date
            System.out.println("Enter the departure date (dd-MM-yyyy):");
            String departureDateString = scanner.nextLine();
            Date departureDate = sdf.parse(departureDateString);

            // Calculate arrival date
            Date arrivalDate = sdf.parse(dateUtils.hoursToDate(selectedVehicle.calculateTimeNeeded(selectedDestinationPort), "yyyy-MM-dd"));

            // Calculate fuel needed
            double fuelNeeded = selectedVehicle.calculateFuelNeeded(selectedDestinationPort);
            // Check if the vehicle has enough fuel
            if (fuelNeeded > selectedVehicle.getCurrentFuel()) {
                System.out.println("Not enough fuel! Do you want to refuel? (Y/N)");
                String refuelChoice = scanner.nextLine();
                if (refuelChoice.equals("Y")) {
                    selectedVehicle.refuel();
                    System.out.println("Refuel successful!");
                } else {
                    System.out.println("Refuel cancelled.");
                    return;
                }
            }

            // Create the trip
            Trip newTrip = new Trip(generateNextTripId(), selectedVehicle, managedPort, selectedDestinationPort, departureDate);

            List<Trip> tripList = tripController.fetchTripsFromDatabase();
            tripList.add(newTrip);
            dbHandler.writeObjects(TRIP_FILE_PATH, tripList.toArray(new Trip[0]));

            // Send message to the user
            uiUtils.printSuccessMessage("Vehicle deployed successfully!");
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }
    }

    public void unloadVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("UNLOAD VEHICLE", 82);
        System.out.println();

        // Display all vehicles at the managed port
        List<Vehicle> vehicles = vehicleController.fetchVehiclesFromDatabase();
        uiUtils.printTopBorderWithTableName("LOADED VEHICLES AT YOUR PORT", 15, 15, 20, 20);
        System.out.printf("| %-15s | %-15s | %-20s | %-20s |\n", "Vehicle ID", "Licensed Plate", "Current Fuel", "Fuel Capacity");
        uiUtils.printHorizontalLine(15, 15, 20, 20);
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getCurrentPort() == null) {
                continue;
            }
            if (vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                if (!vehicle.getContainers().isEmpty()) {
                    System.out.printf("| %-15s | %-15s | %-20.2f | %-20.2f |\n", vehicle.getVehicleId(), vehicle.getName(), vehicle.getCurrentFuel(), vehicle.getFuelCapacity());
                }
            }
        }
        uiUtils.printHorizontalLine(15, 15, 20, 20);

        // Get input for vehicle id
        String vehicleId = inputValidation.idValidation("V", "Enter the vehicle ID you want to unload:");
        System.out.println();
        Vehicle selectedVehicle = vehicleController.getVehicleById(vehicleId);

        if (selectedVehicle != null) {
            selectedVehicle.emptyContainers();
            vehicleController.updateVehicleInDatabase(selectedVehicle);
            uiUtils.printSuccessMessage("Unload successful!");
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }
    }

    private String generateNextTripId() {
        List<Trip> tripsList = tripController.fetchTripsFromDatabase();

        if (tripsList.isEmpty()) {
            return "T-000001"; // Starting ID
        } else {
            String lastTripId = tripsList.get(tripsList.size() - 1).getTripId();
            int nextId = Integer.parseInt(lastTripId.substring(2)) + 1;  // start at index 2 to skip "T-"
            return String.format("T-%06d", nextId);
        }
    }

    public void addExistingVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("ADD EXISTING VEHICLE WIZARD", 100);


        List<Vehicle> vehicleList = vehicleController.fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("UNOCCUPIED VEHICLES", 15, 20, 20, 25, 20, 15, 15);
        System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                "Vehicle ID", "Name", "Current Fuel (l)", "Carrying Capacity (kg)",
                "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getCurrentPort() == null) {
                String vehicleType = "";
                String truckType = "N/A";

                if (vehicle instanceof Truck) {
                    vehicleType = "Truck";
                    truckType = ((Truck) vehicle).getType();
                } else if (vehicle instanceof Ship) {
                    vehicleType = "Ship";
                }

                System.out.printf("| %-15s | %-20s | %-20.2f | %-25.2f | %-20.2f | %-15s | %-15s |\n",
                        vehicle.getVehicleId(), vehicle.getName(), vehicle.getCurrentFuel(),
                        vehicle.getCarryingCapacity(), vehicle.getFuelCapacity(), vehicleType, truckType);
            }
        }
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

        String vehicleId = inputValidation.idValidation("V", "Enter the vehicle ID you want to add:");
        System.out.println();

        Vehicle vehicleToAdd = findUnoccupiedVehicleById(vehicleList, vehicleId);

        if (vehicleToAdd == null) {
            uiUtils.printFailedMessage("No unoccupied vehicle with ID " + vehicleId + " found!");
            return;
        }

        managedPort.addVehicle(vehicleToAdd);
        vehicleToAdd.setCurrentPort(managedPort);

        vehicleController.updateVehicleInDatabase(vehicleToAdd);

        uiUtils.printSuccessMessage("Vehicle with ID " + vehicleId + " added successfully!");
    }

    private Vehicle findUnoccupiedVehicleById(List<Vehicle> vehicleList, String vehicleId) {
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.getCurrentPort() == null) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public void createNewVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("VEHICLE CREATION WIZARD", 100);
        System.out.println();

        String vehicleId;
        do {
            vehicleId = inputValidation.idValidation("C", "Enter vehicle ID to create: ");
        } while (!vehicleController.uniqueVehicleIdCheck(vehicleId));  // Keep asking until a unique ID is provided
        System.out.println();

        String name = inputValidation.getString("Enter vehicle name:");
        System.out.println();

        double currentFuel = inputValidation.getDouble("Enter current fuel: ");
        System.out.println();

        double carryingCapacity = inputValidation.getDouble("Enter carrying capacity: ");
        System.out.println();

        double fuelCapacity = inputValidation.getDouble("Enter fuel capacity: ");
        System.out.println();

        String vehicleType = inputValidation.getString("Enter vehicle type (Truck/Ship): ");
        System.out.println();

        Vehicle newVehicle;
        if (vehicleType.equalsIgnoreCase("Truck")) {
            System.out.print("Enter truck type (BASIC/REEFER/TANKER): ");
            if (!scanner.hasNextLine()) {
                uiUtils.printFailedMessage("Invalid truck type. Please enter a valid truck type.");
                return;
            } else if (!scanner.nextLine().trim().toUpperCase().matches("BASIC|REEFER|TANKER")) {
                uiUtils.printFailedMessage("Invalid truck type. Please enter a valid truck type.");
                return;
            } else {
                Truck.TruckType type = Truck.TruckType.valueOf(scanner.nextLine().toUpperCase());
                newVehicle = new Truck(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, type, managedPort);
            }
        } else {
            newVehicle = new Ship(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, managedPort);
        }

        List<Vehicle> vehiclesList = vehicleController.fetchVehiclesFromDatabase();
        vehiclesList.add(newVehicle);
        dbHandler.writeObjects(VEHICLE_FILE_PATH, vehiclesList.toArray(new Vehicle[0]));
        uiUtils.printSuccessMessage("Vehicle with ID " + vehicleId + " created successfully.");
    }

    @Override
    public void findVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("VEHICLE SEARCH WIZARD", 100);
        System.out.println();

        String vehicleIdToDisplay = inputValidation.idValidation("V", "Enter vehicle ID to search:");
        System.out.println();

        List<Vehicle> vehiclesList = vehicleController.fetchVehiclesFromDatabase();

        Vehicle vehicleToDisplay = null;
        for (Vehicle vehicle : vehiclesList) {
            if (vehicle.getVehicleId().equals(vehicleIdToDisplay) && vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                vehicleToDisplay = vehicle;
                break;
            }
        }

        if (vehicleToDisplay != null) {
            String vehicleType = "";
            String truckType = "N/A";

            if (vehicleToDisplay instanceof Truck) {
                vehicleType = "Truck";
                truckType = ((Truck) vehicleToDisplay).getType();
            } else if (vehicleToDisplay instanceof Ship) {
                vehicleType = "Ship";
            }

            uiUtils.printTopBorderWithTableName("VEHICLE INFORMATION", 15, 20, 20, 25, 20, 15, 15);
            System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                    "Vehicle ID", "Licensed Plate", "Current Fuel (l)", "Carrying Capacity (kg)",
                    "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
            uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

            System.out.printf("| %-15s | %-20s | %-20.2f | %-25.2f | %-20.2f | %-15s | %-15s |\n",
                    vehicleToDisplay.getVehicleId(), vehicleToDisplay.getName(), vehicleToDisplay.getCurrentFuel(),
                    vehicleToDisplay.getCarryingCapacity(), vehicleToDisplay.getFuelCapacity(), vehicleType, truckType);
            uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }

    }

    @Override
    public void displayAllVehicles() {
        uiUtils.clearScreen();

        List<Vehicle> vehiclesList = vehicleController.fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("VEHICLES INFORMATION", 15, 20, 20, 25, 20, 15, 15);
        System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                "Vehicle ID", "Licensed Plate", "Current Fuel (l)", "Carrying Capacity (kg)",
                "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

        for (Vehicle vehicle : vehiclesList) {
            if (vehicle.getCurrentPort() == null) {
                continue;
            }
            if (vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                String vehicleType = "";
                String truckType = "N/A";

                if (vehicle instanceof Truck) {
                    vehicleType = "Truck";
                    truckType = ((Truck) vehicle).getType();
                } else if (vehicle instanceof Ship) {
                    vehicleType = "Ship";
                }

                System.out.printf("| %-15s | %-20s | %-20.2f | %-25.2f | %-20.2f | %-15s | %-15s |\n",
                        vehicle.getVehicleId(), vehicle.getName(), vehicle.getCurrentFuel(),
                        vehicle.getCarryingCapacity(), vehicle.getFuelCapacity(), vehicleType, truckType);
            }
        }
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

    }

    @Override
    public void updateVehicle() {
        uiUtils.clearScreen();

        displayAllVehicles();

        uiUtils.printFunctionName("VEHICLE UPDATE WIZARD", 100);
        System.out.println();

        String vehicleIdToUpdate = inputValidation.idValidation("V", "Enter vehicle ID to update:");
        System.out.println();

        List<Vehicle> vehicleList = vehicleController.fetchVehiclesFromDatabase();

        Vehicle vehicleToUpdate = null;
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleId().equals(vehicleIdToUpdate) && vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                vehicleToUpdate = vehicle;
                break;
            }
        }

        if (vehicleToUpdate != null) {
            System.out.print("Enter new vehicle name (leave blank to keep unchanged): ");
            String name = scanner.nextLine();
            if (!name.isEmpty()) {
                if (typeCheck.isString(name)) {
                    vehicleToUpdate.setName(name);
                }
            }

            System.out.print("Enter new current fuel amount (leave blank to keep unchanged): ");
            String currentFuelStr = scanner.nextLine();
            if (!currentFuelStr.isEmpty()) {
                if (typeCheck.isDouble(currentFuelStr)) {
                    double currentFuel = Double.parseDouble(currentFuelStr);
                    vehicleToUpdate.setCurrentFuel(currentFuel);
                }
            }

            System.out.print("Enter new carrying capacity (leave blank to keep unchanged): ");
            String carryingCapacityStr = scanner.nextLine();
            if (!carryingCapacityStr.isEmpty()) {
                if (typeCheck.isDouble(carryingCapacityStr)) {
                    double carryingCapacity = Double.parseDouble(carryingCapacityStr);
                    vehicleToUpdate.setCarryingCapacity(carryingCapacity);
                }
            }

            System.out.print("Enter new fuel capacity (leave blank to keep unchanged): ");
            String fuelCapacityStr = scanner.nextLine();
            if (!fuelCapacityStr.isEmpty()) {
                if (typeCheck.isDouble(fuelCapacityStr)) {
                    double fuelCapacity = Double.parseDouble(fuelCapacityStr);
                    vehicleToUpdate.setFuelCapacity(fuelCapacity);
                }
            }

            dbHandler.writeObjects(Constants.VEHICLE_FILE_PATH, vehicleList.toArray(new Vehicle[0]));
            uiUtils.printSuccessMessage("Vehicle with ID " + vehicleIdToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }
    }

    @Override
    public void deleteVehicle() {
        uiUtils.clearScreen();

        displayAllVehicles();

        uiUtils.printFunctionName("VEHICLE DELETE WIZARD", 100);
        System.out.println();

        String vehicleIdToDelete = inputValidation.idValidation("V", "Enter vehicle ID to delete:");
        System.out.println();

        List<Vehicle> vehicleList = vehicleController.fetchVehiclesFromDatabase();
        boolean isDeleted = false;
        Iterator<Vehicle> iterator = vehicleList.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            if (vehicle.getVehicleId().equals(vehicleIdToDelete) && vehicle.getCurrentPort().getPortId().equals(managedPort.getPortId())) {
                iterator.remove();
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            dbHandler.writeObjects(Constants.VEHICLE_FILE_PATH, vehicleList.toArray(new Vehicle[0]));
            uiUtils.printSuccessMessage("Vehicle with ID " + vehicleIdToDelete + " deleted successfully.");
        } else {
            uiUtils.printFailedMessage("No vehicle found with the given ID.");
        }
    }
}

