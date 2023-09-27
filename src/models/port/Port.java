package models.port;

import models.container.Container;
import models.vehicle.Vehicle;
import utils.DistanceCalculator;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Port implements Serializable {

    // Attributes
    private final String portId;  // Making it final to ensure immutability
    private String name;
    private double latitude;
    private double longitude;
    private double storingCapacity;
    private boolean landingAbility;
    private final List<Container> currentContainers;  // Lists are mutable, but we'll handle that in getters
    private final List<Vehicle> currentVehicles;

    // Constructors
    public Port(String portId) {
        if (portId == null || portId.trim().isEmpty()) {
            throw new IllegalArgumentException("portId cannot be null or empty");
        }

        this.portId = portId;
        this.currentContainers = new ArrayList<>();
        this.currentVehicles = new ArrayList<>();
    }

    public Port(String portId, String name, double latitude, double longitude, double storingCapacity, boolean landingAbility) {
        this(portId);  // Calling the other constructor
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storingCapacity = storingCapacity;
        this.landingAbility = landingAbility;
    }

    // Getters and setters
    public String getPortId() {
        return portId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getStoringCapacity() {
        return storingCapacity;
    }

    public void setStoringCapacity(double storingCapacity) {
        this.storingCapacity = storingCapacity;
    }

    public boolean getLandingAbility() {
        return landingAbility;
    }

    public void setLandingAbility(boolean landingAbility) {
        this.landingAbility = landingAbility;
    }

    public List<Container> getCurrentContainers() {
        return Collections.unmodifiableList(currentContainers);
    }

    public List<Vehicle> getCurrentVehicles() {
        return Collections.unmodifiableList(currentVehicles);
    }

    // Methods
    public double calculateDistanceToAnotherPort(Port anotherPort) {
        if (anotherPort == null) {
            throw new IllegalArgumentException("anotherPort cannot be null");
        }

        return DistanceCalculator.calculateDistance(
                this.latitude, this.longitude, anotherPort.latitude, anotherPort.longitude
        );
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("vehicle cannot be null");
        }
        this.currentVehicles.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        this.currentVehicles.remove(vehicle);
    }

    public void addContainer(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("container cannot be null");
        }
        this.currentContainers.add(container);
    }

    public void removeContainer(Container container) {
        this.currentContainers.remove(container);
    }

    public double getOccupiedCapacity() {
        double occupiedCapacity = 0;
        for (Container container : this.currentContainers) {
            occupiedCapacity += container.getWeight();
        }
        return occupiedCapacity;
    }
}
