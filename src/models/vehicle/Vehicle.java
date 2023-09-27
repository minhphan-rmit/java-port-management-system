package models.vehicle;

import models.container.Container;
import models.port.Port;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle implements Serializable {
    protected String vehicleId;
    protected String name;
    protected Port currentPort;
    protected double currentFuel;
    protected double carryingCapacity;
    protected double fuelCapacity;
    protected List<Container> containers;

    // Constructor
    public Vehicle(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.currentPort = null;
        this.currentFuel = currentFuel;
        this.carryingCapacity = carryingCapacity;
        this.fuelCapacity = fuelCapacity;

        this.containers = new ArrayList<>();
    }

    public Vehicle(String vehicleId, String name, double currentFuel, double carryingCapacity, double fuelCapacity, Port currentPort) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.currentPort = currentPort;
        this.currentFuel = currentFuel;
        this.carryingCapacity = carryingCapacity;
        this.fuelCapacity = fuelCapacity;

        this.containers = new ArrayList<>();
    }

    // Methods
    public boolean loadContainer(Container container) {
        this.containers.add(container);
        return false;
    }

    public void unloadContainer(Container container) {
        this.containers.remove(container);
    }

    public void refuel() {
        this.currentFuel = this.fuelCapacity;
    }

    // Abstract methods
    public abstract boolean canCarry(Container container);
    public abstract boolean canMoveToPort(Port destinationPort);
    public abstract double calculateFuelNeeded(Port destinationPort);
    public abstract double calculateTimeNeeded(Port destinationPort);

    // Setters and getters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleID) {
        this.vehicleId = vehicleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        this.currentFuel = currentFuel;
    }

    public double getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(double carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    public double getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(double fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public Port getCurrentPort() {
        return currentPort;
    }

    public void setCurrentPort(Port currentPort) {
        this.currentPort = currentPort;
    }

    public String getVehicleStatus() {
        if (this.currentPort == null) {
            return "In transit";
        } else {
            return "In port";
        }
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void emptyContainers() {
        this.containers = new ArrayList<>();
    }
}



