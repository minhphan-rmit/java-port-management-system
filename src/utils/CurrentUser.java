/*
  RMIT University Vietnam
  Course: COSC2081 Programming 1
  Semester: 2023B
  Assessment: Group Assignment
  Group: Team Hi
  Members:
  Phan Nhat Minh - s3978598
  Huynh Duc Gia Tin - s3818078
  Nguyen Viet Ha - s3978128
  Vu Minh Ha - s3978681
  Created  date: 02/09/2023
  Acknowledgement: chat.openai.com, stackoverflow.com, geeksforgeeks.org, javatpoint.com, tutorialspoint.com, oracle.com, w3schools.com, github.com
*/

package utils;

import models.user.User;

/**
 * Utility class to manage the current logged-in user's session.
 * This class provides static methods to get, set, and clear the current user.
 */
public class CurrentUser {

    // Holds the current logged-in user's instance.
    private static User currentUser;

    /**
     * Retrieves the current logged-in user.
     *
     * @return The current user or null if no user is logged in.
     */
    public static User getUser() {
        return currentUser;
    }

    /**
     * Sets the current logged-in user.
     *
     * @param user The user to be set as the current user.
     */
    public static void setUser(User user) {
        currentUser = user;
    }

    /**
     * Clears the current logged-in user, effectively logging them out.
     */
    public static void clearUser() {
        currentUser = null;
    }
}

