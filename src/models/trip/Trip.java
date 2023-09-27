package models.trip;

import models.port.Port;
import models.vehicle.Vehicle;

import java.io.Serializable;
import java.util.Date;

public class Trip implements Serializable {
    private final String tripId;            // Unique ID for the trip, made final since it's immutable
    private Vehicle vehicle;                // Vehicle used for the trip
    private Port departurePort;             // Departure port
    private Port arrivalPort;               // Destination port
    private Date departureDate;             // Departure date
    private Date arrivalDate;               // Arrival date
    private Status status;                  // Current status (e.g., "In Transit", "Completed")

    public enum Status {
        LOADING, IN_TRANSIT, COMPLETED
    }

    public Trip(String tripId, Vehicle vehicle, Port departurePort, Port arrivalPort, Date departureDate) {
        if (tripId == null || vehicle == null || departurePort == null || arrivalPort == null || departureDate == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        this.tripId = tripId;
        this.vehicle = vehicle;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.departureDate = new Date(departureDate.getTime());
        this.status = Status.LOADING;
    }

    public Trip(String tripId, Vehicle vehicle, Port departurePort, Port arrivalPort, Date departureDate, Date arrivalDate, Status status) {
        if (tripId == null || vehicle == null || departurePort == null || arrivalPort == null || departureDate == null || arrivalDate == null || status == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        this.tripId = tripId;
        this.vehicle = vehicle;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.departureDate = new Date(departureDate.getTime());
        this.arrivalDate = new Date(arrivalDate.getTime());
        this.status = status;
    }

    public void completeTrip(Date arrivalDate) {
        if (arrivalDate == null) {
            throw new IllegalArgumentException("Arrival date cannot be null");
        }

        this.arrivalDate = new Date(arrivalDate.getTime());
        this.status = Status.COMPLETED;
    }

    // Getters and Setters
    public String getTripId() {
        return tripId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        this.vehicle = vehicle;
    }

    public Port getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(Port departurePort) {
        if (departurePort == null) {
            throw new IllegalArgumentException("Departure port cannot be null");
        }
        this.departurePort = departurePort;
    }

    public Port getArrivalPort() {
        return arrivalPort;
    }

    public void setArrivalPort(Port arrivalPort) {
        if (arrivalPort == null) {
            throw new IllegalArgumentException("Arrival port cannot be null");
        }
        this.arrivalPort = arrivalPort;
    }

    public Date getDepartureDate() {
        return new Date(departureDate.getTime());
    }

    public void setDepartureDate(Date departureDate) {
        if (departureDate == null) {
            throw new IllegalArgumentException("Departure date cannot be null");
        }
        this.departureDate = new Date(departureDate.getTime());
    }

    public Date getArrivalDate() {
        if (arrivalDate == null) {
            return null;
        }
        return new Date(arrivalDate.getTime());
    }

    public String getStatus() {
        return String.valueOf(status);
    }

    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Invalid status");
        } else {
            Status.valueOf(status);
        }
        this.status = Status.valueOf(status);
    }

    public double getFuelUsed() {
        return vehicle.calculateFuelNeeded(arrivalPort);
    }

    public long getDuration() {
        long differenceInMillis = arrivalDate.getTime() - departureDate.getTime();
        return differenceInMillis / (1000 * 60 * 60 * 24);
    }
}
