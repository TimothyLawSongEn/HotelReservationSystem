/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.AccountEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomRateEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.Employee;
import entity.Room;
import entity.RoomType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author clara
 */
@Singleton
@LocalBean
@Startup
public class TestDataInitSessionBean {

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
    private AccountEntitySessionBeanLocal accountEntitySessionBeanLocal;
    
    @PostConstruct
    public void initializeData() {
        
        // Create Employee
        Employee systemAdmin = new Employee("sysadmin", "password", Employee.EmployeeType.SYSTEMADMIN);
        Employee opsManager = new Employee("opmanager", "password", Employee.EmployeeType.OPSMANAGER);
        Employee salesManager = new Employee("salesmanager", "password", Employee.EmployeeType.SALESMANAGER);
        Employee guestOfficer = new Employee("guestrelo", "password", Employee.EmployeeType.GUESTRELATIONOFFICER);
        
        // Persist system admin using session bean
        employeeEntitySessionBeanLocal.createEmployee(systemAdmin);
        employeeEntitySessionBeanLocal.createEmployee(opsManager);
        employeeEntitySessionBeanLocal.createEmployee(salesManager);
        employeeEntitySessionBeanLocal.createEmployee(guestOfficer);
        
        // Create some RoomType objects
        RoomType deluxeRoom = new RoomType("Deluxe Room", 100.0, 50.0);
        RoomType premierRoom = new RoomType("Premier Room", 200.0, 100.0);
        RoomType familyRoom = new RoomType("Family Room", 300.0, 150.0);
        RoomType juniorSuite = new RoomType("Junior Suite", 400.0, 200.0);
        RoomType grandSuite = new RoomType("Grand Suite", 500.0, 250.0);
        
        // Persist the RoomTypes using the session bean
        roomTypeEntitySessionBeanLocal.createRoomType(deluxeRoom);
        roomTypeEntitySessionBeanLocal.createRoomType(premierRoom);
        roomTypeEntitySessionBeanLocal.createRoomType(familyRoom);
        roomTypeEntitySessionBeanLocal.createRoomType(juniorSuite);
        roomTypeEntitySessionBeanLocal.createRoomType(grandSuite);
        
        // Set Next Higher Room Type
        deluxeRoom.setNextHigherRoomType(premierRoom);
        premierRoom.setNextHigherRoomType(familyRoom);
        familyRoom.setNextHigherRoomType(juniorSuite);
        juniorSuite.setNextHigherRoomType(grandSuite);
        
        // Create deluxe Room objects linked to RoomTypes
        Room room101 = new Room("0101", deluxeRoom);
        Room room201 = new Room("0201", deluxeRoom);
        Room room301 = new Room("0301", deluxeRoom);
        Room room401 = new Room("0401", deluxeRoom);
        Room room501 = new Room("0501", deluxeRoom);
        
        Room room102 = new Room("0102", premierRoom);
        Room room202 = new Room("0202", premierRoom);
        Room room302 = new Room("0302", premierRoom);
        Room room402 = new Room("0402", premierRoom);
        Room room502 = new Room("0502", premierRoom);
        
        Room room103 = new Room("0103", familyRoom);
        Room room203 = new Room("0203", familyRoom);
        Room room303 = new Room("0303", familyRoom);
        Room room403 = new Room("0403", familyRoom);
        Room room503 = new Room("0503", familyRoom);
        
        Room room104 = new Room("0104", juniorSuite);
        Room room204 = new Room("0204", juniorSuite);
        Room room304 = new Room("0304", juniorSuite);
        Room room404 = new Room("0404", juniorSuite);
        Room room504 = new Room("0504", juniorSuite);
        
        Room room105 = new Room("0105", grandSuite);
        Room room205 = new Room("0205", grandSuite);
        Room room305 = new Room("0305", grandSuite);
        Room room405 = new Room("0405", grandSuite);
        Room room505 = new Room("0505", grandSuite);

        // Persist the rooms using the session bean
        roomEntitySessionBeanLocal.createRoom(room101);
        roomEntitySessionBeanLocal.createRoom(room201);
        roomEntitySessionBeanLocal.createRoom(room301);
        roomEntitySessionBeanLocal.createRoom(room401);
        roomEntitySessionBeanLocal.createRoom(room501);
        
        roomEntitySessionBeanLocal.createRoom(room102);
        roomEntitySessionBeanLocal.createRoom(room202);
        roomEntitySessionBeanLocal.createRoom(room302);
        roomEntitySessionBeanLocal.createRoom(room402);
        roomEntitySessionBeanLocal.createRoom(room502);
        
        roomEntitySessionBeanLocal.createRoom(room103);
        roomEntitySessionBeanLocal.createRoom(room203);
        roomEntitySessionBeanLocal.createRoom(room303);
        roomEntitySessionBeanLocal.createRoom(room403);
        roomEntitySessionBeanLocal.createRoom(room503);
        
        roomEntitySessionBeanLocal.createRoom(room104);
        roomEntitySessionBeanLocal.createRoom(room204);
        roomEntitySessionBeanLocal.createRoom(room304);
        roomEntitySessionBeanLocal.createRoom(room404);
        roomEntitySessionBeanLocal.createRoom(room504);
        
        roomEntitySessionBeanLocal.createRoom(room105);
        roomEntitySessionBeanLocal.createRoom(room205);
        roomEntitySessionBeanLocal.createRoom(room305);
        roomEntitySessionBeanLocal.createRoom(room405);
        roomEntitySessionBeanLocal.createRoom(room505);
    }
}
