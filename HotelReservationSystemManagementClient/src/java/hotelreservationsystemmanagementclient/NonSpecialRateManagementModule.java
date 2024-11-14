/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.RoomType;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.client.InputUtils;

/**
 *
 * @author timothy
 */
public class NonSpecialRateManagementModule {
    
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    public NonSpecialRateManagementModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
    }
    
    // RoomType Management Menu
    void manageNonSpecialRates(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Normal Rate Management ---");
            System.out.println("1. Update Non-Special Rates");
            System.out.println("0. Back to Main Menu");
            int choice = InputUtils.readInt(scanner, "> ");

            switch (choice) {
                case 1:
                    updateNonSpecialRatesForRoomType(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void viewAllRoomTypes() {
        try {
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes(); // Assuming session bean method
            if (roomTypes.isEmpty()) {
                System.out.println("No room types available.");
            } else {
                System.out.println("\n--- List of Room Types ---");
                System.out.printf("%-10s %-20s %-20s %-20s %-15s\n", "ID", "Room Type", "Normal Rate", "Published Rate", "Disabled");
                System.out.println("-----------------------------------------------------------------------------------------");
                for (RoomType roomType : roomTypes) {
                    String disabledStatus = roomType.isDisabled() ? "Yes" : "No";
                    System.out.printf("%-10d %-20s %-20.2f %-20.2f %-15s\n", 
                                      roomType.getId(), 
                                      roomType.getName(), 
                                      roomType.getNormalRate(), 
                                      roomType.getPublishedRate(),
                                      disabledStatus);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching room types: " + e.getMessage());
        }
    }

    private void updateNonSpecialRatesForRoomType(Scanner scanner) {
        try {
            // Display all room types for user to select
            viewAllRoomTypes();

            // Prompt for RoomType ID to update
            Long roomTypeId = InputUtils.readLong(scanner, "Enter Room Type ID to update rate: ");

            // Fetch the room type to update
            RoomType roomType = roomTypeEntitySessionBeanRemote.findRoomType(roomTypeId);

            if (roomType == null) {
                System.out.println("Room Type not found with ID " + roomTypeId);
                return;
            }

            // Show current details
            System.out.println("Current Room Type Name: " + roomType.getName());
            System.out.println("Current Normal Rate: " + roomType.getNormalRate());
            System.out.println("Current Published Rate: " + roomType.getPublishedRate());

            // Prompt for new values, allowing user to skip by pressing Enter
            String newNormalRate = InputUtils.readString(scanner, "Enter new Normal Rate (leave blank to keep current): ");
            if (!newNormalRate.isEmpty()) {
                try {
                    roomType.setNormalRate(Double.parseDouble(newNormalRate));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Normal Rate. Keeping the current value.");
                }
            }

            String newPublishedRate = InputUtils.readString(scanner, "Enter new Published Rate (leave blank to keep current): ");
            if (!newPublishedRate.isEmpty()) {
                try {
                    roomType.setPublishedRate(Double.parseDouble(newPublishedRate));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Published Rate. Keeping the current value.");
                }
            }

            // Update the room type
            RoomType updatedRoomType = roomTypeEntitySessionBeanRemote.updateRoomType(roomType);
            System.out.println("Room Type updated successfully.");
            System.out.println("Updated Room Type details: " + updatedRoomType);

        } catch (Exception e) {
            System.out.println("An error occurred while updating room type: " + e.getMessage());
        }
    }

}
