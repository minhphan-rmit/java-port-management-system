package views.display;

import views.BaseView;
import utils.UiUtils;

import java.util.Arrays;

/**
 * Represents a static display utility for the application.
 * This class provides methods to display static messages and separators in the console.
 */
public class StaticDisplay extends BaseView {

    private final UiUtils uiUtils = new UiUtils();

    /**
     * Displays the assessment information including group assignment details, instructor names, and group members.
     */
    public void displayAssessmentInfo() {
        String title = "COSC2081 GROUP ASSIGNMENT";
        String subTitle = "CONTAINER PORT MANAGEMENT SYSTEM";
        String instructor = "Instructor: Mr. Minh Vu & Dr. Phong Ngo";
        String group = "Group: Team Hi";
        String[] members = {
                "Phan Nhat Minh - s3978598",
                "Huynh Duc Gia Tin - s3962053",
                "Nguyen Viet Ha - s3978128",
                "Vu Minh Ha - s3978681"
        };

        int maxWidth = Arrays.stream(members)
                .mapToInt(String::length)
                .max()
                .orElse(0);
        maxWidth = Math.max(maxWidth, subTitle.length());
        maxWidth = Math.max(maxWidth, instructor.length());

        printCentered(title, maxWidth);
        printSeparator(maxWidth);
        printCentered(subTitle, maxWidth);
        printCentered(instructor, maxWidth);
        printCentered(group, maxWidth);
        printSeparator(maxWidth);
        System.out.println("Members: ");
        for (String member : members) {
            printCentered(member, maxWidth);
        }
        printSeparator(maxWidth);
    }


    /**
     * Displays a message indicating a successful login.
     */
    public void loginSuccessful() {
        System.out.println("Login successful!");
    }

    /**
     * Displays a message indicating a failed login attempt.
     */
    public void loginFailed() {
        System.out.println("Login failed!");
    }

    /**
     * Displays a thank you message to the user.
     */
    public void thankYou() {
        System.out.println("Thank you for using our system!");
    }
}
