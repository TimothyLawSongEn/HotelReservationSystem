/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.GuestEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.Employee;
import entity.Employee.EmployeeType;
import entity.Guest;
import entity.Room;
import entity.RoomType;
import entity.RoomRate;
import entity.RoomRate.SpecialRateType;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.LocalDate;
import javax.ejb.EJB;
import javax.ejb.LocalBean;

/**
 *
 * @author timothy
 */

/**
 * Singleton bean to initialize data for Room, RoomType, and RoomRate.
 * The bean will be created at startup and inject data into the database.
 */
@Singleton
@LocalBean
@Startup  // Ensures this bean is initialized on application startup
public class DataInitSessionBean {

    @EJB
    private RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;

    @EJB
    private RoomRateEntitySessionBeanLocal roomRateEntitySessionBeanLocal;

    @EJB
    private RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    
    @EJB
    private EmployeeEntitySessionBeanLocal employeeEntitySessionBeanLocal;
    
    @EJB
    private AvailabilitySessionBeanLocal availabilitySessionBeanLocal;
    
    @EJB
    private GuestEntitySessionBeanLocal guestEntitySessionBeanLocal;

    @PostConstruct  // Runs automatically after the bean is created
    public void initializeData() {
        // Create some RoomType objects
        RoomType standardRoomType = new RoomType("Standard Room", 200.0, 180.0);
        RoomType deluxeRoomType = new RoomType("Deluxe Room", 350.0, 320.0);
        standardRoomType.setNextHigherRoomType(deluxeRoomType);

        // Persist the RoomTypes using the session bean
        roomTypeEntitySessionBeanLocal.createRoomType(standardRoomType);
        roomTypeEntitySessionBeanLocal.createRoomType(deluxeRoomType);

        // Create RoomRate objects for the Standard Room Type
        RoomRate standardRoomPromoRate = new RoomRate("s promo", 150.0, LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31), SpecialRateType.PROMO, standardRoomType);
        RoomRate standardRoomPeakRate = new RoomRate("s peak", 250.0, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31), SpecialRateType.PEAK, standardRoomType);

        // Persist RoomRates using the session bean
        roomRateEntitySessionBeanLocal.createRoomRate(standardRoomPromoRate);
        roomRateEntitySessionBeanLocal.createRoomRate(standardRoomPeakRate);

        // Create RoomRate objects for the Deluxe Room Type
        RoomRate deluxeRoomPromoRate = new RoomRate("d promo", 300.0, LocalDate.of(2024, 12, 1), LocalDate.of(2024, 12, 31), SpecialRateType.PROMO, deluxeRoomType);
        RoomRate deluxeRoomPeakRate = new RoomRate("d peak", 400.0, LocalDate.of(2024, 7, 1), LocalDate.of(2024, 8, 31), SpecialRateType.PEAK, deluxeRoomType);

        // Persist RoomRates using the session bean
        roomRateEntitySessionBeanLocal.createRoomRate(deluxeRoomPromoRate);
        roomRateEntitySessionBeanLocal.createRoomRate(deluxeRoomPeakRate);

        // Create some Room objects linked to RoomTypes
        Room room101 = new Room("0101", standardRoomType);
//        Room room102 = new Room("0102", standardRoomType);
        Room room201 = new Room("0201", deluxeRoomType);

        // Persist the rooms using the session bean
        roomEntitySessionBeanLocal.createRoom(room101);
//        roomEntitySessionBeanLocal.createRoom(room102);
        roomEntitySessionBeanLocal.createRoom(room201);

        // Create System Administrator
        Employee systemAdmin = new Employee("password", EmployeeType.SYSTEMADMIN);
        
        // Persist system admin using session bean
        employeeEntitySessionBeanLocal.createEmployee(systemAdmin);
        
        // You can add more initialization logic here if needed
        System.out.println("Data initialization complete.");
        
        availabilitySessionBeanLocal.loadRoomTypesAndBookings(); // call after roomtypes and bookings created
        
        Guest guest1 = new Guest("tommy@gmail.com", "password");
        guestEntitySessionBeanLocal.createGuest(guest1);
    }
}
