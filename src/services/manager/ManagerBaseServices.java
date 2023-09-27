package services.manager;

import interfaces.manager.ManagerInterface;
import models.container.Container;
import models.port.Port;
import models.user.PortManager;
import models.vehicle.Vehicle;
import services.admin.PortServicesAdmin;
import utils.CurrentUser;

import java.util.List;

public abstract class ManagerBaseServices implements ManagerInterface {

    private final PortServicesAdmin portServices = new PortServicesAdmin();
    private final Port managedPort;

    public ManagerBaseServices() {
        if (CurrentUser.getUser() instanceof PortManager) {
            this.managedPort = ((PortManager) CurrentUser.getUser()).getManagedPort();
        } else {
            throw new IllegalStateException("The current user is not a Port Manager");
        }
    }

    Container findContainerById(String id) {
        for (Container container : managedPort.getCurrentContainers()) {
            if (container.getContainerId().equals(id)) {
                return container;
            }
        }
        return null;
    }

    Port findPortById(String id) {
        return portServices.getPortById(id);
    }

    Vehicle findVehicleById(String id) {
        // Check if managedPort is null
        if (managedPort == null) {
            System.out.println("managedPort is null!");
            return null;
        }

        // Fetch the current vehicles and check if it returns null
        List<Vehicle> vehicles = managedPort.getCurrentVehicles();
        if (vehicles == null) {
            System.out.println("getCurrentVehicles() returned null!");
            return null;
        }

        // Iterate through the vehicles and find the one with the matching ID
        for (Vehicle vehicle : vehicles) {
            // Check if any vehicle's ID is null
            if (vehicle.getVehicleId() == null) {
                System.out.println("A vehicle has a null ID!");
            } else if (vehicle.getVehicleId().equals(id)) {
                return vehicle;
            }
        }

        // If no vehicle with the provided ID is found, return null
        return null;
    }
}
