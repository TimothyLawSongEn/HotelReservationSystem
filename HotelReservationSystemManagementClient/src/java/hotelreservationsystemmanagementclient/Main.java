/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemmanagementclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.EmployeeEntitySessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.RoomEntitySessionBeanRemote;
import ejb.session.stateless.RoomRateEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author timothy
 */
public class Main {

    @EJB
    private static RoomEntitySessionBeanRemote roomEntitySessionBeanRemote;
    @EJB
    private static RoomTypeEntitySessionBeanRemote roomTypeEntitySessionBeanRemote;
    @EJB
    private static RoomRateEntitySessionBeanRemote roomRateEntitySessionBeanRemote;
    @EJB
    private static EmployeeEntitySessionBeanRemote employeeEntitySessionBean;
    @EJB
    private static AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    @EJB
    private static BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        MainApp mainApp = new MainApp(roomEntitySessionBeanRemote, roomTypeEntitySessionBeanRemote, roomRateEntitySessionBeanRemote, employeeEntitySessionBean, availabilitySessionBeanRemote, bookingEntitySessionBeanRemote, guestEntitySessionBeanRemote);
//        mainApp.timothyStart();
        mainApp.start();
    }
    
}
