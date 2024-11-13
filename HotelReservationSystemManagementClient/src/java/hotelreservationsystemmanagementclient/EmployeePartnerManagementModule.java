/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.stateless.AccountEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import entity.Account;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import util.client.InputUtils;

/**
 *
 * @author timothy
 */
public class EmployeePartnerManagementModule {
    private EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote;
    private AccountEntitySessionBeanRemote accountEntitySessionBeanRemote;
    
    public EmployeePartnerManagementModule(EmployeeEntitySessionBeanRemote employeeEntitySessionBeanRemote, AccountEntitySessionBeanRemote accountEntitySessionBeanRemote) {
        this.employeeEntitySessionBeanRemote = employeeEntitySessionBeanRemote;
        this.accountEntitySessionBeanRemote = accountEntitySessionBeanRemote;
    }
    
    void manageEmployeesAndPartners(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Employee & Partner Management ---");
            System.out.println("1. Create New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Create New Partner");
            System.out.println("4. View All Partners");
            System.out.println("0. Back to Main Menu");
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
                case 3:
                    createNewPartner(scanner);
                    break;
                case 4:
                    viewAllPartners();
                    break;
                case 0:
                    return;
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
        
        String username = InputUtils.readString(scanner, "Create Username for Employee: ");
        String password = InputUtils.readString(scanner, "Create Password for Employee: ");
        
        Employee newEmployee = new Employee(username, password, newType);
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
    
    // Create New Partner
    private void createNewPartner(Scanner scanner) {
        System.out.println("\n--- Creating New Partner ---");
        
        System.out.print("Enter Partner Username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter Partner Password: ");
        String password = scanner.nextLine();
        
        try {
            Account partner = new Account(username, password);
            Account persistedPartner = accountEntitySessionBeanRemote.createAccount(partner);
            
            System.out.println("Partner account successfully created with Id: " + persistedPartner.getId());
        } catch (Exception e) {
            System.out.println("Unable to create partner. Try again");
        }
        
    }
    
    // View all partners
    private void viewAllPartners() {
        List<Account> accounts = accountEntitySessionBeanRemote.getAllPartnerAccounts();
        
        System.out.println("\nAll partner accounts are listed below");
        for (Account a: accounts) {
            System.out.println(a);
        }
    }
}
