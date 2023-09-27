/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2023B
  Assessment: Group Assignment
  Group: Team Hi
  Members:
  Phan Nhat Minh - s3978598
  Huynh Duc Gia Tin - s3962053
  Nguyen Viet Ha - s3978128
  Vu Minh Ha - s3978681
  Created  date: 02/09/2023
  Acknowledgement: chat.openai.com, stackoverflow.com, geeksforgeeks.org, javatpoint.com, tutorialspoint.com, oracle.com, w3schools.com, github.com
*/

package services.admin;

import exceptions.InputValidation;
import exceptions.TypeCheck;
import interfaces.CRUD.TripCRUD;
import models.trip.Trip;
import models.vehicle.Vehicle;
import utils.Constants;
import database.DatabaseHandler;
import models.port.Port;
import utils.UiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TripServicesAdmin extends AdminBaseServices implements TripCRUD {

    private final Scanner scanner = new Scanner(System.in);
    private final String TRIP_FILE_PATH = Constants.TRIP_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private final UiUtils uiUtils = new UiUtils();
    private final InputValidation inputValidation = new InputValidation();

    // Modularized method to fetch trips from the database
    public List<Trip> fetchTripsFromDatabase() {
        try {
            Trip[] tripsArray = (Trip[]) dbHandler.readObjects(TRIP_FILE_PATH);
            return new ArrayList<>(Arrays.asList(tripsArray));
        } catch (Exception e) {  // Catching a generic exception as a placeholder.
            uiUtils.printFailedMessage("Error reading trips or no trips exist.");
            return new ArrayList<>();
        }
    }

    @Override
    public void createTrip() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("TRIP CREATION WIZARD", 100);
        System.out.println();

        String tripId = inputValidation.idValidation("T", "Enter trip ID to create: ");
        System.out.println();

        VehicleServicesAdmin vehicleController = new VehicleServicesAdmin();
        vehicleController.displayAllVehicles();

        String vehicleId = inputValidation.idValidation("V", "Enter vehicle ID for the trip: ");
        Vehicle vehicle = vehicleController.getVehicleById(vehicleId);
        if (vehicle == null) {
            uiUtils.printFailedMessage("Invalid vehicle ID. Aborting trip creation.");
            return;
        }

        // Using PortServicesAdmin to display available Ports for departure and arrival
        PortServicesAdmin portController = new PortServicesAdmin();

        portController.displayAllPorts(); // Display all ports
        String departurePortId = inputValidation.idValidation("P", "Enter departure port ID: ");
        Port departurePort = portController.getPortById(departurePortId);
        if (departurePort == null) {
            uiUtils.printFailedMessage("Invalid departure port ID. Aborting trip creation.");
            return;
        }

        String arrivalPortId = inputValidation.idValidation("P", "Enter arrival port ID: ");
        Port arrivalPort = portController.getPortById(arrivalPortId);
        if (arrivalPort == null) {
            uiUtils.printFailedMessage("Invalid arrival port ID. Aborting trip creation.");
            return;
        }

        // Check if departure and arrival ports are the same
        if (departurePortId.equals(arrivalPortId)) {
            uiUtils.printFailedMessage("Departure and arrival ports cannot be the same. Aborting trip creation.");
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("Enter departure date (dd-MM-yyyy): ");
        Date departureDate;
        try {
            departureDate = sdf.parse(scanner.nextLine());
        } catch (Exception e) {
            uiUtils.printFailedMessage("Error parsing date. Please use the correct format.");
            return;
        }

        Trip newTrip = new Trip(tripId, vehicle, departurePort, arrivalPort, departureDate);

        List<Trip> tripsList = fetchTripsFromDatabase();
        tripsList.add(newTrip);
        dbHandler.writeObjects(TRIP_FILE_PATH, tripsList.toArray(new Trip[0]));
        uiUtils.printSuccessMessage("Trip created successfully!");
    }

    @Override
    public void findTrip() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("TRIP SEARCH WIZARD", 100);
        System.out.println();

        String tripIdToDisplay = inputValidation.idValidation("T", "Enter trip ID to search: ");

        List<Trip> tripsList = fetchTripsFromDatabase();

        Trip tripToDisplay = null;
        for (Trip trip : tripsList) {
            if (trip.getTripId().equals(tripIdToDisplay)) {
                tripToDisplay = trip;
                break;
            }
        }

        if (tripToDisplay != null) {
            uiUtils.printTopBorderWithTableName("TRIP INFO", 10, 20, 20, 20, 20, 20, 20);
            System.out.printf("| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                    "Trip ID", "Vehicle", "Departure Port", "Arrival Port", "Departure Date", "Arrival Date", "Status");
            uiUtils.printHorizontalLine(10, 20, 20, 20, 20, 20, 20);
            System.out.printf("| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                    tripToDisplay.getTripId(), tripToDisplay.getVehicle().toString(), tripToDisplay.getDeparturePort().getName(),
                    tripToDisplay.getArrivalPort().getName(), sdf.format(tripToDisplay.getDepartureDate()),
                    tripToDisplay.getArrivalDate() != null ? sdf.format(tripToDisplay.getArrivalDate()) : "N/A", tripToDisplay.getStatus());
            uiUtils.printHorizontalLine(10, 20, 20, 20, 20, 20, 20);
        } else {
            uiUtils.printFailedMessage("No trip found with the given ID.");
        }
    }

    @Override
    public void displayAllTrips() {
        uiUtils.clearScreen();

        List<Trip> tripsList = fetchTripsFromDatabase();

        uiUtils.printTopBorderWithTableName("TRIPS INFORMATION", 10, 20, 20, 20, 20, 20, 20);
        System.out.printf("| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                "Trip ID", "Vehicle", "Departure Port", "Arrival Port", "Departure Date", "Arrival Date", "Status");
        uiUtils.printHorizontalLine(10, 20, 20, 20, 20, 20, 20);
        for (Trip trip : tripsList) {
            System.out.printf("| %-10s | %-20s | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                    trip.getTripId(), trip.getVehicle().getName(), trip.getDeparturePort().getName(),
                    trip.getArrivalPort().getName(), sdf.format(trip.getDepartureDate()),
                    trip.getArrivalDate() != null ? sdf.format(trip.getArrivalDate()) : "N/A", trip.getStatus());
        }
        uiUtils.printHorizontalLine(10, 20, 20, 20, 20, 20, 20);
    }

    @Override
    public void updateTrip() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("TRIP UPDATE WIZARD", 100);
        System.out.println();

        String tripIdToUpdate = inputValidation.idValidation("T", "Enter trip ID to update:");
        System.out.println();

        List<Trip> tripsList = fetchTripsFromDatabase();

        Trip tripToUpdate = null;
        for (Trip trip : tripsList) {
            if (trip.getTripId().equals(tripIdToUpdate)) {
                tripToUpdate = trip;
                break;
            }
        }

        if (tripToUpdate != null) {
            System.out.print("Enter new arrival date (dd-MM-yyyy, leave blank to keep unchanged): ");
            String arrivalDateStr = scanner.nextLine();
            if (!arrivalDateStr.isEmpty()) {
                Date arrivalDate;
                try {
                    arrivalDate = sdf.parse(arrivalDateStr);
                    tripToUpdate.completeTrip(arrivalDate);
                } catch (ParseException e) {
                    uiUtils.printFailedMessage("Error parsing date. Please use the correct format.");
                }
            }

            System.out.print("Enter new status (e.g., \"In Transit\", \"Completed\", leave blank to keep unchanged): ");
            String newStatus = scanner.nextLine();
            if (!newStatus.isEmpty()) {
                tripToUpdate.setStatus(newStatus);
            }

            dbHandler.writeObjects(TRIP_FILE_PATH, tripsList.toArray(new Trip[0]));
            uiUtils.printSuccessMessage("Trip updated successfully!");
        } else {
            uiUtils.printFailedMessage("No trip found with the given ID.");
        }
    }

    @Override
    public void deleteTrip() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("TRIP DELETION WIZARD", 100);
        System.out.println();

        String tripIdToDelete = inputValidation.idValidation("T", "Enter trip ID to delete: ");
        System.out.println();

        List<Trip> tripsList = fetchTripsFromDatabase();

        Trip tripToDelete = null;
        for (Trip trip : tripsList) {
            if (trip.getTripId().equals(tripIdToDelete)) {
                tripToDelete = trip;
                break;
            }
        }

        if (tripToDelete != null) {
            tripsList.remove(tripToDelete);
            dbHandler.writeObjects(TRIP_FILE_PATH, tripsList.toArray(new Trip[0]));
            uiUtils.printSuccessMessage("Trip deleted successfully!");
        } else {
            uiUtils.printFailedMessage("No trip found with the given ID.");
        }
    }

    public boolean vehicleAvailCheck(Vehicle vehicle) {
        List<Trip> tripsList = fetchTripsFromDatabase();

        for (Trip trip : tripsList) {
            if (trip.getVehicle().equals(vehicle)) {
                if (trip.getStatus().equals("COMPLETED")) {
                    continue;
                }
                if (trip.getStatus().equals("IN_TRANSIT")) {
                    return false;
                }
                if (trip.getStatus().equals("LOADING")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void historyClear() {
        List<Trip> tripsList = fetchTripsFromDatabase();
        List<Trip> filteredTrips = new ArrayList<>();

        Date currentDate = Constants.SYSTEM_DATE; // Gets the current date

        for (Trip trip : tripsList) {
            if ((currentDate.getTime() - trip.getDepartureDate().getTime()) <= (7 * 24 * 60 * 60 * 1000)) { // Checks if the trip's date is within the last 7 days.
                filteredTrips.add(trip);
            }
        }

        dbHandler.writeObjects(TRIP_FILE_PATH, filteredTrips.toArray(new Trip[0])); // Writes the filtered trips back to the database.
    }

    public void updateTripStatusBasedOnSystemDate() {
        List<Trip> tripsList = fetchTripsFromDatabase();

        Date systemDate;
        systemDate = Constants.SYSTEM_DATE;

        for (Trip trip : tripsList) {
            if (trip.getDepartureDate().equals(systemDate)) {
                trip.setStatus(String.valueOf(Trip.Status.IN_TRANSIT)); // Assuming the Trip class has an enum called Status with a value ONGOING.
            }
        }

        dbHandler.writeObjects(TRIP_FILE_PATH, tripsList.toArray(new Trip[0]));
    }

}
