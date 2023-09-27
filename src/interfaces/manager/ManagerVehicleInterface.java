package interfaces.manager;

import java.text.ParseException;

public interface ManagerVehicleInterface extends ManagerInterface {
    void refuelVehicle();
    void deployVehicle() throws ParseException;
    void addExistingVehicle();
}
