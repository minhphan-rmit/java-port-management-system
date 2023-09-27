package script;

import models.vehicle.Truck;
import models.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import database.DatabaseHandler;
import utils.Constants;

/**
 * VehiclePopulation is a class responsible for generating and managing vehicles
 * and storing them in a database.
 */
public class VehiclePopulation {

    private static final String VEHICLE_FILE_PATH = Constants.VEHICLE_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();

    /**
     * Generates a list of vehicles and stores them in the database.
     */
    public void run() {
        List<Vehicle> vehiclesList;
        try {
            // Read existing vehicles from the database if they exist
            Vehicle[] vehiclesArray = (Vehicle[]) dbHandler.readObjects(VEHICLE_FILE_PATH);
            vehiclesList = new ArrayList<>(Arrays.asList(vehiclesArray));
        } catch (Exception e) {
            vehiclesList = new ArrayList<>();
        }

        String[] licensePlates = {
                "29C-123.45", "30C-234.56", "31C-345.67", "32C-456.78", "33C-567.89",
                "34C-678.90", "35C-789.01", "36C-890.12", "37C-901.23", "38C-012.34",
                "39C-123.46", "40C-234.57", "41C-345.68", "42C-456.79", "43C-567.80",
                "44C-678.91", "45C-789.02", "46C-890.13", "47C-901.24", "48C-012.35",
                "49C-123.47", "50C-234.58", "51C-345.69", "52C-456.80", "53C-567.81",
                "54C-678.92", "55C-789.03", "56C-890.14", "57C-901.25", "58C-012.36",
                "59C-123.48", "60C-234.59", "61C-345.60", "62C-456.71", "63C-567.82",
                "64C-678.93", "65C-789.04", "66C-890.15", "67C-901.26", "68C-012.37",
                "69C-123.49", "70C-234.50", "71C-345.61", "72C-456.72", "73C-567.83",
                "74C-678.94", "75C-789.05", "76C-890.16", "77C-901.27", "78C-012.38"
        };

        // Generate Basic Trucks
        for (int i = 1; i <= 20; i++) {
            String vehicleId = String.format("V-%06d", i);
            String name = licensePlates[i - 1];
            double currentFuel = 0; // Assuming trucks start with no fuel
            double carryingCapacity = 20000 + (i - 1) * 500; // As per the data provided
            double fuelCapacity = 400 + (i - 1) * 10; // As per the data provided
            Truck.TruckType type = Truck.TruckType.valueOf("BASIC");
            vehiclesList.add(new Truck(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, type));
        }

        // Generate Reefer Trucks
        for (int i = 21; i <= 40; i++) {
            String vehicleId = String.format("V-%06d", i);
            String name = licensePlates[i - 1];
            double currentFuel = 0;
            double carryingCapacity = 15000 + (i - 21) * 500;
            double fuelCapacity = 420 + (i - 21) * 10;
            Truck.TruckType type = Truck.TruckType.valueOf("REEFER");
            vehiclesList.add(new Truck(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, type));
        }

        // Generate Tanker Trucks
        for (int i = 41; i <= 50; i++) {
            String vehicleId = String.format("V-%06d", i);
            String name = licensePlates[i - 1];
            double currentFuel = 0;
            double carryingCapacity = 25000 + (i - 41) * 1000;
            double fuelCapacity = 550 + (i - 41) * 10;
            Truck.TruckType type = Truck.TruckType.valueOf("TANKER");
            vehiclesList.add(new Truck(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, type));
        }

        // Write the updated vehicles list back to the database
        dbHandler.writeObjects(VEHICLE_FILE_PATH, vehiclesList.toArray(new Vehicle[0]));
    }

    /**
     * Empties the vehicle database by writing an empty array of vehicles.
     */
    public void emptyDatabase() {
        try {
            dbHandler.writeObjects(VEHICLE_FILE_PATH, new Vehicle[0]);
            // If you have other database files, you can add similar lines here to empty them too.
        } catch (Exception e) {
            System.out.println("Error emptying the database: " + e.getMessage());
        }
    }

    /**
     * The main method to execute vehicle population.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        VehiclePopulation vehiclePopulation = new VehiclePopulation();
        vehiclePopulation.emptyDatabase();
        vehiclePopulation.run();
    }
}

