/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package holidayreservationsystemjavaseclient;

import java.util.Scanner;
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
                    searchRooms(scanner);
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

            System.out.println("kinda working");

//            if (guest != null) {
//                guestMainMenu(scanner, guest);
//            }
        } catch (Exception e) {
            System.out.println("Invalid Credentials.");
        }
    }
    
    public void searchRooms(Scanner scanner) {
        try {
            System.out.println("\n--- Search Rooms ---");
            System.out.print("Enter Booking Start Date (YYYY-MM-DD): ");
//            LocalDate startDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter Booking End Date (YYYY-MM-DD): ");
//            LocalDate endDate = LocalDate.parse(scanner.nextLine()); 
            
            // Show Available Room Types Between Start and End Date
//            List<Pair<RoomType, Integer>> rooms = availabilitySessionBeanRemote.getAvailableRoomTypesWithCount(startDate, endDate);

//            for (Pair<RoomType, Integer> room:rooms) {
//                System.out.println(room);
//            }
        } catch (Exception e) {
            System.out.println("An error occurred while searching room");
        }
    }
}
