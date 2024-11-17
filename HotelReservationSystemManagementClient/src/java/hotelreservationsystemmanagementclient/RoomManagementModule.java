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
import javafx.util.Pair;
import javax.ejb.EJBException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import util.client.InputUtils;

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
//            System.out.println("2. View Room");
            System.out.println("2. Create Room");
            System.out.println("3. Update Room");
            System.out.println("4. Delete Room");
            System.out.println("5. Generate Room Allocation Exception Report");
            System.out.println("0. Back to Main Menu");
            int choice = InputUtils.readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    viewAllRooms();
                    break;
//                case 2:
//                    viewRoom(scanner);
//                    break;
                case 2:
                    createRoom(scanner);
                    break;
                case 3:
                    updateRoom(scanner);
                    break;
                case 4:
                    deleteRoom(scanner);
                    break;
                case 5:
                    generateRoomAllocationExceptionReport(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
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
            Long roomId = InputUtils.readLong(scanner, "Enter Room ID to view details: "); // todo: chg to enter roomnum instead of id
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
            String roomNumber = InputUtils.readString(scanner, "Enter Room Number: ");

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

            int roomTypeChoice = InputUtils.readInt(scanner, "Select room type by number: ");
            RoomType selectedRoomType = nonDisabledRoomTypes.get(roomTypeChoice - 1);
            
            boolean available;
            while (true) {
                String strInput = InputUtils.readString(scanner, "Enter room status ('y' for available, 'n' for not): ");
                if ("y".equals(strInput)) {
                    available = true;
                    break;
                } else if ("n".equals(strInput)) {
                    available = false;
                    break;
                } else {
                    System.out.println("Invalid input. Try again.");
                }
            }
            
            // Create Room
            Room newRoom = new Room(roomNumber, selectedRoomType, available);

            // Try to create the room
            Room createdRoom = roomEntitySessionBeanRemote.createRoom(newRoom);
            System.out.println("Room created successfully: " + createdRoom.getRoomNumber() + " of type " + createdRoom.getRoomType().getName());

        } catch (EJBException e) {
            // Check if the cause of the EJBException is a ConstraintViolationException
            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
                handleValidationException(cve);
            } else {
                // Handle other types of EJB exceptions
                System.out.println("An unexpected error occurred while creating the room: " + e.getMessage());
            }
        } catch (Exception e) {
            // Catch any other unexpected errors
            System.out.println("An error occurred while creating the room: " + e.getMessage());
        }
    }


    private void updateRoom(Scanner scanner) {
        try {
            Long roomId = InputUtils.readLong(scanner, "Enter Room ID to update: ");

            Room roomToUpdate = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (roomToUpdate != null) {
                System.out.println("\n--- Room Details ---");
                System.out.printf("Room ID: %d\nRoom Number: %s\nRoom Type: %s\n", roomToUpdate.getId(), roomToUpdate.getRoomNumber(), roomToUpdate.getRoomType().getName());
                
                // Update room number
                String newRoomNumber = InputUtils.readString(scanner, "Enter new Room Number (current: " + roomToUpdate.getRoomNumber() + ") or press Enter to skip: ");
                if (!newRoomNumber.isEmpty()) { // Keep the old value if the user skips
                    roomToUpdate.setRoomNumber(newRoomNumber);
                }

                // Update room type (choose from available room types or skip)
                System.out.println("Choose a new Room Type or press Enter to skip:");
                List<RoomType> nonDisabledRoomTypes = roomTypeEntitySessionBeanRemote.findAllNonDisabledRoomTypes();
                for (int i = 0; i < nonDisabledRoomTypes.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, nonDisabledRoomTypes.get(i).getName());
                }
                String roomTypeInput = InputUtils.readString(scanner, "Select room type by number (current: " + roomToUpdate.getRoomType().getName() + ") or press Enter to skip: ");

                RoomType selectedRoomType;
                if (!roomTypeInput.isEmpty()) { // Keep the old value if the user skips
                    int roomTypeChoice = Integer.parseInt(roomTypeInput);
                    selectedRoomType = nonDisabledRoomTypes.get(roomTypeChoice - 1);
                    roomToUpdate.setRoomType(selectedRoomType);
                }
                
                String strInput = InputUtils.readString(scanner, "Change room type to " + (roomToUpdate.getAvailable() ? "UNavailable" : "Available") + "? (enter 'y' to change or press Enter to skip): ");
                if ("y".equals(strInput)) {
                    roomToUpdate.setAvailable(!roomToUpdate.getAvailable());
                }

                // Apply changes
                roomEntitySessionBeanRemote.updateRoom(roomToUpdate);
                System.out.println("Room updated successfully.");
            } else {
                System.out.println("Room not found with ID " + roomId);
            }
        } catch (EJBException e) {
            // Check if the cause of the EJBException is a ConstraintViolationException
            if (e.getCause() != null && e.getCause().getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) e.getCause().getCause();
                handleValidationException(cve);
            } else {
                // Handle other types of EJB exceptions
                System.out.println("An unexpected error occurred while updating the room: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating the room: " + e.getMessage());
        }
    }

    private void deleteRoom(Scanner scanner) {
        try {
            Long roomId = InputUtils.readLong(scanner, "Enter Room ID to delete: ");

            // Check if the room exists
            Room room = roomEntitySessionBeanRemote.findRoomById(roomId);
            if (room != null) {
                // Confirm deletion
//                System.out.println("Room Details: " + room);
                System.out.println("\n--- Room Details ---");
                System.out.printf("Room ID: %d\nRoom Number: %s\nRoom Type: %s\n", room.getId(), room.getRoomNumber(), room.getRoomType().getName());
                String confirmation = InputUtils.readString(scanner, "Are you sure you want to delete this Room? (y/n): ");

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
//            System.out.print("Enter date (YYYY-MM-DD): ");
            LocalDate date = InputUtils.readDate(scanner, "Enter date (YYYY-MM-DD): ");
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
    
    
    private void handleValidationException(ConstraintViolationException e) {
        StringBuilder errorMessage = new StringBuilder("Validation failed:\n");

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errorMessage.append(" - ")
                        .append(violation.getPropertyPath())
                        .append(" ")
                        .append(violation.getMessage())
                        .append(" (invalid value: ")
                        .append(violation.getInvalidValue())
                        .append(")\n");
        }

        System.out.println(errorMessage.toString());
    }
}
