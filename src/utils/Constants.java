package utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Constants {

    // Determine the root directory based on the location of the Constants class file
    private static final String ROOT_DIR;
    static {
        try {
            File classDir = new File(Constants.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String potentialRoot = classDir.getParentFile().getParentFile().getParentFile().getParent() + File.separator;
            if (new File(potentialRoot, "src").exists()) {
                ROOT_DIR = potentialRoot + "src" + File.separator + "database" + File.separator + "data" + File.separator;
            } else {
                ROOT_DIR = classDir.getParentFile().getParentFile().getParentFile() + File.separator + "src" + File.separator + "database" + File.separator + "data" + File.separator;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to determine root directory", e);
        }
    }

    // File names for serialized data
    private static final String CONTAINER_FILE = "containers.ser";
    private static final String PORT_FILE = "ports.ser";
    private static final String VEHICLE_FILE = "vehicles.ser";
    private static final String TRIP_FILE = "trips.ser";
    private static final String USER_FILE = "users.ser";

    // Full paths for serialized data files
    public static final String CONTAINER_FILE_PATH = ROOT_DIR + CONTAINER_FILE;
    public static final String PORT_FILE_PATH = ROOT_DIR + PORT_FILE;
    public static final String VEHICLE_FILE_PATH = ROOT_DIR + VEHICLE_FILE;
    public static final String TRIP_FILE_PATH = ROOT_DIR + TRIP_FILE;
    public static final String USER_FILE_PATH = ROOT_DIR + USER_FILE;

    // Represents the system's starting date
    public static final Date SYSTEM_DATE = initializeSystemDate();

    private static Date initializeSystemDate() {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse("23-09-2023");
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse system date", e);
        }
    }
}
