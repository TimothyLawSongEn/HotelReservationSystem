/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Booking;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javafx.util.Pair;

/**
 *
 * @author timothy
 */
public class RoomManagementModule {
    
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    
    public RoomManagementModule(RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote) {
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
    }

    // Room Management Menu
    void manageRooms(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Room Management ---");
            System.out.println("1. View All Rooms");
            System.out.println("2. View Room");
            System.out.println("3. Create Room");
            System.out.println("4. Update Room");
            System.out.println("5. Delete Room");
            System.out.println("6. Generate Room Allocation Exception Report");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
                case 2:
                    viewRoom(scanner);
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
                case 6:
                    generateRoomAllocationExceptionReport(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
        // Room methods
    private void viewAllRooms() {
        try {
            List<Room> rooms = roomEntitySessionBeanRemote.findAllRooms();
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

    private void viewRoom(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to view details: "); // todo: chg to enter roomnum instead of id
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

    private void createRoom(Scanner scanner) {
        try {
            // Get room number
            scanner.nextLine();
            System.out.print("Enter Room Number: ");
            String roomNumber = scanner.nextLine().trim();

            // Get non-disabled room types
            System.out.println("Choose a Room Type:");
            List<RoomType> nonDisabledRoomTypes = roomTypeEntitySessionBeanRemote.findAllNonDisabledRoomTypes();

            if (nonDisabledRoomTypes.isEmpty()) {
                System.out.println("No available room types.");
                return;
            }

            // Display available room types
            for (int i = 0; i < nonDisabledRoomTypes.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, nonDisabledRoomTypes.get(i).getName());
            }

            System.out.print("Select room type by number: ");
            int roomTypeChoice = scanner.nextInt();
            RoomType selectedRoomType = nonDisabledRoomTypes.get(roomTypeChoice - 1);

            // Create Room
            Room newRoom = new Room(roomNumber, selectedRoomType);

            Room createdRoom = roomEntitySessionBeanRemote.createRoom(newRoom);
            System.out.println("Room created successfully: " + createdRoom.getRoomNumber() + " of type " + createdRoom.getRoomType().getName());
        } catch (Exception e) {
            System.out.println("An error occurred while creating the room: " + e.getMessage());
        }
    }

    private void updateRoom(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to update: ");
            Long roomId = scanner.nextLong();
            
            scanner.nextLine();
            Room roomToUpdate = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (roomToUpdate != null) {
                // Update room number
                System.out.print("Enter new Room Number (current: " + roomToUpdate.getRoomNumber() + ") or press Enter to skip: ");
                String newRoomNumber = scanner.nextLine().trim();
                if (!newRoomNumber.isEmpty()) { // Keep the old value if the user skips
                    roomToUpdate.setRoomNumber(newRoomNumber);
                }

                // Update room type (choose from available room types or skip)
                System.out.println("Choose a new Room Type or press Enter to skip:");
                List<RoomType> nonDisabledRoomTypes = roomTypeEntitySessionBeanRemote.findAllNonDisabledRoomTypes();
                for (int i = 0; i < nonDisabledRoomTypes.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, nonDisabledRoomTypes.get(i).getName());
                }
                System.out.print("Select room type by number (current: " + roomToUpdate.getRoomType().getName() + ") or press Enter to skip: ");
                String roomTypeInput = scanner.nextLine().trim();

                RoomType selectedRoomType;
                if (!roomTypeInput.isEmpty()) { // Keep the old value if the user skips
                    int roomTypeChoice = Integer.parseInt(roomTypeInput);
                    selectedRoomType = nonDisabledRoomTypes.get(roomTypeChoice - 1);
                    roomToUpdate.setRoomType(selectedRoomType);
                }

                // Apply changes
                roomEntitySessionBeanRemote.updateRoom(roomToUpdate);
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room not found with ID " + roomId);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the room: " + e.getMessage());
        }
    }

    private void deleteRoom(Scanner scanner) {
        try {
            System.out.print("Enter Room ID to delete: ");
            Long roomId = scanner.nextLong();

            // Check if the room exists
            Room room = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (room != null) {
                // Confirm deletion
                System.out.println("Room Details: " + room);
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
    
    private void generateRoomAllocationExceptionReport(Scanner scanner) {
        try {
            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(scanner.next());
            // Generate the report
            Pair<List<Booking>, List<Booking>> report = bookingEntitySessionBeanRemote.getBookingsWithRoomAllocException(date);
            List<Booking> bookingsWithoutRoom = report.getKey();
            List<Booking> bookingsWithHigherRoom = report.getValue();

            // Print out the report details
            System.out.println("Room Allocation Exception Report");
            System.out.println("--------------------------------");
            System.out.println("Bookings Without Room (No Upgrade Available):");
            if (bookingsWithoutRoom.isEmpty()) {
                System.out.println("  - None");
            } else {
                for (Booking booking : bookingsWithoutRoom) {
                    System.out.println("  - Booking ID: " + booking.getId() + ", Reserved Room Type: " + booking.getRoomType());
                }
            }

            System.out.println("\nBookings With Higher Room (Upgrade Available):");
            if (bookingsWithHigherRoom.isEmpty()) {
                System.out.println("  - None");
            } else {
                for (Booking booking : bookingsWithHigherRoom) {
                    System.out.println("  - Booking ID: " + booking.getId() + ", Reserved Room Type: " + booking.getRoomType() + ", Allocated Room Type: " + booking.getAllocatedRoom().getRoomType());
                }
            }

        } catch (Exception e) {
            System.err.println("An error occurred while generating the report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
