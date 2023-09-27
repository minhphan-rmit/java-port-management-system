package models.vehicle;

import models.container.Container;
import models.port.Port;
import utils.DistanceCalculator;

public class Ship extends Vehicle {

    protected double shipAvgSpeed = 41.65; // 25 knots in km/h

    public Ship(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity) {
        super(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity);
    }

    public Ship(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity, Port currentPort) {
        super(vehicleId, name, currentFuel, carryingCapacity, fuelCapacity, currentPort);
    }

    @Override
    public boolean canCarry(Container container) {
        return true;
    }

    @Override
    public double calculateFuelNeeded(Port destinationPort) {
        if (this.currentPort == null) {
            throw new IllegalStateException("The ship is not currently at a port!");
        }
        Port currentPort = this.currentPort;
        double distance = DistanceCalculator.calculateDistance(
                currentPort.getLatitude(),
                currentPort.getLongitude(),
                destinationPort.getLatitude(),
                destinationPort.getLongitude()
        );

        double fuelNeeded = 0;

        for (Container container : this.containers) {
            fuelNeeded += distance / container.getFuelConsumptionPerKmForShip();
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
        if (this.currentPort == null) {
            throw new IllegalStateException("The ship is not currently at a port!");
        }
        Port currentPort = this.currentPort;
        double distance = DistanceCalculator.calculateDistance(
                currentPort.getLatitude(),
                currentPort.getLongitude(),
                destinationPort.getLatitude(),
                destinationPort.getLongitude()
        );
        return distance / this.shipAvgSpeed;
    }
}
