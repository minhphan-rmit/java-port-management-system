package models.container;

import models.port.Port;
import models.vehicle.Vehicle;

public class OpenSide extends Container {
    public OpenSide(String containerId, double weight) {
        super(containerId, weight);
    }

    public OpenSide(String containerId, double weight, Port currentPort) {
        super(containerId, weight, currentPort);
    }

    public OpenSide(String containerId, double weight, Vehicle currentVehicle) {
        super(containerId, weight, currentVehicle);
    }

    @Override
    public String getContainerType() {
        return "Open Side";
    }

    @Override
    public double getFuelConsumptionPerKmForShip() {
        return 2.7;
    }

    @Override
    public double getFuelConsumptionPerKmForTruck() {
        return 3.2;
    }
}
