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
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

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
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Account guest = accountEntitySessionBeanRemote.logIn(username, password);

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
        
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine();

            System.out.print("Create Password: ");
            String password = scanner.nextLine();

            Account newGuest = new Account(username, email, password);
            Account persistedGuest = accountEntitySessionBeanRemote.createAccount(newGuest);

            System.out.println("Guest Account Successfully Created: " + persistedGuest);
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
            System.out.print("Enter Booking Start Date (YYYY-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter Booking End Date (YYYY-MM-DD): ");
            LocalDate endDate = LocalDate.parse(scanner.nextLine()); 
            
            // Show Available Room Types Between Start and End Date
            List<RoomCount> rooms = availabilitySessionBeanRemote.getAvailableRoomTypesWithCount(startDate, endDate);
            
            for (RoomCount room:rooms) {
                RoomType type = room.getRoomType();
                double rate = availabilitySessionBeanRemote.calculateReservationFee(type.getId(), startDate, endDate);
                System.out.println(rate);
                String output = String.format("Room Type: %s, Reservation Fees: %.2f, Availability: %d", type.getName(), rate, room.getCount());
                System.out.println(output);
            }

            if (guest != null) {
                System.out.println("Proceed to book room?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    createReservation(scanner, startDate, endDate, guest);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void createReservation(Scanner scanner, LocalDate startDate, LocalDate endDate, Account guest) {
        System.out.println("\n--- Create Reservation ---");
        
        try {
            System.out.print("Enter Room Type ID for Reservation: ");
            Long roomTypeId = Long.parseLong(scanner.nextLine());
            
            Booking persistedBooking = bookingEntitySessionBeanRemote.reserveRoomType(startDate, endDate, roomTypeId, guest.getId());

            System.out.println("Reservation successfully created " + persistedBooking);
            
        } catch (Exception e) {
            System.out.println("Failed to create new reservation");
        }
    }
    
    public void viewReservationDetails(Scanner scanner, Account guest) {
        System.out.println("\n--- View Reservation Details ---");
        
        System.out.print("Enter Reservation ID: ");
        Long bookingId = Long.parseLong(scanner.nextLine());
        
        Booking booking = bookingEntitySessionBeanRemote.getBookingById(bookingId);
        
        System.out.println("\n--- Booking Details Can Be Found Below ---");
        System.out.println(booking);
    }
    
    public void viewAllReservations(Scanner scanner, Account guest) {
        System.out.println("\n--- View All Reservations ---");
        
        List<Booking> bookings = bookingEntitySessionBeanRemote.getBookingsByAccountId(guest.getId());
        
        for (Booking booking:bookings) {
            System.out.println(booking);
        }
    }
}
