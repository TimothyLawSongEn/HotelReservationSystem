/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.RoomRate;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import util.client.InputUtils;

/**
 *
 * @author timothy
 */
public class SpecialRateManagementModule {
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;

    public SpecialRateManagementModule(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateEntitySessionBeanRemote;
    }
    
    // RoomRate Management Menu
    void manageSpecialRates(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Special Rate Management (Promo/Peak) ---");
            System.out.println("1. View All Special Rates");
            System.out.println("2. View Special Rate");
            System.out.println("3. Create Special Rate ");
            System.out.println("4. Update Special Rate");
            System.out.println("5. Delete Special Rate");
            System.out.println("0. Back to Main Menu");
            int choice = InputUtils.readInt(scanner, "> ");

            switch (choice) {
                case 1:
                    viewAllSpecialRates();
                    break;
                case 2:
                    viewSpecialRate(scanner);
                    break;
                case 3:
                    createSpecialRate(scanner);
                    break;
                case 4:
                    updateSpecialRate(scanner);
                    break;
                case 5:
                    deleteSpecialRate(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    // View all Room Rates categorised by RoomType
    private void viewAllSpecialRates() {
        try {
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes(); // Fetch room types
            List<RoomRate> specialRoomRates = roomRateEntitySessionBeanRemote.findAllRoomRates(); // Fetch special rates

            if (roomTypes.isEmpty()) {
                System.out.println("No room types available.");
            } else {
                System.out.println("\n--- List of Room Rates by Room Type ---");
                for (RoomType roomType : roomTypes) {
                    System.out.println("\nRoom Type: " + roomType.getName());
                    System.out.printf("  Normal Rate: %.2f\n", roomType.getNormalRate());
                    System.out.printf("  Published Rate: %.2f\n", roomType.getPublishedRate());

                    // Filter and display promotional rates
                    System.out.println("  Promotional Rates:");
                    System.out.printf("%-10s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Amount", "Start Date", "End Date");
                    System.out.println("---------------------------------------------------------------");

                    for (RoomRate roomRate : specialRoomRates) {
                        if (roomRate.getRoomType().getId().equals(roomType.getId()) 
                            && RoomRate.SpecialRateType.PROMO.equals(roomRate.getSpecialRateType())) {
                            System.out.printf("%-10d %-20s %-20.2f %-20s %-20s\n", 
                                              roomRate.getId(), 
                                              roomRate.getName(), 
                                              roomRate.getAmount(), 
                                              roomRate.getStartDate(),
                                              roomRate.getEndDate());
                        }
                    }

                    // Filter and display peak rates
                    System.out.println("  Peak Rates:");
                    System.out.printf("%-10s %-20s %-20s %-20s %-20s\n", "ID", "Name", "Amount", "Start Date", "End Date");
                    System.out.println("---------------------------------------------------------------");

                    for (RoomRate roomRate : specialRoomRates) {
                        if (roomRate.getRoomType().getId().equals(roomType.getId()) 
                            && RoomRate.SpecialRateType.PEAK.equals(roomRate.getSpecialRateType())) {
                            System.out.printf("%-10d %-20s %-20.2f %-20s %-20s\n", 
                                              roomRate.getId(), 
                                              roomRate.getName(), 
                                              roomRate.getAmount(), 
                                              roomRate.getStartDate(),
                                              roomRate.getEndDate());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching room rates: " + e.getMessage());
        }
    }



    // View a specific room rate by ID
    private void viewSpecialRate(Scanner scanner) {
        try {
            Long roomRateId = InputUtils.readLong(scanner, "Enter Room Rate ID: ");
            RoomRate roomRate = roomRateEntitySessionBeanRemote.findRoomRate(roomRateId);

            if (roomRate != null) {
                System.out.println("Room Rate Details:");
                System.out.println("ID: " + roomRate.getId());
                System.out.println("Room Type: " + roomRate.getRoomType().getName());
                System.out.println("Rate Type: " + roomRate.getSpecialRateType());
                System.out.println("Amount: " + roomRate.getAmount());
                System.out.println("Start Date: " + roomRate.getStartDate());
                System.out.println("End Date: " + roomRate.getEndDate());
            } else {
                System.out.println("Room rate not found for ID " + roomRateId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the room rate: " + e.getMessage());
        }
    }


    // Create a new room rate
    private void createSpecialRate(Scanner scanner) {
        try {
            // Get room type
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes();
            System.out.println("Choose Room Type:");
            for (int i = 0; i < roomTypes.size(); i++) {
                System.out.printf("%d: %s\n", i + 1, roomTypes.get(i).getName());
            }
            int roomTypeIndex = InputUtils.readInt(scanner, "") - 1;
            RoomType selectedRoomType = roomTypes.get(roomTypeIndex);

            // Get rate type
            RoomRate.SpecialRateType rateType = chooseRateType(scanner);
            if (rateType == null) {
                System.out.println("Rate type selection failed.");
                return;
            }

            String newRateName = InputUtils.readString(scanner, "Enter name: ");
            
            // Get amount
            double amount = InputUtils.readDouble(scanner, "Enter amount: ");

            // Get period (start date and end date)
            LocalDate startDate = InputUtils.readDate(scanner, "Enter start date (yyyy-mm-dd): ");
            LocalDate endDate = InputUtils.readDate(scanner, "Enter end date (yyyy-mm-dd): ");

            // Create room rate object and persist
            RoomRate newRoomRate = new RoomRate(newRateName, amount, startDate, endDate, rateType, selectedRoomType);
            roomRateEntitySessionBeanRemote.persistRoomRate(newRoomRate);

            System.out.println("Room Rate created successfully.");
        } catch (Exception e) {
            System.out.println("An error occurred while creating the room rate: " + e.getMessage());
        }
    }
    
    // Method to display and let the user choose rate type (Promo or Peak), dynamically using enum values
    private RoomRate.SpecialRateType chooseRateType(Scanner scanner) {
        System.out.println("\n--- Available Rate Types ---");
        RoomRate.SpecialRateType[] rateTypes = RoomRate.SpecialRateType.values();

        // Iterate over enum values and display them as options
        for (int i = 0; i < rateTypes.length; i++) {
            System.out.println((i + 1) + ". " + rateTypes[i].name());
        }

        int choice = InputUtils.readInt(scanner, "Please select a rate type (1-" + rateTypes.length + "): ");

        if (choice < 1 || choice > rateTypes.length) {
            System.out.println("Invalid choice, please select a valid rate type.");
            return null;  // Return null if invalid input
        }

        // Return the selected rate type based on user input
        return rateTypes[choice - 1];
    }



    // Update a room rate
    private void updateSpecialRate(Scanner scanner) {
        try {
            Long roomRateId = InputUtils.readLong(scanner, "Enter Room Rate ID to update: ");
            RoomRate roomRate = roomRateEntitySessionBeanRemote.findRoomRate(roomRateId);

            if (roomRate != null) {
                String newAmountStr = InputUtils.readString(scanner, "Enter new amount (or press Enter to keep current): ");
                if (!newAmountStr.isEmpty()) {
                    roomRate.setAmount(Double.parseDouble(newAmountStr));
                }

                String newStartDateStr = InputUtils.readString(scanner, "Enter new start date (yyyy-mm-dd) (or press Enter to keep current): ");
                if (!newStartDateStr.isEmpty()) {
                    roomRate.setStartDate(LocalDate.parse(newStartDateStr));
                }

                String newEndDateStr = InputUtils.readString(scanner, "Enter new end date (yyyy-mm-dd) (or press Enter to keep current): ");
                if (!newEndDateStr.isEmpty()) {
                    roomRate.setEndDate(LocalDate.parse(newEndDateStr));
                }

                roomRateEntitySessionBeanRemote.updateRoomRate(roomRate);
                System.out.println("Room Rate updated successfully.");
            } else {
                System.out.println("Room Rate not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the room rate: " + e.getMessage());
        }
    }


    // Delete room rate by ID
    private void deleteSpecialRate(Scanner scanner) {
        try {
            Long roomRateId = InputUtils.readLong(scanner, "Enter Room Rate ID to delete: ");

            RoomRate roomRate = roomRateEntitySessionBeanRemote.findRoomRate(roomRateId);

            if (roomRate != null) {
                // Confirm deletion
                String confirmation = InputUtils.readString(scanner, "Are you sure you want to delete this Room Rate? (y/n): ");

                if ("y".equalsIgnoreCase(confirmation)) {
                    roomRateEntitySessionBeanRemote.deleteRoomRate(roomRateId);
                    System.out.println("Room Rate deleted successfully.");
                } else {
                    System.out.println("Room Rate deletion canceled.");
                }
            } else {
                System.out.println("Room Rate not found.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the room rate: " + e.getMessage());
        }
    }
}
