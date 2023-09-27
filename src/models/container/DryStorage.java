package models.container;

import models.port.Port;
import models.vehicle.Vehicle;

public class DryStorage extends Container {
    public DryStorage(String containerId, double weight) {
        super(containerId, weight);
    }

    public DryStorage(String containerId, double weight, Port currentPort) {
        super(containerId, weight, currentPort);
    }

    public DryStorage(String containerId, double weight, Vehicle currentVehicle) {
        super(containerId, weight, currentVehicle);
    }

    @Override
    public String getContainerType() {
        return "Dry Storage";
    }

    @Override
    public double getFuelConsumptionPerKmForShip() {
        return 3.5;
    }

    @Override
    public double getFuelConsumptionPerKmForTruck() {
        return 4.6;
    }
}
