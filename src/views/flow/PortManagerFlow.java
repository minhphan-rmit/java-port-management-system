package views.flow;

import exceptions.InputValidation;
import views.BaseView;
import services.manager.*;
import utils.UiUtils;
import views.statistics.ManagerStatistics;

import java.text.ParseException;

public class PortManagerFlow extends BaseView {

    private final PortServicesManager portServices = new PortServicesManager();
    private final ContainerServicesManager containerServices = new ContainerServicesManager();
    private final VehicleServicesManager vehicleServices = new VehicleServicesManager();
    private final ManagerStatistics statistics = new ManagerStatistics();
    private final UiUtils uiUtils = new UiUtils();
    private final InputValidation inputValidation = new InputValidation();

    public void PortManagerMenu() throws ParseException {
        uiUtils.clearScreen();
        System.out.println("Welcome, Port Manager!");
        System.out.println();

        displayMenuHeader("PORT MANAGER MENU", 53);
        displayOption("1. Manage Containers");
        displayOption("2. Manage Vehicles");
        displayOption("3. View Port Details");
        displayOption("4. Update Port Details");
        displayOption("5. View Statistics");
        displayOption("0. Logout");
        uiUtils.printHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> PManagerContainerMenu();
            case 2 -> PManagerVehicleMenu();
            case 3 -> {
                portServices.viewPortInfo();
                backToMenu();
                PortManagerMenu();
            }
            case 4 -> {
                portServices.updatePortInfo();
                backToMenu();
                PortManagerMenu();
            }
            case 5 -> PManagerStatisticsMenu();
            case 0 -> logoutView();
            default -> {
                uiUtils.printFailedMessage("Invalid choice. Please try again.");
                PortManagerMenu();
            }
        }
    }

    public void PManagerContainerMenu() throws ParseException {
        uiUtils.clearScreen();

        displayMenuHeader("PORT MANAGER CONTAINER MENU", 53);
        displayOption("1. Load Container");
        displayOption("2. Unload Container");
        uiUtils.printHorizontalLine(53);
        displayOption("3. Add Existing Container");
        displayOption("4. Add New Container");
        displayOption("5. Update Container");
        displayOption("6. Delete Container");
        displayOption("7. Search Container");
        displayOption("8. View All Containers");
        displayOption("0. Back");
        uiUtils.printHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter you choice (0-8): ", 0, 8);
        switch (choice) {
            case 1 -> {
                containerServices.loadContainerFlow();
                backToMenu();
                PManagerContainerMenu();
            }
            case 2 -> {
                containerServices.unloadContainerFlow();
                backToMenu();
                PManagerContainerMenu();
            }
            case 3 -> {
                containerServices.addExistingContainer();
                backToMenu();
                PManagerContainerMenu();
            }
            case 4 -> {
                containerServices.createNewContainer();
                backToMenu();
                PManagerContainerMenu();
            }
            case 5 -> {
                containerServices.updateContainer();
                backToMenu();
                PManagerContainerMenu();
            }
            case 6 -> {
                containerServices.deleteContainer();
                backToMenu();
                PManagerContainerMenu();
            }
            case 7 -> {
                containerServices.findContainer();
                backToMenu();
                PManagerContainerMenu();

            }
            case 8 -> {
                containerServices.displayAllContainers();
                backToMenu();
                PManagerContainerMenu();
            }
            case 0 -> PortManagerMenu();
            default -> {
                uiUtils.printFailedMessage("Invalid choice. Please try again.");
                PManagerContainerMenu();
            }
        }
    }

    public void PManagerVehicleMenu() throws ParseException {
        uiUtils.clearScreen();

        displayMenuHeader("PORT MANAGER VEHICLE MENU", 53);
        displayOption("1. Deploy Vehicle (Create Trip)");
        displayOption("2. Refuel Vehicle");
        displayOption("3. Unload Vehicle");
        uiUtils.printHorizontalLine(53);
        displayOption("4. Add Existing Vehicle");
        displayOption("5. Create New Vehicle");
        displayOption("6. Update Vehicle");
        displayOption("7. Delete Vehicle");
        displayOption("8. Search Vehicle");
        displayOption("9. View All Vehicles");
        displayOption("0. Back");
        uiUtils.printHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter you choice (0-8): ", 0, 9);
        switch (choice) {
            case 1 -> {
                vehicleServices.deployVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 2 -> {
                vehicleServices.refuelVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 3 -> {
                vehicleServices.unloadVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 4 -> {
                vehicleServices.addExistingVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 5 -> {
                vehicleServices.createNewVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 6 -> {
                vehicleServices.updateVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 7 -> {
                vehicleServices.deleteVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 8 -> {
                vehicleServices.findVehicle();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 9 -> {
                vehicleServices.displayAllVehicles();
                backToMenu();
                PManagerVehicleMenu();
            }
            case 0 -> PortManagerMenu();
            default -> {
                uiUtils.printFailedMessage("Invalid choice. Please try again.");
                PManagerVehicleMenu();
            }
        }
    }

    public void PManagerStatisticsMenu() throws ParseException {
        uiUtils.clearScreen();

        displayMenuHeader("PORT MANAGER STATISTICS MENU", 53);
        displayOption("1. Daily Fuel Usage");
        displayOption("2. Weight of each type of container");
        displayOption("3. List ships in your port");
        displayOption("4. List trips in a day");
        displayOption("0. Back");

        int choice = inputValidation.getChoice("Enter your choice (0-4): ", 0, 4);
        switch (choice) {
            case 1 -> {
                statistics.displayFuelUsedInDay();
                backToMenu();
                PManagerStatisticsMenu();
            }
            case 2 -> {
                statistics.displayWeightOfContainerByType();
                backToMenu();
                PManagerStatisticsMenu();
            }
            case 3 -> {
                statistics.displayAllShipsInPort();
                backToMenu();
                PManagerStatisticsMenu();
            }
            case 4 -> {
                statistics.displayAllTripsInDuration();
                backToMenu();
                PManagerStatisticsMenu();
            }
            case 0 -> PortManagerMenu();
            default -> {
                displayMessage("Invalid choice. Please try again.");
                PManagerStatisticsMenu();
            }
        }
    }
}

