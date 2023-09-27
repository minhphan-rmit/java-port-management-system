package views.flow;

import exceptions.InputValidation;
import services.admin.*;
import services.statistics.PortStatistics;
import services.statistics.TripStatistics;
import services.statistics.VehicleStatistics;
import views.BaseView;
import utils.UiUtils;
import services.statistics.ContainerStatistics;

public class AdminFlow extends BaseView {

    private final PortServicesAdmin adminPortController = new PortServicesAdmin();
    private final VehicleServicesAdmin adminVehicleController = new VehicleServicesAdmin();
    private final ContainerServicesAdmin adminContainerController = new ContainerServicesAdmin();
    private final UserServicesAdmin adminUserController = new UserServicesAdmin();
    private final TripServicesAdmin adminTripController = new TripServicesAdmin();
    private final UiUtils uiUtils = new UiUtils();
    private final ContainerStatistics containerStatistics = new ContainerStatistics();
    private final PortStatistics portStatistics = new PortStatistics();
    private final VehicleStatistics vehicleStatistics = new VehicleStatistics();
    private final TripStatistics tripStatistics = new TripStatistics();
    private final InputValidation inputValidation = new InputValidation();

    public void displayAdminMenu() {
        uiUtils.clearScreen();

        System.out.println("Welcome, System Admin!");
        System.out.println();

        displayMenuHeader("ADMIN MENU", 53);
        displayOption("1. Manage Ports");
        displayOption("2. Manage Containers");
        displayOption("3. Manage Vehicles");
        displayOption("4. Manage Users");
        displayOption("5. Manage Trips");
        displayOption("6. Statistics");
        displayOption("0. Logout");
        uiUtils.printHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-6): ", 0, 6);
        switch (choice) {
            case 1 -> displayAdminPortsMenu();
            case 2 -> displayAdminContainersMenu();
            case 3 -> displayAdminVehiclesMenu();
            case 4 -> displayAdminUsersMenu();
            case 5 -> displayAdminTripsMenu();
            case 6 -> displayAdminStatisticsMenu();
            case 0 -> logoutView();
            default -> {
                displayMessage("Invalid choice. Please try again.");
                displayAdminMenu();
            }
        }
    }

    public void displayAdminPortsMenu() {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN PORTS MENU", 53);
        displayOption("1. Add Port");
        displayOption("2. Update Port");
        displayOption("3. Delete Port");
        displayOption("4. Search Port");
        displayOption("5. View All Ports");
        displayOption("0. Back");
        uiUtils.displayHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                adminPortController.createNewPort();
                backToMenu();
                displayAdminPortsMenu();
            }
            case 2 -> {
                adminPortController.updatePort();
                backToMenu();
                displayAdminPortsMenu();
            }
            case 3 -> {
                adminPortController.deletePort();
                backToMenu();
                displayAdminPortsMenu();
            }
            case 4 -> {
                adminPortController.findPort();
                backToMenu();
                displayAdminPortsMenu();
            }
            case 5 -> {
                adminPortController.displayAllPorts();
                backToMenu();
                displayAdminPortsMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again.");
                backToMenu();
                displayAdminPortsMenu();
            }
        }
    }

    public void displayAdminContainersMenu() {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN CONTAINERS MENU", 53);
        displayOption("1. Add Container");
        displayOption("2. Update Container");
        displayOption("3. Delete Container");
        displayOption("4. Search Container");
        displayOption("5. View All Containers");
        displayOption("0. Back");
        uiUtils.displayHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                adminContainerController.createNewContainer();
                backToMenu();
                displayAdminContainersMenu();
            }
            case 2 -> {
                adminContainerController.updateContainer();
                backToMenu();
                displayAdminContainersMenu();
            }
            case 3 -> {
                adminContainerController.deleteContainer();
                backToMenu();
                displayAdminContainersMenu();
            }
            case 4 -> {
                adminContainerController.findContainer();
                backToMenu();
                displayAdminContainersMenu();
            }
            case 5 -> {
                adminContainerController.displayAllContainers();
                backToMenu();
                displayAdminContainersMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again.");
                backToMenu();
                displayAdminContainersMenu();
            }
        }

    }

    public void displayAdminVehiclesMenu() {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN VEHICLES MENU", 53);
        displayOption("1. Add Vehicle");
        displayOption("2. Update Vehicle");
        displayOption("3. Delete Vehicle");
        displayOption("4. Search Vehicle");
        displayOption("5. View All Vehicles");
        displayOption("0. Back");
        uiUtils.displayHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                adminVehicleController.createNewVehicle();
                backToMenu();
                displayAdminVehiclesMenu();
            }
            case 2 -> {
                adminVehicleController.updateVehicle();
                backToMenu();
                displayAdminVehiclesMenu();
            }
            case 3 -> {
                adminVehicleController.deleteVehicle();
                backToMenu();
                displayAdminVehiclesMenu();
            }
            case 4 -> {
                adminVehicleController.findVehicle();
                backToMenu();
                displayAdminVehiclesMenu();
            }
            case 5 -> {
                adminVehicleController.displayAllVehicles();
                backToMenu();
                displayAdminVehiclesMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again.");
                backToMenu();
                displayAdminVehiclesMenu();
            }
        }
    }

    public void displayAdminUsersMenu() {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN USERS MENU", 53);
        displayOption("1. Add Port Manager");
        displayOption("2. Update Port Manager");
        displayOption("3. Delete Port Manager");
        displayOption("4. Search Port Manager");
        displayOption("5. View All Port Managers");
        displayOption("0. Back");
        uiUtils.displayHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                adminUserController.createNewUser();
                backToMenu();
                displayAdminUsersMenu();
            }
            case 2 -> {
                adminUserController.updateUser();
                backToMenu();
                displayAdminUsersMenu();
            }
            case 3 -> {
                adminUserController.deleteUser();
                backToMenu();
                displayAdminUsersMenu();
            }
            case 4 -> {
                adminUserController.findUser();
                backToMenu();
                displayAdminUsersMenu();
            }
            case 5 -> {
                adminUserController.displayAllUsers();
                backToMenu();
                displayAdminUsersMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again");
                displayAdminUsersMenu();
            }
        }
    }

    public void displayAdminTripsMenu() {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN TRIPS MENU", 53);
        displayOption("1. Add Trip");
        displayOption("2. Update Trip");
        displayOption("3. Delete Trip");
        displayOption("4. Search Trip");
        displayOption("5. View All Trips");
        displayOption("0. Back");
        uiUtils.displayHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-5): ", 0, 5);
        switch (choice) {
            case 1 -> {
                adminTripController.createTrip();
                backToMenu();
                displayAdminTripsMenu();
            }
            case 2 -> {
                adminTripController.updateTrip();
                backToMenu();
                displayAdminTripsMenu();
            }
            case 3 -> {
                adminTripController.deleteTrip();
                backToMenu();
                displayAdminTripsMenu();
            }
            case 4 -> {
                adminTripController.findTrip();
                backToMenu();
                displayAdminTripsMenu();
            }
            case 5 -> {
                adminTripController.displayAllTrips();
                backToMenu();
                displayAdminTripsMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again");
                backToMenu();
                displayAdminTripsMenu();
            }
        }
    }

    public void displayAdminStatisticsMenu () {
        uiUtils.clearScreen();

        displayMenuHeader("ADMIN STATISTICS MENU", 53);
        displayOption("1. View Port Statistics");
        displayOption("2. View Container Statistics");
        displayOption("3. View Vehicle Statistics");
        displayOption("4. View Trip Statistics");
        displayOption("0. Back");
        uiUtils.printHorizontalLine(53);

        int choice = inputValidation.getChoice("Enter your choice (0-4): ", 0, 4);
        switch (choice) {
            case 1 -> {
                uiUtils.clearScreen();
                portStatistics.displayTotalNumberOfPorts();
                System.out.println();
                portStatistics.portUsedCapacity();
                System.out.println();
                portStatistics.portTripCount();
                backToMenu();
                displayAdminStatisticsMenu();
            }
            case 2 -> {
                uiUtils.clearScreen();
                containerStatistics.displayTotalNumberOfContainers();
                System.out.println();
                containerStatistics.containerStatus();
                System.out.println();
                containerStatistics.containerType();
                System.out.println();
                containerStatistics.containerPerPort();
                backToMenu();
                displayAdminStatisticsMenu();
            }
            case 3 -> {
                uiUtils.clearScreen();
                vehicleStatistics.displayTotalNumberOfVehicles();
                System.out.println();
                vehicleStatistics.vehiclePerPort();
                System.out.println();
                vehicleStatistics.vehicleStatus();
                System.out.println();
                vehicleStatistics.vehicleType();
                backToMenu();
                displayAdminStatisticsMenu();
            }
            case 4 -> {
                uiUtils.clearScreen();
                tripStatistics.displayTotalTrips();
                System.out.println();
                tripStatistics.tripStatus();
                System.out.println();
                tripStatistics.tripByVehicle();
                System.out.println();
                tripStatistics.averageTripDuration();
                backToMenu();
                displayAdminStatisticsMenu();
            }
            case 0 -> displayAdminMenu();
            default -> {
                displayMessage("Invalid choice. Please try again");
                backToMenu();
                displayAdminStatisticsMenu();
            }
        }
    }
}

