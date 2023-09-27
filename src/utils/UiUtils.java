package utils;

import java.io.IOException;

public class UiUtils {
    public static void printSeparator(int width) {
        System.out.println("=".repeat(width));
    }

    public static void printCentered(String text, int width) {
        int paddingSize = (width - text.length()) / 2;
        for (int i = 0; i < paddingSize; i++) {
            System.out.print(" ");
        }
        System.out.println(text);
    }

    public void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void displayHorizontalLine(int width) {
        System.out.println("-".repeat(width));
    }

    public void printHorizontalLine(int... columnWidths) {
        int totalWidth = 0;
        for (int width : columnWidths) {
            totalWidth += width + 3; // +3 accounts for the "| " at the beginning and " " at the end of each column
        }
        for (int i = 0; i < totalWidth + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public void printFunctionName(String functionName, int width) {

        int remainingWidth = width - functionName.length() - 2;
        int halfWidth = remainingWidth / 2;

        for (int i = 0; i < halfWidth; i++) {
            System.out.print("-");
        }
        System.out.print(" " + "\u001B[1m" + "\u001B[36m" + functionName + "\u001B[0m" + "\u001B[0m" + " ");
        for (int i = halfWidth + functionName.length() + 2; i < width + 1; i++) {
            System.out.print("-");
        }
    }

    public void printTopBorderWithTableName(String tableName, int... widths) {
        int totalWidth = 0;
        for (int width : widths) {
            // 3 extra characters account for spaces and the "|"
            totalWidth += width + 3;
        }

        // Length of the border without the table name
        int remainingWidth = totalWidth - tableName.length() - 2; // subtract 2 for the spaces around the table name
        int halfWidth = remainingWidth / 2;

        for (int i = 0; i < halfWidth; i++) {
            System.out.print("-");
        }

        System.out.print(" " +"\u001B[1m" + "\u001B[32m" + tableName + "\u001B[0m" + "\u001B[0m"  + " ");

        for (int i = halfWidth + tableName.length() + 2; i < totalWidth + 1; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public String makeTextGreen(String text) {
        return "\u001B[32m" + text + "\u001B[0m";
    }

    public void printSuccessMessage(String message) {
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    public void printFailedMessage(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }
}
