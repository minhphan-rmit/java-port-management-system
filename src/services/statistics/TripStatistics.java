package services.statistics;

import interfaces.statistics.TripStatInterface;
import models.trip.Trip;
import models.vehicle.Ship;
import models.vehicle.Truck;
import services.admin.PortServicesAdmin;
import services.admin.TripServicesAdmin;
import utils.UiUtils;

import java.util.List;

public class TripStatistics implements TripStatInterface {

    private final TripServicesAdmin tripController = new TripServicesAdmin();
    private final UiUtils uiUtils = new UiUtils();

    public void displayTotalTrips() {
        int total = tripController.fetchTripsFromDatabase().size();

        uiUtils.printTopBorderWithTableName("Total number of trips in the system", 53);
        System.out.printf("| %-53s |\n", total + " trips");
        uiUtils.printHorizontalLine(53);
    }

    public void tripStatus() {
        List<Trip> trips = tripController.fetchTripsFromDatabase();
        int loadingCount = 0;
        int inTransitCount = 0;
        int completedCount = 0;

        for (Trip trip : trips) {
            if (trip.getStatus().equals("LOADING")) {
                loadingCount++;
            } else if (trip.getStatus().equals("IN_TRANSIT")) {
                inTransitCount++;
            } else if (trip.getStatus().equals("COMPLETED")) {
                completedCount++;
            }
        }

        uiUtils.printTopBorderWithTableName("Trip status", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Trip Status", "Count");
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "LOADING", loadingCount);
        System.out.printf("| %-30s | %-20d |\n", "IN TRANSIT", inTransitCount);
        System.out.printf("| %-30s | %-20d |\n", "COMPLETED", completedCount);
        uiUtils.printHorizontalLine(30, 20);
    }

    public void tripByVehicle() {
        List<Trip> trips = tripController.fetchTripsFromDatabase();
        int truckCount = 0;
        int shipCount = 0;

        for (Trip trip : trips) {
            if (trip.getVehicle() instanceof Truck) {
                truckCount++;
            } else if (trip.getVehicle() instanceof Ship) {
                shipCount++;
            }
        }

        uiUtils.printTopBorderWithTableName("Trip by vehicle", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Vehicle Type", "Trips");
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Truck", truckCount);
        System.out.printf("| %-30s | %-20d |\n", "Ship", shipCount);
        uiUtils.printHorizontalLine(30, 20);
    }

    public void averageTripDuration() {
        List<Trip> trips = tripController.fetchTripsFromDatabase();
        int total = 0;
        int count = 0;

        for (Trip trip : trips) {
            if (trip.getStatus().equals("COMPLETED")) {
                total += (int) trip.getDuration();
                count++;
            }
        }

        uiUtils.printTopBorderWithTableName("Average trip duration", 53);
        uiUtils.printHorizontalLine(53);

        if (count == 0) {
            System.out.printf("| %-53s |\n", "No completed trips in the system.");
        } else {
            System.out.printf("| %-53s |\n", (total / count) + " hours");
        }

        uiUtils.printHorizontalLine(53);
    }
}
