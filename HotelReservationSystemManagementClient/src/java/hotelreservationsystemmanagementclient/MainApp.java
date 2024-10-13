/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Room;
import entity.RoomRate;
import entity.RoomType;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author timothy
 */
public class MainApp {
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateTransactionEntitySessionBeanRemote) {
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateTransactionEntitySessionBeanRemote;
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("Main menu:");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Room Types");
            System.out.println("3. Manage Room Rates");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageRooms(scanner);
                    break;
                case 2:
                    manageRoomTypes(scanner);
                    break;
                case 3:
                    manageRoomRates(scanner);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Room Management Menu
    private void manageRooms(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Room Management ---");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Room");
            System.out.println("3. Create Room");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
                case 2:
                    viewRoomById(scanner);
                    break;
                case 3:
                    createRoom(scanner);
                    break;
                case 4:
                    updateRoom(scanner);
                    break;
                case 5:
                    deleteRoom(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // RoomType Management Menu
    private void manageRoomTypes(Scanner scanner) {
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
                    viewRoomTypeById(scanner);
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

    // RoomRate Management Menu
    private void manageRoomRates(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Room Rate Management ---");
            System.out.println("1. View All Rates");
            System.out.println("2. View RoomType Rates");
            System.out.println("3. Create Special Rate (Promo/Peak)");
            System.out.println("4. Update Rate");
            System.out.println("5. Delete Special Rate (Promo/Peak)");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllRoomRates();
                    break;
                case 2:
                    viewRoomRateById(scanner);
                    break;
                case 3:
                    createRoomRate(scanner);
                    break;
                case 4:
                    updateRoomRate(scanner);
                    break;
                case 5:
                    deleteRoomRate(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Room methods
    public void viewAllRooms() {
        try {
            List<Room> rooms = roomEntitySessionBeanRemote.viewAllRooms();
            if (rooms.isEmpty()) {
                System.out.println("No rooms available.");
            } else {
                System.out.println("\n--- List of Rooms ---");
                System.out.printf("%-10s %-20s %-20s\n", "ID", "Room Number", "Room Type");
                System.out.println("-----------------------------------------------");
                for (Room room : rooms) {
                    System.out.printf("%-10d %-20s %-20s\n", room.getId(), room.getRoomNumber(), room.getRoomType().getName());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching rooms: " + e.getMessage());
        }
    }

    // Method to view a room by ID
    public void viewRoomById(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to view details: ");
            Long roomId = scanner.nextLong();
            Room room = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (room != null) {
                System.out.println("\n--- Room Details ---");
                System.out.printf("Room ID: %d\nRoom Number: %s\nRoom Type: %s\n", room.getId(), room.getRoomNumber(), room.getRoomType().getName());
            } else {
                System.out.println("Room not found with ID " + roomId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching room details: " + e.getMessage());
        }
    }

    // Method to create a new room
    public void createRoom(Scanner scanner) {
        try {
            // Get room number
            scanner.nextLine();
            System.out.print("Enter Room Number: ");
            String roomNumber = scanner.nextLine().trim();
            
            // Get room type (choose from available room types)
            System.out.println("Choose a Room Type:");
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes();
            for (int i = 0; i < roomTypes.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, roomTypes.get(i).getName());
            }
            System.out.print("Select room type by number: ");
            int roomTypeChoice = scanner.nextInt();
            RoomType selectedRoomType = roomTypes.get(roomTypeChoice - 1);

            // Create Room
            Room newRoom = new Room(roomNumber, selectedRoomType);

            Room createdRoom = roomEntitySessionBeanRemote.createRoom(newRoom);
            System.out.println("Room created successfully: " + createdRoom.getRoomNumber() + " of type " + createdRoom.getRoomType().getName());
        } catch (Exception e) {
            System.out.println("An error occurred while creating the room: " + e.getMessage());
        }
    }

    // Method to update a room with the option to skip fields
    public void updateRoom(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to update: ");
            Long roomId = scanner.nextLong();
            scanner.nextLine(); // Consume newline

            Room roomToUpdate = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (roomToUpdate != null) {
                // Update room number
                System.out.print("Enter new Room Number (current: " + roomToUpdate.getRoomNumber() + ") or press Enter to skip: ");
                String newRoomNumber = scanner.nextLine().trim();
                if (newRoomNumber.isEmpty()) {
                    newRoomNumber = roomToUpdate.getRoomNumber(); // Keep the old value if the user skips
                }

                // Update room type (choose from available room types or skip)
                System.out.println("Choose a new Room Type or press Enter to skip:");
                List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes();
                for (int i = 0; i < roomTypes.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, roomTypes.get(i).getName());
                }
                System.out.print("Select room type by number (current: " + roomToUpdate.getRoomType().getName() + ") or press Enter to skip: ");
                String roomTypeInput = scanner.nextLine().trim();

                RoomType selectedRoomType;
                if (roomTypeInput.isEmpty()) {
                    selectedRoomType = roomToUpdate.getRoomType(); // Keep the old value if the user skips
                } else {
                    int roomTypeChoice = Integer.parseInt(roomTypeInput);
                    selectedRoomType = roomTypes.get(roomTypeChoice - 1);
                }

                // Apply changes
                roomEntitySessionBeanRemote.updateRoom(roomId, newRoomNumber, selectedRoomType);
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room not found with ID " + roomId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the room: " + e.getMessage());
        }
    }


    // Method to delete a room
    public void deleteRoom(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to delete: ");
            Long roomId = scanner.nextLong();

            // Check if the room exists
            Room room = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (room != null) {
                // Confirm deletion
                System.out.print("Are you sure you want to delete this Room? (y/n): ");
                String confirmation = scanner.next();

                if ("y".equalsIgnoreCase(confirmation)) {
                    roomEntitySessionBeanRemote.deleteRoom(roomId);
                    System.out.println("Room deleted successfully.");
                } else {
                    System.out.println("Room deletion canceled.");
                }
            } else {
                System.out.println("Room not found with ID " + roomId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the room: " + e.getMessage());
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
                System.out.printf("%-10s %-20s %-20s %-20s\n", "ID", "Room Type", "Normal Rate", "Published Rate");
                System.out.println("--------------------------------------------------------------------------");
                for (RoomType roomType : roomTypes) {
                    System.out.printf("%-10d %-20s %-20.2f %-20.2f\n", 
                                      roomType.getId(), 
                                      roomType.getName(), 
                                      roomType.getNormalRate(), 
                                      roomType.getPublishedRate());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching room types: " + e.getMessage());
        }
    }



    private void viewRoomTypeById(Scanner scanner) {
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
            scanner.nextLine(); // Consume newline left-over

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
                roomTypeEntitySessionBeanRemote.deleteRoomType(roomTypeId);
                System.out.println("Room Type deleted successfully.");
            } else {
                System.out.println("Room Type deletion canceled.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting room type: " + e.getMessage());
        }
    }


    // View all Room Rates categorised by RoomType
    private void viewAllRoomRates() {
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
    private void viewRoomRateById(Scanner scanner) {
        try {
            System.out.print("Enter Room Rate ID: ");
            Long roomRateId = scanner.nextLong();
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
    private void createRoomRate(Scanner scanner) {
        try {
            // Get room type
            List<RoomType> roomTypes = roomTypeEntitySessionBeanRemote.findAllRoomTypes();
            System.out.println("Choose Room Type:");
            for (int i = 0; i < roomTypes.size(); i++) {
                System.out.printf("%d: %s\n", i + 1, roomTypes.get(i).getName());
            }
            int roomTypeIndex = scanner.nextInt() - 1;
            RoomType selectedRoomType = roomTypes.get(roomTypeIndex);

            // Get rate type
            RoomRate.SpecialRateType rateType = chooseRateType(scanner);
            if (rateType == null) {
                System.out.println("Rate type selection failed.");
                return;
            }
            
            scanner.nextLine();
            System.out.print("Enter name: ");
            String newRateName = scanner.nextLine().trim();
            
            // Get amount
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            
            scanner.nextLine();

            // Get period (start date and end date)
            System.out.print("Enter start date (yyyy-mm-dd): ");
            String startDateStr = scanner.next();
            LocalDate startDate = LocalDate.parse(startDateStr);

            System.out.print("Enter end date (yyyy-mm-dd): ");
            String endDateStr = scanner.next();
            LocalDate endDate = LocalDate.parse(endDateStr);

            // Create room rate object and persist
            RoomRate newRoomRate = new RoomRate(newRateName, amount, startDate, endDate, rateType, selectedRoomType);
            roomRateEntitySessionBeanRemote.createRoomRate(newRoomRate);

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

        System.out.print("Please select a rate type (1-" + rateTypes.length + "): ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > rateTypes.length) {
            System.out.println("Invalid choice, please select a valid rate type.");
            return null;  // Return null if invalid input
        }

        // Return the selected rate type based on user input
        return rateTypes[choice - 1];
    }



    // Update a room rate
    private void updateRoomRate(Scanner scanner) {
        try {
            System.out.print("Enter Room Rate ID to update: ");
            Long roomRateId = scanner.nextLong();
            RoomRate roomRate = roomRateEntitySessionBeanRemote.findRoomRate(roomRateId);

            if (roomRate != null) {
                scanner.nextLine();
                System.out.print("Enter new amount (or press Enter to keep current): ");
                String newAmountStr = scanner.nextLine().trim();
                if (!newAmountStr.isEmpty()) {
                    roomRate.setAmount(Double.parseDouble(newAmountStr));
                }

                System.out.print("Enter new start date (yyyy-mm-dd) (or press Enter to keep current): ");
                String newStartDateStr = scanner.nextLine().trim();
                if (!newStartDateStr.isEmpty()) {
                    roomRate.setStartDate(LocalDate.parse(newStartDateStr));
                }

                System.out.print("Enter new end date (yyyy-mm-dd) (or press Enter to keep current): ");
                String newEndDateStr = scanner.nextLine().trim();
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
    private void deleteRoomRate(Scanner scanner) {
        try {
            System.out.print("Enter Room Rate ID to delete: ");
            Long roomRateId = scanner.nextLong();

            RoomRate roomRate = roomRateEntitySessionBeanRemote.findRoomRate(roomRateId);

            if (roomRate != null) {
                // Confirm deletion
                System.out.print("Are you sure you want to delete this Room Rate? (y/n): ");
                String confirmation = scanner.next();

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
