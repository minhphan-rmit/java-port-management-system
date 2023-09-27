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

package views;

import java.io.IOException;
import java.util.Scanner;

public abstract class BaseView {
    protected Scanner scanner = new Scanner(System.in);

    public void displayMenuHeader(String menuName, int... widths) {
        int totalWidth = 0;
        for (int width : widths) {
            // 3 extra characters account for spaces and the "|"
            totalWidth += width + 3;
        }

        // Length of the border without the table name
        int remainingWidth = totalWidth - menuName.length() - 2; // subtract 2 for the spaces around the table name
        int halfWidth = remainingWidth / 2;

        for (int i = 0; i < halfWidth; i++) {
            System.out.print("-");
        }

        System.out.print(" " +"\u001B[1m" + "\u001B[32m" + menuName + "\u001B[0m" + "\u001B[0m"  + " ");

        for (int i = halfWidth + menuName.length() + 2; i < totalWidth + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    protected void displayHorizontalLine() {
        displayMessage("-".repeat(30));
    }

    protected void displayMessage(String message) {
        System.out.println(message);
    }

    protected void displayOption(String option) {
        System.out.printf("| %-53s |\n", option);
    }

    protected int promptForInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextInt();
    }

    protected void backToMenu() {
        System.out.print("Press enter to continue...");
        scanner.nextLine();
        scanner.nextLine();
    }

    protected void logoutView() {
        System.out.println("\u001B[31mLogging out...\u001B[0m");
        System.out.println("Thank you for using our system!");
    }

    protected void printCentered(String text, int width) {
        int paddingSize = (width - text.length()) / 2;
        for (int i = 0; i < paddingSize; i++) {
            System.out.print(" ");
        }
        System.out.println(text);
    }

    protected void printSeparator(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
