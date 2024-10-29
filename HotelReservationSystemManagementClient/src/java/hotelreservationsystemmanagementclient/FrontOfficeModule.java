/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import entity.Booking;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author timothy
 */
public class FrontOfficeModule {

    private AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;

    public FrontOfficeModule(AvailabilitySessionBeanRemote availabilitySessionBeanRemote,
                             BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote) {
        this.availabilitySessionBeanRemote = availabilitySessionBeanRemote;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
    }

    public void frontOfficeMenu(Scanner scanner) {
        while (true) {
            System.out.println("*** Front Office Menu ***");
            System.out.println("1: Walk-In Search Room");
            System.out.println("2: Check-In Guest");
            System.out.println("3: Check-Out Guest");
            System.out.println("4: Allocate Bookings");
            System.out.println("5: Exit");
            System.out.print("> ");

            int option = scanner.nextInt();

            scanner.nextLine(); // Clear buffer after nextInt()

            switch (option) {
                case 1:
                    walkInSearchRoom(scanner);
                    break;
                case 2:
                    // Placeholder for check-in functionality
                    System.out.println("Check-In functionality is not yet implemented.");
                    break;
                case 3:
                    // Placeholder for check-out functionality
                    System.out.println("Check-Out functionality is not yet implemented.");
                    break;
                case 4:
                    allocateBookings(scanner);
                    return;
                case 5:
                    System.out.println("Exiting Front Office.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    private void allocateBookings(Scanner scanner) {
        System.out.print("Enter date (YYYY-MM-DD): ");
        LocalDate date = LocalDate.parse(scanner.next());

        List<Booking> allocatedBookings = bookingEntitySessionBeanRemote.allocateRoomToBookings(date);

        if (allocatedBookings.isEmpty()) {
            System.out.println("No bookings were allocated for date " + date);
        } else {
            System.out.println("Bookings allocated for date " + date + ":");

            for (Booking booking : allocatedBookings) {
                System.out.println(booking);
            }
        }
    }

    private void walkInSearchRoom(Scanner scanner) {
        while (true) {
            System.out.print("Enter check-in date (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.next());

            System.out.print("Enter check-out date (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.next());

            try {
                // Call the availability session bean to get available room types and their counts
                List<Pair<RoomType, Integer>> availableRoomTypes = availabilitySessionBeanRemote.getAvailableRoomTypesWithCount(startDate, endDate);

                if (availableRoomTypes.isEmpty()) {
                    System.out.println("No available rooms for the given dates.");
                } else {
                    // Display the available room types and their counts
                    System.out.println("Available room types:");
                    for (int i = 0; i < availableRoomTypes.size(); i++) {
                        Pair<RoomType, Integer> roomTypePair = availableRoomTypes.get(i);
                        RoomType roomType = roomTypePair.getKey();
                        int availableCount = roomTypePair.getValue();

                        System.out.printf("%d: %s (Available: %d)\n", i + 1, roomType.getName(), availableCount);
                    }

                    // Provide option to reserve or search again
                    System.out.println("1: Reserve a room");
                    System.out.println("2: Search again");
                    System.out.print("> ");
                    int choice = scanner.nextInt();

                    if (choice == 1) {
                        // Reserve room option
                        reserveRoomType(availableRoomTypes, startDate, endDate, scanner);
                        break; // Exit loop after reservation attempt
                    } else if (choice == 2) {
                        System.out.println("Searching again...");
                        continue; // Restart search process
                    } else {
                        System.out.println("Invalid option, please try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("An error occurred while searching for rooms: " + e.getMessage());
            }
        }
    }

    private void reserveRoomType(List<Pair<RoomType, Integer>> availableRoomTypes, LocalDate startDate, LocalDate endDate, Scanner scanner) {
        try {
            while (true) {
                System.out.print("Enter the number of the room type to reserve (or 0 to cancel): ");
                int roomTypeChoice = scanner.nextInt();

                if (roomTypeChoice == 0) {
                    System.out.println("Reservation cancelled.");
                    return;
                }

                if (roomTypeChoice < 0 || roomTypeChoice > availableRoomTypes.size()) {
                    System.out.println("Room type does not exist. Try again.");
                    continue;
                }

                Pair<RoomType, Integer> selectedRoomTypePair = availableRoomTypes.get(roomTypeChoice - 1);
                Long selectedRoomTypeId = selectedRoomTypePair.getKey().getId();
                Booking booking = bookingEntitySessionBeanRemote.reserveRoomType(startDate, endDate, selectedRoomTypeId);
                System.out.println("Room successfully reserved. Your booking details: " + booking);
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed to reserve room: " + e.getMessage());
        }
    }
}
