package services.statistics;

import interfaces.statistics.VehicleStatInterface;
import models.vehicle.Ship;
import models.vehicle.Truck;
import models.vehicle.Vehicle;
import services.admin.VehicleServicesAdmin;
import utils.UiUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VehicleStatistics extends Statistics implements VehicleStatInterface {

    private final VehicleServicesAdmin vehicleController = new VehicleServicesAdmin();
    private final UiUtils uiUtils = new UiUtils();

    public void displayTotalNumberOfVehicles() {
        int total = vehicleController.fetchVehiclesFromDatabase().size();

        uiUtils.printTopBorderWithTableName("Total number of vehicles in the system", 53);
        System.out.printf("| %-53s |\n", total + " vehicles");
        uiUtils.printHorizontalLine(53);
    }

    public void vehicleStatus() {
        int total = vehicleController.fetchVehiclesFromDatabase().size();
        String[] statuses = {"In port", "In transit", "Awaiting loading", "Awaiting unloading"};

        uiUtils.printTopBorderWithTableName("Vehicle status", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Status", "Count");
        uiUtils.printHorizontalLine(30, 20);
        for (String status : statuses) {
            long count = vehicleController.fetchVehiclesFromDatabase().stream()
                    .filter(vehicle -> vehicle.getVehicleStatus().equals(status))
                    .count();
            System.out.printf("| %-30s | %-20d |\n", status, count);
        }
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }

    public void vehicleType() {
        List<Vehicle> vehicles = vehicleController.fetchVehiclesFromDatabase();
        int total = vehicles.size();

        Map<String, Long> vehicleCounts = new HashMap<>();

        vehicleCounts.put("Ship", vehicles.stream().filter(v -> v instanceof Ship).count());
        vehicleCounts.put("BASIC", vehicles.stream().filter(v -> v instanceof Truck && ((Truck) v).getType().equals("BASIC")).count());
        vehicleCounts.put("REEFER", vehicles.stream().filter(v -> v instanceof Truck && ((Truck) v).getType().equals("REEFER")).count());
        vehicleCounts.put("TANKER", vehicles.stream().filter(v -> v instanceof Truck && ((Truck) v).getType().equals("TANKER")).count());

        uiUtils.printTopBorderWithTableName("Vehicle type", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Type", "Count");
        uiUtils.printHorizontalLine(30, 20);

        for (Map.Entry<String, Long> entry : vehicleCounts.entrySet()) {
            if (entry.getValue() > 0) {
                System.out.printf("| %-30s | %-20d |\n", entry.getKey(), entry.getValue());
            }
        }

        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }


    public void vehiclePerPort() {
        int total = vehicleController.fetchVehiclesFromDatabase().size();
        List<Vehicle> vehicles = vehicleController.fetchVehiclesFromDatabase();

        uiUtils.printTopBorderWithTableName("Vehicle per port", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Port", "Count");
        uiUtils.printHorizontalLine(30, 20);
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getCurrentPort() != null) {
                long count = vehicles.stream()
                        .filter(v -> v.getCurrentPort() != null)
                        .count();
                System.out.printf("| %-30s | %-20d |\n", vehicle.getCurrentPort().getName(), count);
                break;
            }
        }
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }
}
