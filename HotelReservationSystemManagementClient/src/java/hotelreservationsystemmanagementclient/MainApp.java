/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import ejb.session.stateless.AccountEntitySessionBeanRemote;
import util.client.InputUtils;

/**
 *
 * @author timothy
 */
public class MainApp {
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    
    private RoomManagementModule roomManagementModule;
    private RoomTypeManagementModule roomTypeManagementModule;
    private SpecialRateManagementModule specialRateManagementModule;
    private FrontOfficeModule frontOfficeModule;
    private EmployeePartnerManagementModule employeePartnerManagementModule;

    public MainApp() {
    }

    public MainApp(RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, AvailabilitySessionBeanRemote availabilitySessionBeanRemote, BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote, AccountEntitySessionBeanRemote accountEntitySessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        
        this.roomManagementModule = new RoomManagementModule(roomEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote, bookingEntitySessionBeanRemote);
        this.roomTypeManagementModule = new RoomTypeManagementModule(roomTypeEntitySessionBeanRemote);
        this.specialRateManagementModule = new SpecialRateManagementModule(roomTypeEntitySessionBeanRemote, roomRateEntitySessionBeanRemote);
        this.frontOfficeModule = new FrontOfficeModule(availabilitySessionBeanRemote, bookingEntitySessionBeanRemote, accountEntitySessionBeanRemote, roomEntitySessionBeanRemote);
        this.employeePartnerManagementModule = new EmployeePartnerManagementModule(employeeEntitySessionBeanRemote, accountEntitySessionBeanRemote);
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("1. Employee Log In");
            System.out.println("0. Exit");
            
            int choice = InputUtils.readInt(scanner, "> ");
            
            switch (choice) {
                case 1:
                    employeeLogIn(scanner);
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
    
    public void timothyStart() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("Main menu:");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Room Types");
            System.out.println("3. Manage Special Rates");
            System.out.println("4. Front Office");
            System.out.println("0. Exit");
            int choice = InputUtils.readInt(scanner, "> ");

            switch (choice) {
                case 1:
                    roomManagementModule.manageRooms(scanner);
                    break;
                case 2:
                    roomTypeManagementModule.manageRoomTypes(scanner);
                    break;
                case 3:
                    specialRateManagementModule.manageSpecialRates(scanner);
                    break;
                case 4:
                    frontOfficeModule.frontOfficeMenu(scanner);
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

    // Employee Log In
    private void employeeLogIn(Scanner scanner) {
        String username = InputUtils.readString(scanner, "Enter Username: ");
        String password = InputUtils.readString(scanner, "Enter Password: ");
        
        Employee.EmployeeType type = employeeEntitySessionBeanRemote.logIn(username, password);
        
        if (type == null) {
            System.out.println("Invalid credentials");
            return;
        }
        
        switch (type) {
            case ALLACCESS:
                allAccess(scanner);
                break;
            case SYSTEMADMIN:
                systemAdmin(scanner);
                break;
            case OPSMANAGER:
                opsManager(scanner);
                break;
            case SALESMANAGER:
                salesManager(scanner);
                break;
            case GUESTRELATIONOFFICER:
                guestRelationOfficer(scanner);
                break;
            default:
                System.out.println("Unrecognized employee type");
                break;
        }
    }
    
    private void allAccess(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- Hotel Management System [ALL ACCESS] ---");
            System.out.println("Main menu:");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Room Types");
            System.out.println("3. Manage Special Rates");
            System.out.println("4. Front Office");
            System.out.println("5. Manage Employees and Partners");
            System.out.println("0. Log Out");
            int choice = InputUtils.readInt(scanner, "> ");

            switch (choice) {
                case 1:
                    roomManagementModule.manageRooms(scanner);
                    break;
                case 2:
                    roomTypeManagementModule.manageRoomTypes(scanner);
                    break;
                case 3:
                    specialRateManagementModule.manageSpecialRates(scanner);
                    break;
                case 4:
                    frontOfficeModule.frontOfficeMenu(scanner);
                    break;
                case 5:
                    employeePartnerManagementModule.manageEmployeesAndPartners(scanner);
                    break;
                case 0:
                    System.out.println("Logging Out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    // System Admin Menu
    private void systemAdmin(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- System Admin Menu---");
            System.out.println("1. Manage Employees and Partners");
            System.out.println("0. Log Out");
            
            int choice = InputUtils.readInt(scanner, "> ");
            
            switch (choice) {
                case 1:
                    employeePartnerManagementModule.manageEmployeesAndPartners(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    // Operations Manager Menu
    private void opsManager(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n---Operations Manager Menu---");
            System.out.println("1. Manage Room");
            System.out.println("2. Manage Room Type");
            System.out.println("0. Log Out");
            
            int choice = InputUtils.readInt(scanner, "> ");
            
            switch (choice) {
                case 1:
                    roomManagementModule.manageRooms(scanner);
                    break;
                case 2:
                    roomTypeManagementModule.manageRoomTypes(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    // Sales Manager Menu
    private void salesManager(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n---Sales Manager Menu---");
            System.out.println("1. Manage Special Rates");
            System.out.println("0. Log Out");
            
            int choice = InputUtils.readInt(scanner, "> ");
            
            switch (choice) {
                case 1:
                    specialRateManagementModule.manageSpecialRates(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
    
    private void guestRelationOfficer(Scanner scanner) {
        frontOfficeModule.frontOfficeMenu(scanner);
    }
}
