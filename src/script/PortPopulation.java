package script;

import models.port.Port;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import database.DatabaseHandler;
import utils.Constants;

public class PortPopulation {

    private static final String PORT_FILE_PATH = Constants.PORT_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();

    public void run() {
        List<Port> portList;
        try {
            Port[] portArray = (Port[]) dbHandler.readObjects(PORT_FILE_PATH);
            portList = new ArrayList<>(Arrays.asList(portArray));
        } catch (Exception e) {
            portList = new ArrayList<>();
        }

        String[] portNames = {
                "Cai Lan Port", "Da Nang Port", "Hai Phong Port", "Cam Ranh Port",
                "Vung Tau Port", "Phu My Port", "Qui Nhon Port", "Nghi Son Port",
                "Dung Quat Port", "Can Tho Port"
        };

        double[] latitudes = {
                20.9563, 16.0693, 20.8444, 11.9214, 10.3759, 10.5881, 13.7825, 19.3340, 15.3877, 10.0331
        };

        double[] longitudes = {
                107.0482, 108.2207, 106.7029, 109.1591, 107.0843, 107.0489, 109.2197, 105.7766, 108.7929, 105.7837
        };

        double[] storingCapacities = {
                5000000, 4500000, 6000000, 3000000, 5500000, 4800000, 3500000, 4200000, 4700000, 3800000
        };

        boolean[] landingAbilities = {
                true, true, true, true, true, true, false, true, true, false
        };

        for (int i = 0; i < portNames.length; i++) {
            String portId = String.format("P-%06d", i + 1);
            portList.add(new Port(portId, portNames[i].toUpperCase(), latitudes[i], longitudes[i], storingCapacities[i], landingAbilities[i]));
        }

        dbHandler.writeObjects(PORT_FILE_PATH, portList.toArray(new Port[0]));
    }

    public void emptyDatabase() {
        try {
            dbHandler.writeObjects(PORT_FILE_PATH, new Port[0]);
            // If you have other database files, you can add similar lines here to empty them too.
        } catch (Exception e) {
            System.out.println("Error emptying the database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PortPopulation portPopulation = new PortPopulation();
        portPopulation.emptyDatabase();
        portPopulation.run();
    }
}
