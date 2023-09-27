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

package database;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles reading and writing objects to a file.
 */
public class DatabaseHandler {

    private static final Logger logger = Logger.getLogger(DatabaseHandler.class.getName());

    /**
     * Writes an array of objects to a file.
     *
     * @param filePath The path of the file to write to.
     * @param objects  The array of objects to write.
     * @throws IllegalArgumentException If filePath is null or empty, or if objects is null.
     */
    public void writeObjects(String filePath, Object[] objects) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty.");
        }

        if (objects == null) {
            throw new IllegalArgumentException("Objects to write must not be null.");
        }

        try (FileOutputStream fs = new FileOutputStream(filePath);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(objects);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to write objects to file: " + filePath, e);
            throw new RuntimeException("Failed to write objects to file.", e);
        }
    }

    /**
     * Reads an array of objects from a file.
     *
     * @param filePath The path of the file to read from.
     * @return An array of objects read from the file.
     * @throws IllegalArgumentException If filePath is null or empty.
     */
    public Object[] readObjects(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty.");
        }

        Object[] objects = null;

        try (FileInputStream fi = new FileInputStream(filePath);
             ObjectInputStream os = new ObjectInputStream(fi)) {
            objects = (Object[]) os.readObject();
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Class not found during deserialization from file: " + filePath, e);
            throw new RuntimeException("Failed to read objects from file due to class not found.", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to read objects from file: " + filePath, e);
            throw new RuntimeException("Failed to read objects from file.", e);
        }

        return objects;
    }
}

