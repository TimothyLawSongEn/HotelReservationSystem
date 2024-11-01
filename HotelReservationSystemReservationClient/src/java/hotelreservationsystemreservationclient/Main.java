/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.GuestEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author timothy
 */
public class Main {

    @EJB
    private static GuestEntitySessionBeanRemote guestEntitySessionBeanRemote;
    @EJB
    private static BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    @EJB
    private static AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(guestEntitySessionBeanRemote, bookingEntitySessionBeanRemote, availabilitySessionBeanRemote);
        mainApp.start();
    }    
}
