package services.statistics;

import models.port.Port;
import models.trip.Trip;
import models.vehicle.Ship;
import services.admin.ContainerServicesAdmin;
import services.admin.TripServicesAdmin;
import models.container.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    private final TripServicesAdmin tripController = new TripServicesAdmin();
    private final ContainerServicesAdmin containerController = new ContainerServicesAdmin();

    public double fuelUsedInDay(Date date) {
        double fuelUsed = 0;
        for (Trip trip : tripController.fetchTripsFromDatabase()) {
            if (trip.getDepartureDate().equals(date)) {
                fuelUsed += trip.getFuelUsed();
            }
        }
        return fuelUsed;
    }

    public double weightOfContainerByType(String type) {
        double weight = 0;
        List<Container> containerList = containerController.fetchContainersFromDatabase();
        for (Container container : containerList) {
            if (container.getContainerType().equals(type)) {
                weight += container.getWeight();
            }
        }
        return weight;
    }

    public List<Ship> allShipsInPort(Port port) {
        return port.getCurrentVehicles().stream()
                .filter(vehicle -> vehicle instanceof Ship)
                .map(vehicle -> (Ship) vehicle)
                .collect(Collectors.toList());
    }

    public List<Trip> allTripsInDuration(Date startDate, Date endDate) {
        return tripController.fetchTripsFromDatabase().stream()
                .filter(trip -> trip.getDepartureDate().after(startDate) && trip.getDepartureDate().before(endDate))
                .collect(Collectors.toList());
    }

}
