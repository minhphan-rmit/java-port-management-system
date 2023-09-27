package services.statistics;

import interfaces.statistics.ContainerStatInterface;
import models.container.Container;
import services.admin.ContainerServicesAdmin;
import utils.UiUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContainerStatistics extends Statistics implements ContainerStatInterface {

    private final ContainerServicesAdmin containerController = new ContainerServicesAdmin();
    private final UiUtils uiUtils = new UiUtils();

    public void displayTotalNumberOfContainers() {
        int total = containerController.fetchContainersFromDatabase().size();

        uiUtils.printTopBorderWithTableName("Total number of containers in the system", 53);
        System.out.printf("| %-53s |\n", total + " containers");
        uiUtils.printHorizontalLine(53);
    }

    public void containerStatus() {
        List<Container> allContainers = containerController.fetchContainersFromDatabase();
        int total = allContainers.size();

        String[] statuses = {"Loaded", "Awaiting loading", "In transit", "Unoccupied"};

        uiUtils.printTopBorderWithTableName("Container status", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Status", "Count");
        uiUtils.printHorizontalLine(30, 20);
        for (String status : statuses) {
            long count = allContainers.stream()
                    .filter(container -> container.getContainerStatus().equals(status))
                    .count();
            System.out.printf("| %-30s | %-20d |\n", status, count);
        }
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }

    public void containerType() {
        List<Container> allContainers = containerController.fetchContainersFromDatabase();
        int total = allContainers.size();

        String[] types = {"Refrigerated", "Liquid", "Dry Storage", "Open Top", "Open Side"};
        uiUtils.printTopBorderWithTableName("Container type", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Type", "Count");
        uiUtils.printHorizontalLine(30, 20);
        for (String type : types) {
            long count = allContainers.stream()
                    .filter(container -> container.getContainerType().equals(type))
                    .count();
            System.out.printf("| %-30s | %-20d |\n", type, count);
        }
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }

    public void containerPerPort() {
        List<Container> allContainers = containerController.fetchContainersFromDatabase();
        int total = allContainers.size();

        Map<String, Long> containerCountPerPort = allContainers.stream()
                .collect(Collectors.groupingBy(container -> {
                    if (container.getCurrentPort() == null) {
                        return "No Port";  // Grouping containers that do not belong to any port
                    }
                    return container.getCurrentPort().getPortId();
                }, Collectors.counting()));

        uiUtils.printTopBorderWithTableName("Containers per port", 30, 20);
        System.out.printf("| %-30s | %-20s |\n", "Port ID", "Count");
        uiUtils.printHorizontalLine(30, 20);
        containerCountPerPort.forEach((portId, count) -> {
            System.out.printf("| %-30s | %-20d |\n", portId, count);
        });
        uiUtils.printHorizontalLine(30, 20);
        System.out.printf("| %-30s | %-20d |\n", "Total", total);
        uiUtils.printHorizontalLine(30, 20);
    }
}
