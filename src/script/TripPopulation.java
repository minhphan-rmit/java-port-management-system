package script;

import models.port.Port;
import models.vehicle.Truck;
import models.vehicle.Vehicle;
import models.trip.Trip;
import models.trip.Trip.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import database.DatabaseHandler;
import utils.Constants;

import static models.vehicle.Truck.TruckType.TANKER;

public class TripPopulation {

    private static final String TRIP_FILE_PATH = Constants.TRIP_FILE_PATH;
    // vehicle file path
    private static final String VEHICLE_FILE_PATH = Constants.VEHICLE_FILE_PATH;
    // get ports
    private static final String PORT_FILE_PATH = Constants.PORT_FILE_PATH;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    public void run() {
        List<Trip> tripList;
        try {
            Trip[] tripArray = (Trip[]) dbHandler.readObjects(TRIP_FILE_PATH);
            tripList = new ArrayList<>(Arrays.asList(tripArray));
        } catch (Exception e) {
            tripList = new ArrayList<>();
        }

        // Sample data for trips
        String[] tripIds = {
                "T-000001", "T-000002", "T-000003", "T-000004", "T-000005",
                "T-000006", "T-000007", "T-000008", "T-000009", "T-000010",
                "T-000011", "T-000012", "T-000013", "T-000014", "T-000015",
                "T-000016", "T-000017", "T-000018", "T-000019", "T-000020",
                "T-000021", "T-000022", "T-000023", "T-000024", "T-000025",
        };

        // get vehicles
        Vehicle[] vehicles = (Vehicle[]) dbHandler.readObjects(VEHICLE_FILE_PATH);

        // get ports
        Port[] ports = (Port[]) dbHandler.readObjects(PORT_FILE_PATH);

        Date[] departureDates = {
                new Date(), new Date(), new Date(), new Date(), new Date(),
                new Date(), new Date(), new Date(), new Date(), new Date(),
                new Date(), new Date(), new Date(), new Date(), new Date(),
                new Date(), new Date(), new Date(), new Date(), new Date(),
                new Date(), new Date(), new Date(), new Date(), new Date(),
        };

        // Creating and adding trips to the list
        for (int i = 0; i < tripIds.length; i++) {
            if (i < vehicles.length) {
                tripList.add(new Trip(tripIds[i], vehicles[i], ports[0], ports[1], departureDates[i], departureDates[i], Status.COMPLETED));
            }
        }

        dbHandler.writeObjects(TRIP_FILE_PATH, tripList.toArray(new Trip[0]));
    }

    public void emptyDatabase() {
        try {
            dbHandler.writeObjects(TRIP_FILE_PATH, new Trip[0]);
            // If you have other database files, you can add similar lines here to empty them too.
        } catch (Exception e) {
            System.out.println("Error emptying the database: " + e.getMessage());
        }
    }

    /**
     * The main method to execute vehicle population.
     *
     * @param args Command-line arguments (not used in this application).
     */

    public static void main(String[] args) {
        TripPopulation tripPopulation = new TripPopulation();
        tripPopulation.emptyDatabase();
        tripPopulation.run();
    }
}
