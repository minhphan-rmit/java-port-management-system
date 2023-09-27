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
import utils.Constants;
import database.DatabaseHandler;
import models.container.*;
import interfaces.CRUD.ContainerCRUD;
import utils.UiUtils;

import java.util.*;

public class ContainerServicesAdmin extends AdminBaseServices implements ContainerCRUD  {

    private final Scanner scanner = new Scanner(System.in);
    private final String CONTAINER_FILE_PATH = Constants.CONTAINER_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final InputValidation inputValidation = new InputValidation();
    private final UiUtils uiUtils = new UiUtils();
    private final TypeCheck typeCheck = new TypeCheck();

    // Modularized method to fetch containers from the database
    public List<Container> fetchContainersFromDatabase() {
        try {
            Container[] containersArray = (Container[]) dbHandler.readObjects(CONTAINER_FILE_PATH);
            return new ArrayList<>(Arrays.asList(containersArray));
        } catch (Exception e) {  // Catching a generic exception as a placeholder.
            uiUtils.printFailedMessage("Error reading containers from the database.");
            return new ArrayList<>();
        }
    }

    // Modularized method to write containers to the database
    private void writeContainersToDatabase(List<Container> containersList) {
        dbHandler.writeObjects(CONTAINER_FILE_PATH, containersList.toArray(new Container[0]));
    }

    // Modularized method to find a container by its ID
    public Optional<Container> findContainerById(String containerId) {
        return fetchContainersFromDatabase().stream()
                .filter(container -> container.getContainerId().equals(containerId))
                .findFirst();
    }

    public Container getContainerById(String containerId) {
        return fetchContainersFromDatabase().stream()
                .filter(container -> container.getContainerId().equals(containerId))
                .findFirst().orElse(null);
    }

    public boolean uniqueContainerIdCheck(String containerId) {
        if (findContainerById(containerId).isPresent()) {
            uiUtils.printFailedMessage("Container with ID " + containerId + " already exists.");
            return false;
        }
        return true;
    }


    @Override
    public void createNewContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER CREATION WIZARD", 100);
        System.out.println();

        String containerId;
        do {
            containerId = inputValidation.idValidation("C", "Enter container ID to create: ");
        } while (!uniqueContainerIdCheck(containerId));  // Keep asking until a unique ID is provided
        System.out.println();

        double weight = inputValidation.getDouble("Enter container weight:");
        System.out.println();

        Container newContainer = null;
        boolean continuePrompt = true;

        while(continuePrompt) {
            System.out.println("Select container type:");
            System.out.println("1. Dry storage");
            System.out.println("2. Open Side");
            System.out.println("3. Open Top");
            System.out.println("4. Liquid");
            System.out.println("5. Refrigerated");
            int choice = inputValidation.getInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    newContainer = new DryStorage(containerId, weight);
                    continuePrompt = false;
                    break;
                case 2:
                    newContainer = new OpenSide(containerId, weight);
                    continuePrompt = false;
                    break;
                case 3:
                    newContainer = new OpenTop(containerId, weight);
                    continuePrompt = false;
                    break;
                case 4:
                    newContainer = new Liquid(containerId, weight);
                    continuePrompt = false;
                    break;
                case 5:
                    newContainer = new Refrigerated(containerId, weight);
                    continuePrompt = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        List<Container> containerList = fetchContainersFromDatabase();
        containerList.add(newContainer);
        writeContainersToDatabase(containerList);

        uiUtils.printSuccessMessage("Container with ID " + containerId + " created successfully.");
    }

    @Override
    public void findContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER SEARCH WIZARD", 100);
        System.out.println();

        String containerIdToDisplay = inputValidation.idValidation("C", "Enter container ID to search: ");
        System.out.println();

        Optional<Container> optionalContainer = findContainerById(containerIdToDisplay);

