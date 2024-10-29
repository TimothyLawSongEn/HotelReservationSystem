/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import entity.Employee;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author timothy
 */
public class MainApp {
    private RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    private RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    private RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    private BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    private GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    
    private RoomManagementModule roomManagementModule;
    private RoomTypeManagementModule roomTypeManagementModule;
    private SpecialRateManagementModule specialRateManagementModule;
    private FrontOfficeModule frontOfficeModule;

    public MainApp() {
    }

    public MainApp(RoomEntitySessionBeanRemote roomEntitySessionBeanRemote, RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote, RoomRateEntitySessionBeanRemote roomRateTransactionEntitySessionBeanRemote, EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, AvailabilitySessionBeanRemote availabilitySessionBeanRemote, BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote, GuestEntitySessionBeanRemote guestEntitySessionBeanRemote) {
        this.roomEntitySessionBeanRemote = roomEntitySessionBeanRemote;
        this.roomTypeEntitySessionBeanRemote = roomTypeEntitySessionBeanRemote;
        this.roomRateEntitySessionBeanRemote = roomRateTransactionEntitySessionBeanRemote;
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.availabilitySessionBeanRemote = availabilitySessionBeanRemote;
        this.bookingEntitySessionBeanRemote = bookingEntitySessionBeanRemote;
        this.guestEntitySessionBeanRemote = guestEntitySessionBeanRemote;
        
        this.roomManagementModule = new RoomManagementModule(roomEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote);
        this.roomTypeManagementModule = new RoomTypeManagementModule(roomTypeEntitySessionBeanRemote);
        this.specialRateManagementModule = new SpecialRateManagementModule(roomTypeEntitySessionBeanRemote, roomRateEntitySessionBeanRemote);
        this.frontOfficeModule = new FrontOfficeModule(availabilitySessionBeanRemote, bookingEntitySessionBeanRemote, guestEntitySessionBeanRemote);
    }
    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("1. Employee Log In");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
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
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

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
            }
        }
    }

    // Employee Log In
    private void employeeLogIn(Scanner scanner) {
        System.out.print("Enter Employee ID: ");
        Long employeeId = Long.parseLong(scanner.nextLine());
        
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        
        Employee.EmployeeType type = employeeEntitySessionBeanRemote.logIn(employeeId, password);
        
        switch (type) {
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
                System.out.println("Invalid credentials");
                break;
        }
    }
    
    // System Admin Menu
    private void systemAdmin(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("\n--- System Admin Menu---");
            System.out.println("1. Create New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Create New Partner");
            System.out.println("4. View All Partners");
            System.out.println("0. Log Out");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createNewEmployee(scanner);
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 0:
                    System.out.println("Logging out...");
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
    }
    
    // Create New Employee for System Admin
    private void createNewEmployee(Scanner scanner) {
        System.out.println("\n---Select Employee Type---");
        System.out.println("1. Create New System Admin");
        System.out.println("2. Create New Operations Manager");
        System.out.println("3. Create New Sales Manager");
        System.out.println("4. Create New Guest Relation Manager");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        Employee.EmployeeType newType = null;
        
        switch (choice) {
            case 1:
                newType = Employee.EmployeeType.SYSTEMADMIN;
                break;
            case 2:
                newType = Employee.EmployeeType.OPSMANAGER;
                break;
            case 3:
                newType = Employee.EmployeeType.SALESMANAGER;
                break;
            case 4: 
                newType = Employee.EmployeeType.GUESTRELATIONOFFICER;
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        
        System.out.print("Create Password for Employee: ");
        String password = scanner.nextLine();
        
        Employee newEmployee = new Employee(password, newType);
        Employee persistedEmployee = employeeEntitySessionBeanRemote.createEmployee(newEmployee);
        
        System.out.println("Employee Successfully Created: " + persistedEmployee);
    }
    
    // View all Employees for System Admin
    private void viewAllEmployees() {
        try {
            List<Employee> employees = employeeEntitySessionBeanRemote.viewAllEmployees();
            
            for (Employee e:employees) {
                System.out.println(e);
            }
            
        } catch (Exception e) {
            System.out.println("An error occurred while fetching employees: " + e.getMessage());
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
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
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
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
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
            }
        }
    }
    
    private void guestRelationOfficer(Scanner scanner) {
        System.out.println("\n---Guest Relationship Officer Menu---");
    }
}
