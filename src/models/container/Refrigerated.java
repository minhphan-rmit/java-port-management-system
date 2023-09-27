package models.container;

import models.port.Port;
import models.vehicle.Vehicle;

public class Refrigerated extends Container {
    public Refrigerated(String containerId, double weight) {
        super(containerId, weight);
    }

    public Refrigerated(String containerId, double weight, Port currentPort) {
        super(containerId, weight, currentPort);
    }

    public Refrigerated(String containerId, double weight, Vehicle currentVehicle) {
        super(containerId, weight, currentVehicle);
    }

    @Override
    public String getContainerType() {
        return "Refrigerated";
    }

    @Override
    public double getFuelConsumptionPerKmForShip() {
        return 4.5;
    }

    @Override
    public double getFuelConsumptionPerKmForTruck() {
        return 5.4;
    }
}
