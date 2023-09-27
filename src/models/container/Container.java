package models.container;

import models.vehicle.Vehicle;
import models.port.Port;

import java.io.Serializable;

/**
 * Represents an abstract Container that can be associated with a Vehicle or a Port.
 */
public abstract class Container implements Serializable {
    private String containerId;
    private double weight;
    private Object location; // Can be either a Vehicle or a Port.

    /**
     * Initializes a new Container with the specified ID and weight.
     * @param containerId The unique identifier for this container.
     * @param weight The weight of this container.
     */
    public Container(String containerId, double weight) {
        this.containerId = containerId;
        this.weight = weight;
    }

    /**
     * Initializes a new Container with the specified ID, weight, and associated Vehicle.
     * @param containerId The unique identifier for this container.
     * @param weight The weight of this container.
     * @param currentVehicle The vehicle carrying this container.
     */
    public Container(String containerId, double weight, Vehicle currentVehicle) {
        this(containerId, weight);
        this.location = currentVehicle;
    }

    /**
     * Initializes a new Container with the specified ID, weight, and associated Port.
     * @param containerId The unique identifier for this container.
     * @param weight The weight of this container.
     * @param currentPort The port where this container is located.
     */
    public Container(String containerId, double weight, Port currentPort) {
        this(containerId, weight);
        this.location = currentPort;
    }

    // Getters and setters
    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Vehicle getCurrentVehicle() {
        if (location instanceof Vehicle) {
            return (Vehicle) location;
        }
        return null;
    }

    public void setCurrentVehicle(Vehicle currentVehicle) {
        this.location = currentVehicle;
    }

    public Port getCurrentPort() {
        if (location instanceof Port) {
            return (Port) location;
        }
        return null;
    }

    public Object getLocation() {
        if (location instanceof Port) {
            return ((Port) location).getName();
        } else if (location instanceof Vehicle) {
            return ((Vehicle) location).getName();
        }
        return null;
    }

    public String getContainerStatus() {
        if (location instanceof Port) {
            return "Awaiting loading";
        } else if (location instanceof Vehicle) {
            if (((Vehicle) location).getCurrentPort() != null) {
                return "Loaded";
            } else {
                return "In transit";
            }
        } else {
            return "Unoccupied";
        }
    }

    public void setCurrentPort(Port currentPort) {
        this.location = currentPort;
    }

    // Abstract methods to be implemented by each container type.
    public abstract String getContainerType();
    public abstract double getFuelConsumptionPerKmForShip();
    public abstract double getFuelConsumptionPerKmForTruck();
}
