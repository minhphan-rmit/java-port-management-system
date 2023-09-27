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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class for date-related operations.
 */
public class DateUtils {

    /**
     * Converts hours since the epoch to a formatted date string.
     *
     * @param hoursSinceEpoch Hours (as a double) since January 1, 1970, 00:00:00 GMT.
     * @param pattern The pattern describing the date and time format.
     * @return Formatted date string.
     */
    public String hoursToDate(double hoursSinceEpoch, String pattern) {
        // Convert hours to milliseconds
        long millisSinceEpoch = (long) (hoursSinceEpoch * 60 * 60 * 1000);

        // Create a Date object from the milliseconds since epoch
        Date date = new Date(millisSinceEpoch);

        // Format the date using the provided pattern
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // Set the time zone to GMT. Adjust if needed.
        return sdf.format(date);
    }

    /**
     * Adds hours to a given date and returns the resulting date as a string.
     *
     * @param dateStr The date string.
     * @param pattern The pattern describing the date and time format.
     * @param hoursToAdd Hours (as a double) to add to the given date.
     * @return The resulting date after adding the hours, formatted as a string.
     * @throws ParseException If the provided date string cannot be parsed.
     */
    public String addHoursToDate(String dateStr, String pattern, double hoursToAdd) throws ParseException {
        // Parse the provided date string using the given pattern
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = sdf.parse(dateStr);

        // Convert hours to milliseconds and add to the date's time
        long millisToAdd = (long) (hoursToAdd * 60 * 60 * 1000);
        date.setTime(date.getTime() + millisToAdd);

        // Return the updated date as a formatted string
        return sdf.format(date);
    }
}

