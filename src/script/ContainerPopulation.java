package script;

import database.DatabaseHandler;
import models.container.*;
import utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * ContainerPopulation is a class responsible for generating and managing containers
 * and storing them in a database.
 */
public class ContainerPopulation {

    private static final String CONTAINER_FILE_PATH = Constants.CONTAINER_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final Random rand = new Random();

    /**
     * Generates a random weight within a specified range.
     *
     * @param min The minimum weight.
     * @param max The maximum weight.
     * @return A random weight.
     */
    double getRandomWeight(double min, double max) {
        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Populates the container list with various types of containers and writes them to the database.
     */
    public void run() {
        List<Container> containersList;
        try {
            // Read existing containers from the database if they exist
            Container[] containersArray = (Container[]) dbHandler.readObjects(CONTAINER_FILE_PATH);
            containersList = new ArrayList<>(Arrays.asList(containersArray));
        } catch (Exception e) {
            // Catching a generic exception as a placeholder.
            System.out.println("Error reading containers or no containers exist.");
            containersList = new ArrayList<>();
        }

        // Populate DryStorage containers
        for (int i = 1; i <= 40; i++) {
            String containerID = String.format("C-%06d", i);
            double weight = getRandomWeight(2300, 30480);
            containersList.add(new DryStorage(containerID, weight));
        }

        // Populate OpenSide containers
        for (int i = 41; i <= 80; i++) {
            String containerID = String.format("C-%06d", i);
            double weight = getRandomWeight(2450, 30480);
            containersList.add(new OpenSide(containerID, weight));
        }

        // Populate OpenTop containers
        for (int i = 81; i <= 120; i++) {
            String containerID = String.format("C-%06d", i);
            double weight = getRandomWeight(2350, 30480);
            containersList.add(new OpenTop(containerID, weight));
        }

        // Populate Liquid containers
        for (int i = 121; i <= 160; i++) {
            String containerID = String.format("C-%06d", i);
            double weight = getRandomWeight(3800, 36000);
            containersList.add(new Liquid(containerID, weight));
        }

        // Populate Refrigerated containers
        for (int i = 161; i <= 200; i++) {
            String containerID = String.format("C-%06d", i);
            double weight = getRandomWeight(3000, 30480);
            containersList.add(new Refrigerated(containerID, weight));
        }

        // Write the updated containers list back to the database
        try {
            dbHandler.writeObjects(CONTAINER_FILE_PATH, containersList.toArray(new Container[0]));
        } catch (Exception e) {
            System.out.println("Error writing containers to the database: " + e.getMessage());
        }
    }

    /**
     * Empties the container database by writing an empty array of containers.
     */
    public void emptyDatabase() {
        try {
            dbHandler.writeObjects(CONTAINER_FILE_PATH, new Container[0]);
        } catch (Exception e) {
            System.out.println("Error emptying the database: " + e.getMessage());
        }
    }

    /**
     * The main method to execute container population.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        ContainerPopulation containerPopulation = new ContainerPopulation();
        containerPopulation.emptyDatabase();
        containerPopulation.run();
    }
}
