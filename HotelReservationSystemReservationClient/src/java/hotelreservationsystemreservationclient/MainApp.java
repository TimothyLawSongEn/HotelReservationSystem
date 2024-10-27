/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Booking;
import entity.Guest;
import entity.RoomType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author clara
 */
public class MainApp {
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;

    public MainApp() {
    }

    public MainApp(RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBean, BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote) {
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.guestEntitySessionBeanRemote = guestEntitySessionBean;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
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
            }
        }
    }
    
    public void guestLogIn(Scanner scanner) {
        try {
            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Guest guest = guestEntitySessionBeanRemote.logIn(email, password);

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
        
            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine();

            System.out.print("Create Password: ");
            String password = scanner.nextLine();

            Guest newGuest = new Guest(email, password);
            Guest persistedGuest = guestEntitySessionBeanRemote.createGuest(newGuest);

            System.out.println("Guest Account Successfully Created: " + persistedGuest);
        } catch (Exception e) {
            System.out.println("Could not create account. Try again");
        }   
    }
    
    public void guestMainMenu(Scanner scanner, Guest guest) {
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
                    
            }
        }
    }
    
    public void searchRooms(Scanner scanner, Guest guest) {
        System.out.println("\n--- Search Rooms ---");
        System.out.print("Enter Booking Start Date (dd/MM/yyyy): ");
        LocalDate startDate = dateFormatter(scanner.nextLine());
        
        System.out.print("Enter Booking End Date (dd/MM/yyyy): ");
        LocalDate endDate = dateFormatter(scanner.nextLine());
        
        //TODO: Show Available Room Types Between Start and End Date
        
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
        
    }
    
    public void createReservation(Scanner scanner, LocalDate startDate, LocalDate endDate, Guest guest) {
        System.out.println("\n--- Create Reservation ---");
        
        System.out.print("Enter Room Type ID for Reservation: ");
        Long roomTypeId = Long.parseLong(scanner.nextLine());
        
        try {
            RoomType roomType = roomTypeEntitySessionBeanRemote.findRoomType(roomTypeId);
            Booking newBooking = new Booking(startDate, endDate, roomType, guest);

            Booking persistedBooking = bookingEntitySessionBeanRemote.createBooking(newBooking);

            System.out.println("Reservation successfully created " + persistedBooking);
            
            //TODO: Add room assignment logic for same date bookings made after 2am
            
        } catch (Exception e) {
            System.out.println("Failed to create new reservation");
        }
    }
    
    public void viewReservationDetails(Scanner scanner, Guest guest) {
        System.out.println("\n--- View Reservation Details ---");
        
        System.out.print("Enter Reservation ID: ");
        Long bookingId = Long.parseLong(scanner.nextLine());
        
        Booking booking = bookingEntitySessionBeanRemote.getBookingById(bookingId);
        
        System.out.println("\n--- Booking Details Can Be Found Below ---");
        System.out.println(booking);
    }
    
    public void viewAllReservations(Scanner scanner, Guest guest) {
        System.out.println("\n--- View All Reservations ---");
        
        List<Booking> bookings = bookingEntitySessionBeanRemote.getBookingByGuest(guest);
        
        for (Booking booking:bookings) {
            System.out.println(booking);
        }
    }
    
    private LocalDate dateFormatter(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate formattedDate = null;
        
        while (formattedDate == null) {
            try {
                formattedDate = LocalDate.parse(date, formatter);

                if (formattedDate.isBefore(LocalDate.now())) {
                    System.out.println("Date cannot be in the past!");
                    formattedDate = null;
                    continue;
                }
                
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use dd/MM/yyyy (e.g., 27/03/2024)");
            }
        }
        
        return formattedDate;
    }
}
