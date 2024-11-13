/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemjavaseclient;

import java.util.Scanner;
import ws.hors.Account;
import ws.hors.HotelReservationSystemWebService_Service;

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
            }
        }
    }
    
    public void partnerLogIn(Scanner scanner) {
        try {
            System.out.print("Enter Username: ");
            String username = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            // To Do: Call partner log in func
//            Account guest = service.getHotelReservationSystemWebServicePort().logIn(username, password);

//            if (partner != null) {
//                partnerMainMenu(scanner, partner);
//            }
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
            
            // Show Available Room Types Between Start and End Date
            
//            List<Pair<RoomType, Integer>> rooms = service.getHotelReservationSystemWebServicePort().searchRooms(startDate, endDate);
//            for (Pair<RoomType, Integer> room:rooms) {
//                System.out.println(room);
//            }

            if (partner != null) {
                System.out.println("Proceed to book room?");
                System.out.println("1. Yes");
                System.out.println("2. No");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    createReservation(scanner, startDate, endDate, partner);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while searching room");
        }
    }
    
    public void createReservation(Scanner scanner, String startDate, String endDate, Account partner) {
        System.out.println("\n--- Create Reservation ---");
        
        try {
            System.out.print("Enter Room Type ID for Reservation: ");
            Long roomTypeId = Long.parseLong(scanner.nextLine());
            
//            Booking persistedBooking = new Booking(startDate, endDate, roomTypeId, partner.getId());

//            System.out.println("Reservation successfully created " + persistedBooking);
            
        } catch (Exception e) {
            System.out.println("Failed to create new reservation");
        }
    }
}
