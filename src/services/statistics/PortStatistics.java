package services.statistics;

import interfaces.statistics.PortStatInterface;
import models.port.Port;
import models.trip.Trip;
import services.admin.PortServicesAdmin;
import services.admin.TripServicesAdmin;
import utils.UiUtils;

import java.util.List;
import java.util.stream.Collectors;

public class PortStatistics implements PortStatInterface {

    private final PortServicesAdmin portController = new PortServicesAdmin();
    private final TripServicesAdmin tripController = new TripServicesAdmin();
    private final UiUtils uiUtils = new UiUtils();

    public void displayTotalNumberOfPorts() {
        int total = portController.fetchPortsFromDatabase().size();

        uiUtils.printTopBorderWithTableName("Total number of ports in the system", 56);
        System.out.printf("| %-56s |\n", total + " ports");
        uiUtils.printHorizontalLine(56);
    }

    public void portUsedCapacity() {
        int total = portController.fetchPortsFromDatabase().size();
        int used = portController.fetchPortsFromDatabase().stream()
                .filter(port -> port.getOccupiedCapacity() > 0)
                .toList()
                .size();

        uiUtils.printTopBorderWithTableName("Port used capacity", 56);
        System.out.printf("| %-56s |\n", used + "/" + total + " ports");
        uiUtils.printHorizontalLine(56);
    }

    public void portTripCount() {
        List<Trip> allTrips = tripController.fetchTripsFromDatabase();
        List<Port> allPorts = portController.fetchPortsFromDatabase();

        uiUtils.printTopBorderWithTableName("Port trip count", 20, 15, 15);
        System.out.printf("| %-20s | %-15s | %-15s |\n", "Port Name", "Departures", "Arrivals");
        uiUtils.printHorizontalLine(20, 15, 15);

        for (Port port : allPorts) {
            long departureCount = allTrips.stream()
                    .filter(trip -> trip.getDeparturePort().getPortId().equals(port.getPortId()))
                    .count();

            long arrivalCount = allTrips.stream()
                    .filter(trip -> trip.getArrivalPort().getPortId().equals(port.getPortId()))
                    .count();

            System.out.printf("| %-20s | %-15d | %-15d |\n", port.getName(), departureCount, arrivalCount);
        }

        uiUtils.printHorizontalLine(20, 15, 15);
    }

}
