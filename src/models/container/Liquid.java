package models.container;

import models.port.Port;
import models.vehicle.Vehicle;

public class Liquid extends Container {
    public Liquid(String containerId, double weight) {
        super(containerId, weight);
    }

    public Liquid(String containerId, double weight, Port currentPort) {
        super(containerId, weight, currentPort);
    }

    public Liquid(String containerId, double weight, Vehicle currentVehicle) {
        super(containerId, weight, currentVehicle);
    }

    @Override
    public String getContainerType() {
        return "Liquid";
    }

    @Override
    public double getFuelConsumptionPerKmForShip() {
        return 4.8;
    }

    @Override
    public double getFuelConsumptionPerKmForTruck() {
        return 5.3;
    }
}
