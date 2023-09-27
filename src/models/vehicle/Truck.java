package models.vehicle;

import models.container.*;
import models.port.Port;
import utils.DistanceCalculator;

import java.util.ArrayList;

public class Truck extends Vehicle {

    protected double truckAvgSpeed = 88.5; // 55 miles per hour in km/h

    public enum TruckType {
        BASIC, REEFER, TANKER
    }

    private final TruckType type;

    public Truck(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity, TruckType type) {
        super(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity);
        this.type = type;
    }

    public Truck(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity, TruckType type, Port currentPort) {
        super(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, currentPort);
        this.type = type;
    }

    @Override
    public boolean canCarry(Container container) {
        if (container == null) {
            throw new IllegalArgumentException("Container argument cannot be null.");
        }

        if (type == null) {
            throw new IllegalStateException("Vehicle type cannot be null.");
        }

        return switch (type) {
            case BASIC -> container instanceof DryStorage ||
                    container instanceof OpenTop ||
                    container instanceof OpenSide;
            case REEFER -> container instanceof Refrigerated;
            case TANKER -> container instanceof Liquid;
            default -> throw new UnsupportedOperationException("Unexpected vehicle type: " + type);
        };
    }

    @Override
    public double calculateFuelNeeded(Port destinationPort) {
        Port currentPort = this.currentPort;
        double distance = DistanceCalculator.calculateDistance(
                currentPort.getLatitude(),
                currentPort.getLongitude(),
                destinationPort.getLatitude(),
                destinationPort.getLongitude()
        );

        double fuelNeeded = 0;

        for (Container container : this.containers) {
            fuelNeeded += distance / container.getFuelConsumptionPerKmForTruck();
        }
        return fuelNeeded;
    }

    @Override
    public boolean canMoveToPort(Port destinationPort) {
        double fuelNeeded = calculateFuelNeeded(destinationPort);
        return !(fuelNeeded > this.fuelCapacity);
    }

    @Override
    public double calculateTimeNeeded(Port destinationPort) {
        Port currentPort = this.currentPort;
        double distance = DistanceCalculator.calculateDistance(
                currentPort.getLatitude(),
                currentPort.getLongitude(),
                destinationPort.getLatitude(),
                destinationPort.getLongitude()
        );
        return distance / this.truckAvgSpeed;
    }

    public String getType() {
        return String.valueOf(type);
    }
}