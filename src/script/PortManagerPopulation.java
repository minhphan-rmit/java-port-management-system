package script;

import models.port.Port;
import models.user.PortManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import database.DatabaseHandler;
import utils.Constants;

public class PortManagerPopulation {

    private static final String USER_FILE_PATH = Constants.USER_FILE_PATH;
    private static final String PORT_FILE_PATH = Constants.PORT_FILE_PATH;
    private final DatabaseHandler dbHandler = new DatabaseHandler();

    public void run() {
        List<Port> portList;
        List<PortManager> portManagerList = new ArrayList<>();

        try {
            Port[] portArray = (Port[]) dbHandler.readObjects(PORT_FILE_PATH);
            portList = new ArrayList<>(Arrays.asList(portArray));
        } catch (Exception e) {
            portList = new ArrayList<>();
            System.out.println("Error reading ports: " + e.getMessage());
        }

        String[] usernames = {
                "managerCaiLan", "managerDaNang", "managerHaiPhong", "managerCamRanh",
                "managerVungTau", "managerPhuMy", "managerQuiNhon", "managerNghiSon",
                "managerDungQuat", "managerCanTho"
        };

        String[] passwords = {
                "Pass1234!", "DaNang$2023", "HaiP@ssword", "CamRanh#Port",
                "VungTau$Pass", "PhuMy2023!", "QuiNhon#123", "Nghi$onPass",
                "DungQuat!Pass", "CanTho#Pass23"
        };

        for (int i = 0; i < usernames.length; i++) {
            if (i < portList.size()) {
                portManagerList.add(new PortManager(usernames[i], passwords[i], portList.get(i)));
            }
        }

        dbHandler.writeObjects(USER_FILE_PATH, portManagerList.toArray(new PortManager[0]));
    }

    public void emptyDatabase() {
        try {
            dbHandler.writeObjects(USER_FILE_PATH, new PortManager[0]);
            // If you have other database files, you can add similar lines here to empty them too.
        } catch (Exception e) {
            System.out.println("Error emptying the database: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PortManagerPopulation portManagerPopulation = new PortManagerPopulation();
        portManagerPopulation.emptyDatabase();
        portManagerPopulation.run();
    }
}
