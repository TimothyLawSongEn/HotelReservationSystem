/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import entity.Booking;
import entity.Account;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import util.exception.BookingAlreadyCheckedInException;
import util.exception.BookingNoAllocatedRoomException;
import util.exception.EntityMissingException;
import ejb.session.stateless.AccountEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import util.client.InputUtils;
import util.dto.RoomCount;

/**
 *
 * @author timothy
 */
public class FrontOfficeModule {

    private AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    private AccountEntitySessionBeanRemote accountEntitySessionBeanRemote;
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;

    public FrontOfficeModule(AvailabilitySessionBeanRemote availabilitySessionBeanRemote,
                             BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote,
                             AccountEntitySessionBeanRemote guestEntitySessionBeanRemote,
                             RoomEntitySessionBeanRemote roomEntitySessionBeanRemote) {
        this.availabilitySessionBeanRemote = availabilitySessionBeanRemote;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
        this.accountEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
    }

    public void frontOfficeMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Guest Relationship Officer Menu ---");
            System.out.println("1: Walk-In Search Room");
            System.out.println("2: Check-In Guest");
            System.out.println("3: Check-Out Guest");
            System.out.println("4: Allocate bookings");
            System.out.println("5: Update room bookings at checkout time");
            System.out.println("0: Exit");

            int option = InputUtils.readInt(scanner, "> ");

