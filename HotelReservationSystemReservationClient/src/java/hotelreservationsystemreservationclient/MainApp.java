/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import entity.Booking;
import entity.Account;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javafx.util.Pair;
import ejb.session.stateless.AccountEntitySessionBeanRemote;
import util.client.InputUtils;
import util.dto.RoomCount;

/**
 *
 * @author clara
 */
public class MainApp {
    private AccountEntitySessionBeanRemote accountEntitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    private AvailabilitySessionBeanRemote availabilitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(AccountEntitySessionBeanRemote guestEntitySessionBean, BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote, AvailabilitySessionBeanRemote availabilitySessionBeanRemote) {
        this.accountEntitySessionBeanRemote = guestEntitySessionBean;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
        this.availabilitySessionBeanRemote = availabilitySessionBeanRemote;
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- Hotel Reservation System ---");
            System.out.println("1. Guest Login");
            System.out.println("2. Register as Guest");
            System.out.println("3. Search Hotel Room");
            System.out.println("0. Exit");
            
            int choice = InputUtils.readInt(scanner, "Choose an option: ");

            switch (choice) {
                case 1:
                    guestLogIn(scanner);
                    break;
                case 2:
                    registerAsGuest(scanner);
                    break;
                case 3:
                    searchRooms(scanner, null);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    public void guestLogIn(Scanner scanner) {
        try {
            String username = InputUtils.readString(scanner, "Enter Username: ");
            String password = InputUtils.readString(scanner, "Enter Password: ");

            Account guest = accountEntitySessionBeanRemote.logInForGuest(username, password);

            if (guest != null) {
                guestMainMenu(scanner, guest);
            }
        } catch (Exception e) {
            System.out.println("Invalid Credentials.");
        }
    }
    
    public void registerAsGuest(Scanner scanner) {
        try {
            System.out.println("\n --- Registering As Guest ---");
        
            String username = InputUtils.readString(scanner, "Create Username: ");
            String email = InputUtils.readString(scanner, "Enter Email Address: ");
            String password = InputUtils.readString(scanner, "Create Password: ");

            Account newGuest = new Account(username, email, password);
            Account persistedGuest = accountEntitySessionBeanRemote.createAccount(newGuest);

            System.out.println("Guest Account Successfully Created with Id: " + persistedGuest.getId());
        } catch (Exception e) {
            System.out.println("Could not create account. Try again");
        }   
    }
    
    public void guestMainMenu(Scanner scanner, Account guest) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- Hotel Reservation Management ---");
            System.out.println("1. Search Hotel Rooms");
            System.out.println("2. View My Reservation Details");
            System.out.println("3. View All My Reservations");
            System.out.println("0. Log Out");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    searchRooms(scanner, guest);
                    break;
                case 2:
                    viewReservationDetails(scanner, guest);
                    break;
                case 3:
                    viewAllReservations(scanner, guest);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;    
            }
        }
    }
    
    public void searchRooms(Scanner scanner, Account guest) {
        try {
            System.out.println("\n--- Search Rooms ---");
            LocalDate startDate = InputUtils.readDate(scanner, "Enter Start Date (YYYY-MM-DD): ");
            LocalDate endDate = InputUtils.readDate(scanner, "Enter End Date (YYYY-MM-DD): ");
            
            // Show Available Room Types Between Start and End Date
            List<RoomCount> rooms = availabilitySessionBeanRemote.getAvailableRoomTypesWithCount(startDate, endDate);
            
            for (RoomCount room:rooms) {
                RoomType type = room.getRoomType();
                double rate = availabilitySessionBeanRemote.calculateReservationFee(type.getId(), startDate, endDate);
                String output = String.format("Room ID: %d, Room Type: %s, Reservation Fees: %.2f, Availability: %d", type.getId(), type.getName(), rate, room.getCount());
                System.out.println(output);
            }

            if (guest != null) {
                String confirmation = InputUtils.readString(scanner, "Proceed to book room? (y/n): ");

                if ("y".equalsIgnoreCase(confirmation)) {
                    createReservation(scanner, startDate, endDate, guest);
                    System.out.println("Reservation successfully made.");
                } else {
                    System.out.println("Reservation canceled.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while reserving room " + e.getMessage());
        }
    }
    
    public void createReservation(Scanner scanner, LocalDate startDate, LocalDate endDate, Account guest) {
        System.out.println("\n--- Create Reservation ---");
        
        try {
            Long roomTypeId = InputUtils.readLong(scanner, "Enter Room Type ID for Reservation: ");
            
            int numRooms = InputUtils.readInt(scanner, "Enter number of rooms (or 0 to cancel): ");
            if (numRooms == 0) {
                System.out.println("Reservation cancelled.");
                return;
            }
            if (numRooms < 0) {
                System.out.println("Invalid number of rooms. Reservation cancelled.");
                return;
            }
            
            List<Booking> persistedBookings = bookingEntitySessionBeanRemote.reserveRoomType(startDate, endDate, roomTypeId, numRooms, guest.getId());

            System.out.println("\nRoom successfully reserved. \nYour booking details: ");
            
            for (Booking booking : persistedBookings) {
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
            System.out.println("Failed to create new reservation");
        }
    }
    
    public void viewReservationDetails(Scanner scanner, Account guest) {
        System.out.println("\n--- View Reservation Details ---");
        
        Long bookingId = InputUtils.readLong(scanner, "Enter Reservation ID: ");
        
        Booking booking = bookingEntitySessionBeanRemote.getBookingById(bookingId);
        
        try {
            if (booking.getAccount().getId().equals(guest.getId())) {
                System.out.println("\n--- Booking Details Can Be Found Below ---");
                System.out.println("Booking Id: " + booking.getId());
                System.out.println("Start Date: " + booking.getStartDate());
                System.out.println("End Date: " + booking.getEndDate());
                System.out.println("Room Type: " + booking.getRoomType().getName());

                if (booking.getAllocatedRoom() != null) {
                    System.out.println("Allocated Room: " + booking.getAllocatedRoom().getRoomNumber());
                }
            } else {
                System.out.println("\nInvalid Booking Id");
            }
        } catch (Exception e) {
            System.out.println("\nInvalid Booking Id");
        }
        
    }
    
    public void viewAllReservations(Scanner scanner, Account guest) {
        System.out.println("\n--- View All Reservations ---");
        
        List<Booking> bookings = bookingEntitySessionBeanRemote.getBookingsByAccountId(guest.getId());
        
        if (bookings.size() > 0) {
            for (Booking booking:bookings) {
                System.out.println("\nBooking Id: " + booking.getId());
                System.out.println("Start Date: " + booking.getStartDate());
                System.out.println("End Date: " + booking.getEndDate());
                System.out.println("Room Type: " + booking.getRoomType().getName());

                if (booking.getAllocatedRoom() != null) {
                    System.out.println("Allocated Room: " + booking.getAllocatedRoom().getRoomNumber());
                }
            }    
        } else {
            System.out.println("No Reservation Placed");
        }
        
    }
}
