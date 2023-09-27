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

import exceptions.TypeCheck;
import interfaces.CRUD.VehicleCRUD;
import models.container.Container;
import utils.Constants;
import database.DatabaseHandler;
import models.vehicle.Vehicle;
import models.vehicle.Truck;
import models.vehicle.Ship;
import exceptions.InputValidation;
import utils.UiUtils;

import java.util.*;

public class VehicleServicesAdmin extends AdminBaseServices implements VehicleCRUD {

    private final Scanner scanner = new Scanner(System.in);
    private final String VEHICLE_FILE_PATH = Constants.VEHICLE_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final TripServicesAdmin tripController = new TripServicesAdmin();
    private final InputValidation inputValidation = new InputValidation();
    private final UiUtils uiUtils = new UiUtils();
    private final TypeCheck typeCheck = new TypeCheck();

    public List<Vehicle> fetchVehiclesFromDatabase() {
        List<Vehicle> vehiclesList;
        try {
            Vehicle[] vehiclesArray = (Vehicle[]) dbHandler.readObjects(VEHICLE_FILE_PATH);
            vehiclesList = new ArrayList<>(Arrays.asList(vehiclesArray));
        } catch (Exception e) {
            vehiclesList = new ArrayList<>();
        }
        return vehiclesList;
    }

    private Optional<Vehicle> findVehicleById(String vehicleId) {
        return fetchVehiclesFromDatabase().stream()
                .filter(vehicle -> vehicle.getVehicleId().equals(vehicleId))
                .findFirst();
    }

    public boolean uniqueVehicleIdCheck(String vehicleId) {
        if (findVehicleById(vehicleId).isPresent()) {
            uiUtils.printFailedMessage("Vehicle with ID " + vehicleId + " already exists.");
            return false;
        }
        return true;
    }

    @Override
    public void createNewVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("VEHICLE CREATION WIZARD", 100);
        System.out.println();

        String vehicleId;
        do {
            vehicleId = inputValidation.idValidation("V", "Enter vehicle ID to create:");
            System.out.println();
        } while (!uniqueVehicleIdCheck(vehicleId));

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
                newVehicle = new Truck(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, type);
            }
        } else if (!vehicleType.equalsIgnoreCase("Ship")) {
            uiUtils.printFailedMessage("Invalid vehicle type. Please enter a valid vehicle type.");
            return;
        } else {
            newVehicle = new Ship(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity);
        }

        List<Vehicle> vehiclesList = fetchVehiclesFromDatabase();
        vehiclesList.add(newVehicle);
        dbHandler.writeObjects(VEHICLE_FILE_PATH, vehiclesList.toArray(new Vehicle[0]));
    }

    @Override
    public void findVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("VEHICLE SEARCH WIZARD", 100);
        System.out.println();

        String vehicleIdToDisplay = inputValidation.idValidation("V", "Enter vehicle ID to search:");
        System.out.println();

        List<Vehicle> vehiclesList = fetchVehiclesFromDatabase();

        Vehicle vehicleToDisplay = null;
        for (Vehicle vehicle : vehiclesList) {
            if (vehicle.getVehicleId().equals(vehicleIdToDisplay)) {
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
        List<Vehicle> vehiclesList = fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("VEHICLES INFORMATION", 15, 20, 20, 25, 20, 15, 15);
        System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                "Vehicle ID", "Licensed Plate", "Current Fuel (l)", "Carrying Capacity (kg)",
                "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);

        for (Vehicle vehicle : vehiclesList) {
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
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);
    }

    @Override
    public void updateVehicle() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("VEHICLE UPDATE WIZARD", 100);
        System.out.println();

        String vehicleIdToUpdate = inputValidation.idValidation("V", "Enter vehicle ID to update:");
        System.out.println();

        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();

        Vehicle vehicleToUpdate = null;
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleId().equals(vehicleIdToUpdate)) {
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

        uiUtils.printFunctionName("VEHICLE DELETE WIZARD", 100);
        System.out.println();

        String vehicleIdToDelete = inputValidation.idValidation("V", "Enter vehicle ID to delete:");
        System.out.println();

        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();
        boolean isDeleted = false;
        Iterator<Vehicle> iterator = vehicleList.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            if (vehicle.getVehicleId().equals(vehicleIdToDelete)) {
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

    public Vehicle getVehicleById(String vehicleId) {
        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();

        Vehicle vehicleToReturn = null;
        for (Vehicle vehicle : vehicleList) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                vehicleToReturn = vehicle;
                break;
            }
        }
        return vehicleToReturn;
    }

    public void displayIdleVehicles() {
        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("IDLE VEHICLES", 15, 20, 20, 25, 20, 15, 15);
        System.out.printf("| %-15s | %-20s | %-20s | %-25s | %-20s | %-15s | %-15s |\n",
                "Vehicle ID", "Name", "Current Fuel (l)", "Carrying Capacity (kg)",
                "Fuel Capacity (l)", "Vehicle Type", "Truck Type");
        uiUtils.printHorizontalLine(15, 20, 20, 25, 20, 15, 15);
        for (Vehicle vehicle : vehicleList) {
            if (tripController.vehicleAvailCheck(vehicle)) {
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

    public void updateVehicleInDatabase(Vehicle vehicleToUpdate) {
        // Load all vehicles from the database
        List<Vehicle> vehicleList = fetchVehiclesFromDatabase();

        // Find the index of the vehicle to update
        int indexToUpdate = -1;
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).getVehicleId().equals(vehicleToUpdate.getVehicleId())) {
                indexToUpdate = i;
                break;
            }
        }

        // If vehicle was found in the list, replace it with the updated one and save back to the database
        if (indexToUpdate != -1) {
            vehicleList.set(indexToUpdate, vehicleToUpdate);

            // Save the updated list of vehicles back to the database
            dbHandler.writeObjects(VEHICLE_FILE_PATH, vehicleList.toArray(new Vehicle[0]));
        }
    }
}