            switch (option) {
                case 1:
                    walkInSearchRoom(scanner);
                    break;
                case 2:
                    checkIn(scanner);
                    break;
                case 3:
                    checkOut(scanner);
                    break;
                case 4:
                    allocateBookings(scanner);
                    break;
                case 5:
                    updateRoomBookingsAtCheckoutTime(scanner);
                    break;
                case 0:
                    System.out.println("Exiting Front Office.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    private void checkIn(Scanner scanner) {
        // get guest
        // display all active bookings that are not checkedin yet
        // allow user to select bookings to checkin (provide select all option)
        // call bookingBean.checkin() (get allocatedRoom field in booking, set room's currBooking field, set booking checkin boolean to true)

        long guestId = InputUtils.readLong(scanner, "Enter guestId (or 0 to cancel): ");
        Account guest = accountEntitySessionBeanRemote.findGuestById(guestId);

        // Fetch all active bookings for the guest that have not been checked in
        List<Booking> activeBookings = bookingEntitySessionBeanRemote.retrieveActiveBookingsForCheckIn(guestId);

        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings available for check-in.");
            return;
        }

        // Display bookings
        System.out.println("Select bookings to check in:");
        for (int i = 0; i < activeBookings.size(); i++) {
            System.out.println((i + 1) + ": " + activeBookings.get(i));
        }
        System.out.println((activeBookings.size() + 1) + ": Select All");

        // Get user selection
        int selection = InputUtils.readInt(scanner, "");
        List<Booking> selectedBookings;
        if (selection == activeBookings.size() + 1) { // Select all bookings
            selectedBookings = new ArrayList<>(activeBookings);
        } else {
            selectedBookings = Arrays.asList(activeBookings.get(selection - 1));
        }

        // Check-in the selected bookings
        for (Booking booking : selectedBookings) {
            try {
                bookingEntitySessionBeanRemote.checkIn(booking.getId());
//                System.out.println("\nChecked in booking: " + booking);
                System.out.println("\nBooking Id: " + booking.getId());
                System.out.println("Start Date: " + booking.getStartDate());
                System.out.println("End Date: " + booking.getEndDate());
                System.out.println("Room Type: " + booking.getRoomType().getName());
                System.out.println("Allocated Room: " + booking.getAllocatedRoom().getRoomNumber());
            } catch (EntityMissingException enfe) {
                System.out.println(enfe.getMessage());
            } catch (BookingAlreadyCheckedInException bacie) {
                System.out.println("Booking has already been checked in!");
                System.out.println("Allocated Room: " + booking.getAllocatedRoom().getRoomNumber());
            } catch (BookingNoAllocatedRoomException bnare) {
                System.out.println("No room allocated for this booking with id " + booking.getId() + ". Please resolve manually.");
            }
        }
    }

    
    private void checkOut(Scanner scanner) {
        // get guest
        // display all active bookings that are checkedin
        // allow user to select bookings to checkout (provide select all option)
        // call bookingBean.checkin() (get allocatedRoom field in booking, set room's currBooking field to null, set booking checkin boolean to false)

        long guestId = InputUtils.readLong(scanner, "Enter guestId (or 0 to cancel): ");
        Account guest = accountEntitySessionBeanRemote.findGuestById(guestId);

        // Fetch all checked-in bookings for the guest
        List<Booking> checkedInBookings = bookingEntitySessionBeanRemote.retrieveCheckedInBookings(guestId);

        if (checkedInBookings.isEmpty()) {
            System.out.println("No checked-in bookings available for check-out.");
            return;
        }

        // Display bookings
        System.out.println("Select bookings to check out:");
        for (int i = 0; i < checkedInBookings.size(); i++) {
            System.out.println((i + 1) + ": " + checkedInBookings.get(i));
        }
        System.out.println((checkedInBookings.size() + 1) + ": Select All");

        // Get user selection
        int selection = InputUtils.readInt(scanner, "");
        List<Booking> selectedBookings;
        if (selection == checkedInBookings.size() + 1) { // Select all bookings
            selectedBookings = new ArrayList<>(checkedInBookings);
        } else {
            selectedBookings = Arrays.asList(checkedInBookings.get(selection - 1));
        }

        // Check-out the selected bookings
        for (Booking booking : selectedBookings) {
            try {
                bookingEntitySessionBeanRemote.checkOut(booking.getId());
                System.out.println("Successfully checked out of room number: " + booking.getAllocatedRoom().getRoomNumber());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    
    private void allocateBookings(Scanner scanner) {
        LocalDate date = InputUtils.readDate(scanner, "Enter date (YYYY-MM-DD): ");

        List<Booking> allocatedBookings = bookingEntitySessionBeanRemote.allocateRoomToBookings(date);

        if (allocatedBookings.isEmpty()) {
            System.out.println("No bookings starts on " + date);
        } else {
            System.out.println("Bookings starting on " + date + ":");

            for (Booking booking : allocatedBookings) {
                System.out.println(booking);
            }
        }
    }
    
    private void updateRoomBookingsAtCheckoutTime(Scanner scanner) {
        LocalDate date = InputUtils.readDate(scanner, "Enter date (YYYY-MM-DD): ");
        roomEntitySessionBeanRemote.updateRoomBookingsAtCheckoutTime(date);
    }

    private void walkInSearchRoom(Scanner scanner) {
        while (true) {
            LocalDate startDate = InputUtils.readDate(scanner, "Enter check-in date (YYYY-MM-DD): ");
            LocalDate endDate = InputUtils.readDate(scanner, "Enter check-out date (YYYY-MM-DD): ");

            try {
                // Call the availability session bean to get available room types and their counts
                List<RoomCount> availableRoomTypes = availabilitySessionBeanRemote.getAvailableRoomTypesWithCount(startDate, endDate);

                if (availableRoomTypes.isEmpty()) {
                    System.out.println("No available rooms for the given dates.");
                    // Provide option to search again
                    System.out.println("1: Search again");
                    System.out.println("2: Exit");
                    int choice = InputUtils.readInt(scanner, "> ");

                    if (choice == 1) {
                        System.out.println("Searching again...");
                        continue; // Restart search process
                    } else if (choice == 2) {
                        System.out.println("Exiting...");
                        return;
                    } else {
                        System.out.println("Invalid option, please try again.");
                    }
                } else {
                    // Display the available room types and their counts
                    System.out.println("Available room types:");
                    for (int i = 0; i < availableRoomTypes.size(); i++) {
                        RoomCount roomTypePair = availableRoomTypes.get(i);
                        RoomType roomType = roomTypePair.getRoomType();
                        int availableCount = roomTypePair.getCount();

                        System.out.printf("%d: %s (Available: %d) $%.2f\n", i + 1, roomType.getName(), availableCount, roomType.calculateTotalWalkinFee(startDate, endDate));
                    }
                    System.out.println();

                    // Provide option to reserve or search again
                    System.out.println("1: Reserve a room");
                    System.out.println("2: Search again");
                    System.out.println("3: Exit");
                    int choice = InputUtils.readInt(scanner, "> ");

                    if (choice == 1) {
                        // Reserve room option
                        reserveRoomType(availableRoomTypes, startDate, endDate, scanner);
                        break; // Exit loop after reservation attempt
                    } else if (choice == 2) {
                        System.out.println("Searching again...");
                        continue; // Restart search process
                    } else if (choice == 3) {
                        System.out.println("Exiting...");
                        return;
                    } else {
                        System.out.println("Invalid option, please try again.");
                    }
                }
            } catch (Exception e) {
                System.out.println("An error occurred while searching for rooms: " + e.getMessage());
            }
        }
    }

    private void reserveRoomType(List<RoomCount> availableRoomTypes, LocalDate startDate, LocalDate endDate, Scanner scanner) {
        try {
            int roomTypeChoice;
            while (true) {
                roomTypeChoice = InputUtils.readInt(scanner, "Enter room type to reserve (Enter the number associated with that roomtype, or 0 to cancel): ");
                if (roomTypeChoice == 0) {
                    System.out.println("Reservation cancelled.");
                    return;
                }

                if (roomTypeChoice < 0 || roomTypeChoice > availableRoomTypes.size()) {
                    System.out.println("Room type does not exist. Try again.");
                } else {
                    break;
                }
            }
            
            RoomCount selectedRoomTypePair = availableRoomTypes.get(roomTypeChoice - 1);
            int availableCount = selectedRoomTypePair.getCount();
            int numRooms = InputUtils.readInt(scanner, "Enter number of rooms [Available: " + availableCount + "] (or 0 to cancel): ");
            if (numRooms == 0) {
                System.out.println("Reservation cancelled.");
                return;
            }
            if (numRooms < 0) {
                System.out.println("Invalid number of rooms. Reservation cancelled.");
                return;
            }
            if (numRooms > availableCount) {
                System.out.println("Exceeded room availability. Reservation cancelled.");
                return;
            }
            
            long guestId;
            while (true) {
                String strInput = InputUtils.readString(scanner, "Is guest a registered user? (y/n) ");
                
                if ("n".equals(strInput)) {
                    guestId = registerGuest(scanner);
                    break;
                } else if ("y".equals(strInput)) {
                    guestId = InputUtils.readLong(scanner, "Enter guestId (or 0 to cancel): ");
                    if (guestId == 0) {
                        System.out.println("Reservation cancelled.");
                        return;
                    }
                    break;
                } else {
                    System.out.println("Invalid input. Try again.");
                }
            }
            
            Long selectedRoomTypeId = selectedRoomTypePair.getRoomType().getId();
            List<Booking> bookings = bookingEntitySessionBeanRemote.reserveRoomType(startDate, endDate, selectedRoomTypeId, numRooms, guestId);
            System.out.println("\nRoom successfully reserved. \nYour booking details: ");
            
            for (Booking booking : bookings) {
                System.out.println("\nBooking Id: " + booking.getId());
                System.out.println("Start Date: " + booking.getStartDate());
                System.out.println("End Date: " + booking.getEndDate());
                System.out.println("Room Type: " + booking.getRoomType().getName());
            
                if (booking.getAllocatedRoom() != null) {
                    System.out.println("Allocated Room: " + booking.getAllocatedRoom().getRoomNumber());
                } else {
                    System.out.println("Room not allocated yet.");
                }
            }


        } catch (Exception e) {
            System.out.println("Failed to reserve room: " + e.getMessage());
        }
    }
    
    private long registerGuest(Scanner scanner) {
        try {
            System.out.println("\n --- Registering Guest ---");
        
            String username = InputUtils.readString(scanner, "Create Username: ");
            String email = InputUtils.readString(scanner, "Enter Email Address: ");
            String password = InputUtils.readString(scanner, "Create Password: ");

            Account newGuest = new Account(username, email, password);
            Account persistedGuest = accountEntitySessionBeanRemote.createAccount(newGuest);

            System.out.println("Guest Account Successfully Created with Id: " + persistedGuest.getId());
            return persistedGuest.getId();
        } catch (Exception e) {
            System.out.println("Could not create account. Try again");
            throw e;
        }
    }
}
