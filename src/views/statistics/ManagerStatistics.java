package views.statistics;

import models.port.Port;
import models.trip.Trip;
import models.user.PortManager;
import models.vehicle.Ship;
import services.statistics.Statistics;
import utils.CurrentUser;
import utils.UiUtils;
import exceptions.InputValidation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ManagerStatistics {

    private final Statistics statistics = new Statistics();
    private final InputValidation inputValidation = new InputValidation();
    private final UiUtils uiUtils = new UiUtils();
    private final Port managedPort;

    public ManagerStatistics() {
        if (CurrentUser.getUser() instanceof PortManager) {
            this.managedPort = ((PortManager) CurrentUser.getUser()).getManagedPort();
        } else {
            throw new IllegalStateException("The current user is not a Port Manager");
        }
    }

    public void displayFuelUsedInDay() {
        uiUtils.clearScreen();
        uiUtils.printTopBorderWithTableName("Fuel used in day", 56);
        Date chosenDate = inputValidation.getDate("Enter date (dd-MM-yyyy): ");

        double fuelUsed = statistics.fuelUsedInDay(chosenDate);
        System.out.printf("| %-56s |\n", fuelUsed + " litres");
        uiUtils.printHorizontalLine(56);
    }

    public void displayWeightOfContainerByType() {
        uiUtils.clearScreen();
        String[] containerTypes = {"Basic", "OpenSide", "OpenTop", "Liquid", "Refrigerated"};

        uiUtils.printTopBorderWithTableName("Weight of container by type", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Container Type", "Total Weight");
        uiUtils.printHorizontalLine(30, 20);

        for (String type : containerTypes) {
            double weight = statistics.weightOfContainerByType(type);
            System.out.printf("| %-30s | %-20.2f |\n", type, weight);
        }

        uiUtils.printHorizontalLine(30, 20);
    }

    public void displayAllShipsInPort() {
        List<Ship> shipsInPort = statistics.allShipsInPort(managedPort);

        System.out.println("List of ships in port " + managedPort.getName() + ":");
        System.out.println("--------------------------------------------------");
        System.out.printf("| %-20s | %-20s |\n", "Ship ID", "Ship Name");
        System.out.println("--------------------------------------------------");

        for (Ship ship : shipsInPort) {
            System.out.printf("| %-20s | %-20s |\n", ship.getVehicleId(), ship.getName());
        }

        System.out.println("--------------------------------------------------");
    }

    public void displayAllTripsInDuration() {
        System.out.println("Please provide the start and end dates for the duration you want to inspect.");

        Date startDate = inputValidation.getDate("Enter the start date (dd-MM-yyyy): ");
        Date endDate = inputValidation.getDate("Enter the end date (dd-MM-yyyy): ");

        List<Trip> trips = statistics.allTripsInDuration(startDate, endDate);

        uiUtils.clearScreen();
        uiUtils.printTopBorderWithTableName("Trips within the date range", 15, 20, 20); // Adjust columns as necessary
        System.out.printf("| %-15s | %-20s | %-20s |\n", "Trip ID", "Departure Date", "Arrival Date");
        uiUtils.printHorizontalLine(15, 20, 20); // Adjust columns as necessary

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        if (trips.isEmpty()) {
            System.out.println("| No trips within the provided date range.                     |"); // Adjust spacing as necessary
        } else {
            for (Trip trip : trips) {
                String departureDateString = sdf.format(trip.getDepartureDate());
                String arrivalDateString = sdf.format(trip.getArrivalDate()); // Assuming there's a getArrivalDate in the Trip class
                System.out.printf("| %-15s | %-20s | %-20s |\n", trip.getTripId(), departureDateString, arrivalDateString); // Assuming there's a getTripId in the Trip class
            }
        }

        uiUtils.printHorizontalLine(15, 20, 20); // Adjust columns as necessary
    }


}
