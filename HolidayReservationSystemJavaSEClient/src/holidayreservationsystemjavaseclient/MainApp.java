/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemjavaseclient;

import java.util.List;
import java.util.Scanner;
import ws.hors.Account;
import ws.hors.Booking;
import ws.hors.HotelReservationSystemWebService_Service;
import ws.hors.RoomCount;
import ws.hors.RoomType;

/**
 *
 * @author clara
 */
public class MainApp {
    private HotelReservationSystemWebService_Service service;

    public MainApp() {
    }

    public MainApp(HotelReservationSystemWebService_Service service) {
        this.service = service;
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Holiday Reservation System ---");
            System.out.println("1. Partner Login");
            System.out.println("2. Search Hotel Room");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    partnerLogIn(scanner);
                    break;
                case 2:
                    searchRooms(scanner, null);
                    break;
                case 0:
                    System.out.println("Exiting application...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    public void partnerLogIn(Scanner scanner) {
        try {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            Account partner = service.getHotelReservationSystemWebServicePort().login(username, password);

            if (partner != null) {
                partnerMainMenu(scanner, partner);
            }
        } catch (Exception e) {
            System.out.println("Invalid Credentials.");
        }
    }
    
    public void searchRooms(Scanner scanner, Account partner) {
        try {
            System.out.println("\n--- Search Rooms ---");
            System.out.print("Enter Booking Start Date (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();

            System.out.print("Enter Booking End Date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine(); 
            
            List<RoomCount> rooms = service.getHotelReservationSystemWebServicePort().searchRooms(startDate, endDate);

            for (RoomCount room:rooms) {
                RoomType type = room.getRoomType();
                int count = room.getCount();
                double rate = service.getHotelReservationSystemWebServicePort().getRoomReservationRate(startDate, endDate, type.getId());
                String output = String.format("Room ID: %d, Room Type: %s, Room Rate: %.2f, Available Rooms: %d", type.getId(), type.getName(), rate, count);
                System.out.println(output);
            }

            if (partner != null) {
                System.out.print("Proceed to book room? (y/n) :");

                String confirmation = scanner.nextLine().trim();

                if ("y".equalsIgnoreCase(confirmation)) {
                    createReservation(scanner, startDate, endDate, partner);
                    System.out.println("Booking successfully made");
                } else {
                    System.out.println("Booking canceled.");
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while searching room");
        }
    }
    
    public void createReservation(Scanner scanner, String startDate, String endDate, Account partner) {
        System.out.println("\n--- Create Booking ---");
        
        try {
            System.out.print("Enter Room Type ID for Booking: ");
            Long roomTypeId = Long.parseLong(scanner.nextLine());
            
            // To Do: Check if it reference the partner or guest id
            service.getHotelReservationSystemWebServicePort().reserveRoom(startDate, endDate, roomTypeId, partner.getId());
            
            System.out.println("Room successfully booked");
            
        } catch (Exception e) {
            System.out.println("Failed to create new Booking");
        }
    }
    
    public void partnerMainMenu(Scanner scanner, Account partner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- Holiday Reservation System ---");
            System.out.println("1. Search Hotel Rooms");
            System.out.println("2. View My Booking Details");
            System.out.println("3. View All My Booking");
            System.out.println("0. Log Out");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    searchRooms(scanner, partner);
                    break;
                case 2:
                    viewReservationDetails(scanner, partner);
                    break;
                case 3:
                    viewAllReservations(scanner, partner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    public void viewReservationDetails(Scanner scanner, Account partner) {
        System.out.println("\n--- View Booking Details ---");
        
        System.out.print("Enter Booking ID: ");
        Long bookingId = Long.parseLong(scanner.nextLine());
        
        Booking booking = service.getHotelReservationSystemWebServicePort().viewReservationDetails(bookingId);
        
        if (booking.getAccount().getId().equals(partner.getId())) {
            System.out.println("\nBooking Details: ");
            System.out.println("Booking Id: " + booking.getId());
            System.out.println("Start Date: " + booking.getStartDate());
            System.out.println("End Date: " + booking.getEndDate());
            System.out.println("Room Type: " + booking.getRoomType().getName());
        } else {
            System.out.println("Invalid booking Id");
        }
    }
    
    public void viewAllReservations(Scanner scanner, Account partner) {
        System.out.println("\n--- View All Booking ---");
        
        try {
            List<Booking> bookings = service.getHotelReservationSystemWebServicePort().viewAllReservations(partner.getId());
            
            if (bookings.size() > 0) {
                for (Booking booking:bookings) {
                    System.out.println("\nBooking Details: ");
                    System.out.println("Booking Id: " + booking.getId());
                    System.out.println("Start Date: " + booking.getStartDate());
                    System.out.println("End Date: " + booking.getEndDate());
                    System.out.println("Room Type: " + booking.getRoomType().getName());
                }
            } else {
                System.out.println("No bookings made");
            }
            
        } catch (Exception e) {
            System.out.println("An error happened. Try again");
        }
    }
}