/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author timothy
 */
public class InputUtils {

    // Method to read a valid long input from the user
    public static long readLong(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextLong()) {
                long num = scanner.nextLong();
                scanner.nextLine();
                return num;
            } else {
                System.out.println("Invalid input. Please enter a valid long value.");
                scanner.nextLine(); // Clear the entire invalid input line
            }
        }
    }
    
    public static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double num = scanner.nextDouble();
                scanner.nextLine(); // Clear the newline character after reading the number
                return num;
            } else {
                System.out.println("Invalid input. Please enter a valid double value.");
                scanner.nextLine(); // Clear the entire invalid input line
            }
        }
    }

    // Method to read a valid int input from the user
    public static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();
                return num;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the entire invalid input line
            }
        }
    }

//    // Method for reading a single word or token (no spaces allowed)
//    public static String readWord(Scanner scanner, String prompt) {
//        System.out.print(prompt);
//        String word = scanner.next();  // Reads the next token before any whitespace
//        scanner.nextLine();
//        return word;
//    }
    
    public static String readString(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();  // Reads the entire line, including spaces
    }


    // Method to read a valid LocalDate input (YYYY-MM-DD format)
    public static LocalDate readDate(Scanner scanner, String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                // Try to parse the input string into a LocalDate
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

}
