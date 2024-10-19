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

/**
 *
 * @author timothy
 */
public class RoomTypeManagementModule {
    
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;

    public RoomTypeManagementModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
    }
    
    // RoomType Management Menu
    void manageRoomTypes(Scanner scanner) {
        while (true) {
            System.out.println("\n--- RoomType Management ---");
            System.out.println("1. View All RoomTypes");
            System.out.println("2. View RoomType");
            System.out.println("3. Create RoomType");
            System.out.println("4. Update RoomType");
            System.out.println("5. Delete RoomType");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllRoomTypes();
                    break;
                case 2:
                    viewRoomType(scanner);
                    break;
                case 3:
                    createRoomType(scanner);
                    break;
                case 4:
                    updateRoomType(scanner);
                    break;
                case 5:
                    deleteRoomType(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    // RoomType methods
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

    private void viewRoomType(Scanner scanner) {
        try {
            System.out.print("Enter Room Type ID to view: ");
            Long roomTypeId = scanner.nextLong();
            RoomType roomType = roomTypeEntitySessionBeanRemote.findRoomType(roomTypeId);

            if (roomType != null) {
                System.out.println("Room Type Details: " + roomType);
            } else {
                System.out.println("Room Type not found with ID " + roomTypeId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching room type: " + e.getMessage());
        }
    }

    private void createRoomType(Scanner scanner) {
        try {
            scanner.nextLine();
            // Collect room type details
            System.out.print("Enter new room type name: ");
            String roomTypeName = scanner.nextLine().trim();
            
            // Add input validation if needed
            if (roomTypeName.isEmpty()) {
                System.out.println("Room type name cannot be empty!");
                return;
            }

            System.out.print("Enter normal rate: ");
            double normalRate = scanner.nextDouble();

            System.out.print("Enter published rate: ");
            double publishedRate = scanner.nextDouble();

            // Create the RoomType object and associated rates
            RoomType newRoomType = new RoomType(roomTypeName, normalRate, publishedRate);

            // Call the session bean to persist the new RoomType
            roomTypeEntitySessionBeanRemote.createRoomType(newRoomType);

            System.out.println("Room type created successfully!");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter valid numeric values for rates.");
            scanner.nextLine(); // Consume the invalid input
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void updateRoomType(Scanner scanner) {
        try {
            // Display all room types for user to select
            viewAllRoomTypes();

            // Prompt for RoomType ID to update
            System.out.print("Enter Room Type ID to update: ");
            Long roomTypeId = scanner.nextLong();

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
            scanner.nextLine();
            System.out.print("Enter new Room Type name (leave blank to keep current): ");
            String newRoomTypeName = scanner.nextLine().trim();
            if (!newRoomTypeName.isEmpty()) {
                roomType.setName(newRoomTypeName);
            }

            System.out.print("Enter new Normal Rate (leave blank to keep current): ");
            String newNormalRate = scanner.nextLine().trim();
            if (!newNormalRate.isEmpty()) {
                try {
                    roomType.setNormalRate(Double.parseDouble(newNormalRate));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input for Normal Rate. Keeping the current value.");
                }
            }

            System.out.print("Enter new Published Rate (leave blank to keep current): ");
            String newPublishedRate = scanner.nextLine().trim();
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


    private void deleteRoomType(Scanner scanner) {
        try {
            System.out.print("Enter Room Type ID to delete: ");
            Long roomTypeId = scanner.nextLong();

            // Fetch the room type to delete
            RoomType roomType = roomTypeEntitySessionBeanRemote.findRoomType(roomTypeId);

            if (roomType == null) {
                System.out.println("Room Type not found with ID " + roomTypeId);
                return;
            }

            // Display room type details before confirmation
            System.out.println("Room Type Details: " + roomType);

            // Confirm deletion
            System.out.print("Are you sure you want to delete this Room Type? (y/n): ");
            String confirmation = scanner.next();

            if ("y".equalsIgnoreCase(confirmation)) {
                RoomType deletedRoomType = roomTypeEntitySessionBeanRemote.deleteRoomType(roomTypeId);
                System.out.println("Room Type deleted successfully: " + deletedRoomType);
            } else {
                System.out.println("Room Type deletion canceled.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting room type: " + e.getMessage());
        }
    }
}
