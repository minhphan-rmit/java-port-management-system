package models.container;

import models.port.Port;
import models.vehicle.Vehicle;

public class OpenTop extends Container {
    public OpenTop(String containerId, double weight) {
        super(containerId, weight);
    }

    public OpenTop(String containerId, double weight, Port currentPort) {
        super(containerId, weight, currentPort);
    }

    public OpenTop(String containerId, double weight, Vehicle currentVehicle) {
        super(containerId, weight, currentVehicle);
    }

    @Override
    public String getContainerType() {
        return "Open Top";
    }

    @Override
    public double getFuelConsumptionPerKmForShip() {
        return 2.8;
    }

    @Override
    public double getFuelConsumptionPerKmForTruck() {
        return 3.2;
    }
}
