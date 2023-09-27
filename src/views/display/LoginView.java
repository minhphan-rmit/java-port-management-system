package views.display;

import models.user.PortManager;
import models.user.SystemAdmin;
import models.user.User;
import services.admin.UserServicesAdmin;
import views.BaseView;
import views.flow.AdminFlow;
import views.flow.PortManagerFlow;
import utils.UiUtils;

import java.io.Console;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Represents the login view of the application.
 * This class is responsible for capturing user input for login and directing the user to the appropriate view based on their role.
 */
public class LoginView extends BaseView {

    // Scanner instance to capture user input.
    private final Scanner scanner = new Scanner(System.in);

    // Controller to handle user-related operations.
    private final UserServicesAdmin userController = new UserServicesAdmin();

    // Flow for admin users.
    private final AdminFlow adminFlow = new AdminFlow();

    // Flow for port manager users. Lazy initialization is used for this instance.
    private PortManagerFlow portManagerFlow;

    // Static display instance to show static messages.
    private final StaticDisplay staticDisplay = new StaticDisplay();

    private final UiUtils uiUtils = new UiUtils();

    /**
     * Lazy initialization for the PortManagerFlow instance.
     *
     * @return The initialized PortManagerFlow instance.
     */
    private PortManagerFlow getPortManagerFlow() {
        if (portManagerFlow == null) {
            portManagerFlow = new PortManagerFlow();
        }
        return portManagerFlow;
    }

    /**
     * Displays the login view, captures user input, and directs the user to the appropriate view based on their role.
     */
    public void displayLoginView() throws ParseException {
        printSeparator(30);
        printCentered("LOGIN", 30);
        printSeparator(30);

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = readPassword().trim();

        // Validate the user's credentials.
        User loggedInUser = userController.loginValidation(username, password);

        printSeparator(30);

        if (loggedInUser != null) {
            // Set the current logged-in user.
            utils.CurrentUser.setUser(loggedInUser);

            // Display login successful message
            staticDisplay.loginSuccessful();

            // Direct the user to the appropriate view based on their role.
            if (loggedInUser instanceof SystemAdmin) {
                adminFlow.displayAdminMenu();
            } else if (loggedInUser instanceof PortManager) {
                getPortManagerFlow().PortManagerMenu();  // Use the getter method for lazy initialization
            }
        } else {
            // Display login failure messages.
            staticDisplay.loginFailed();
            staticDisplay.thankYou();
        }
        printSeparator(30);
    }

    private String readPassword() {
        Console console = System.console();
        if (console != null) {
            char[] passwordChars = console.readPassword();
            return new String(passwordChars);
        } else {
            // Fallback to Scanner if Console is not available (e.g., inside an IDE)
            return scanner.nextLine();
        }
    }
}