        if (optionalContainer.isPresent()) {
            Container containerToDisplay = optionalContainer.get();
            uiUtils.printTopBorderWithTableName("CONTAINER INFO", 15, 20, 15, 20);
            System.out.printf("| %-15s | %-20s | %-15s | %-20s |\n", "Container ID", "Container Type", "Weight", "Current Location");
            displayContainerTableRow(containerToDisplay);
            uiUtils.printHorizontalLine(15, 20, 15, 20);
        } else {
            uiUtils.printFailedMessage("No container found with the given ID.");
        }
    }

    @Override
    public void displayAllContainers() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINERS INFORMATION", 100);
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();
        uiUtils.printTopBorderWithTableName("CONTAINERS INFO", 15, 20, 15, 20);
        System.out.printf("| %-15s | %-20s | %-15s | %-20s |\n", "Container ID", "Container Type", "Weight", "Current Location");
        for (Container container : containerList) {
            displayContainerTableRow(container);
        }
        uiUtils.printHorizontalLine(15, 20, 15, 20);
    }

    public void displayContainerTableRow(Container container) {
        if (container.getCurrentPort() == null && container.getCurrentVehicle() == null) {
            System.out.printf("| %-15s | %-20s | %-15.2f | %-20s |\n",
                    container.getContainerId(), container.getContainerType(),
                    container.getWeight(), "N/A");
        } else if (container.getCurrentPort() == null) {
            System.out.printf("| %-15s | %-20s | %-15.2f | %-20s |\n",
                    container.getContainerId(), container.getContainerType(),
                    container.getWeight(), container.getCurrentVehicle().getName());
        } else {
            System.out.printf("| %-15s | %-20s | %-15.2f | %-20s |\n",
                    container.getContainerId(), container.getContainerType(),
                    container.getWeight(), container.getCurrentPort().getName());
        }
    }

    @Override
    public void updateContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER UPDATE WIZARD", 100);
        System.out.println();

        String containerIdToUpdate = inputValidation.idValidation("C", "Enter container ID to update: ");
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();

        Container containerToUpdate = null;
        int indexToUpdate = -1;  // new variable to keep track of the index in the list
        for (int i = 0; i < containerList.size(); i++) {
            if (containerList.get(i).getContainerId().equals(containerIdToUpdate)) {
                containerToUpdate = containerList.get(i);
                indexToUpdate = i;
                break;
            }
        }

        if (containerToUpdate != null) {
            System.out.print("Enter new container weight (leave blank to keep unchanged): ");
            String newWeight = scanner.nextLine();
            if (!newWeight.isEmpty()) {
                containerToUpdate.setWeight(Double.parseDouble(newWeight));
            }

            System.out.println("Select new container type (or leave blank to keep unchanged):");
            System.out.println("1. Dry storage");
            System.out.println("2. Open Side");
            System.out.println("3. Open Top");
            System.out.println("4. Liquid");
            System.out.println("5. Refrigerated storage");
            System.out.print("Enter your choice: ");
            String typeChoiceStr = scanner.nextLine();

            if (!typeChoiceStr.isEmpty()) {
                if (!typeCheck.isInt(typeChoiceStr)) {
                    uiUtils.printFailedMessage("Invalid choice.");
                    return;
                }
                int typeChoice = Integer.parseInt(typeChoiceStr);
                switch (typeChoice) {
                    case 1 -> containerToUpdate = new DryStorage(containerIdToUpdate, Double.parseDouble(newWeight));
                    case 2 -> containerToUpdate = new OpenSide(containerIdToUpdate, Double.parseDouble(newWeight));
                    case 3 -> containerToUpdate = new OpenTop(containerIdToUpdate, Double.parseDouble(newWeight));
                    case 4 -> containerToUpdate = new Liquid(containerIdToUpdate, Double.parseDouble(newWeight));
                    case 5 -> containerToUpdate = new Refrigerated(containerIdToUpdate, Double.parseDouble(newWeight));
                    default -> {
                        uiUtils.printFailedMessage("Invalid choice.");
                        return;
                    }
                }
                containerList.set(indexToUpdate, containerToUpdate);  // Replace the old container with the updated one
            }

            dbHandler.writeObjects(CONTAINER_FILE_PATH, containerList.toArray(new Container[0]));
            uiUtils.printSuccessMessage("Container with ID " + containerIdToUpdate + " updated successfully.");
        } else {
            uiUtils.printFailedMessage("No container found with the given ID.");
        }
    }


    @Override
    public void deleteContainer() {
        uiUtils.clearScreen();

        uiUtils.printFunctionName("CONTAINER DELETE WIZARD", 100);
        System.out.println();

        String containerIdToDelete = inputValidation.idValidation("C", "Enter container ID to delete: ");
        System.out.println();

        List<Container> containerList = fetchContainersFromDatabase();

        boolean isDeleted = false;
        Iterator<Container> iterator = containerList.iterator();
        while (iterator.hasNext()) {
            Container container = iterator.next();
            if (container.getContainerId().equals(containerIdToDelete)) {
                iterator.remove();
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            dbHandler.writeObjects(CONTAINER_FILE_PATH, containerList.toArray(new Container[0]));
            uiUtils.printSuccessMessage("Container with ID " + containerIdToDelete + " deleted successfully.");
        } else {
            uiUtils.printFailedMessage("No container found with the given ID.");
        }
    }

    private Container getContainerById(List<Container> containers, String containerId) {
        for (Container container : containers) {
            if (Objects.equals(container.getContainerId(), containerId)) {
                return container;
            }
        }
        return null;
    }

    public void updateContainerInDatabase(Container containerToUpdate) {
        // Load all containers from the database
        List<Container> containerList = fetchContainersFromDatabase();

        // Find the index of the container to update
        int indexToUpdate = -1;
        for (int i = 0; i < containerList.size(); i++) {
            if (containerList.get(i).getContainerId().equals(containerToUpdate.getContainerId())) {
                indexToUpdate = i;
                break;
            }
        }

        // If container was found in the list, replace it with the updated one and save back to the database
        if (indexToUpdate != -1) {
            containerList.set(indexToUpdate, containerToUpdate);

            // Assuming you have a method to save a list of containers to the database, something like:
            writeContainersToDatabase(containerList);
        }
    }


}
