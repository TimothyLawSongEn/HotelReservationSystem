/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotelreservationsystemreservationclient;

import ejb.session.singleton.AvailabilitySessionBeanRemote;
import ejb.session.stateless.BookingEntitySessionBeanRemote;
import ejb.session.stateless.RoomTypeEntitySessionBeanRemote;
import javax.ejb.EJB;
import ejb.session.stateless.AccountEntitySessionBeanRemote;

/**
 *
 * @author timothy
 */
public class Main {

    @EJB
    private static AccountEntitySessionBeanRemote accountEntitySessionBeanRemote;
    @EJB
    private static BookingEntitySessionBeanRemote bookingEntitySessionBeanRemote;
    @EJB
    private static AvailabilitySessionBeanRemote availabilitySessionBeanRemote;
    
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(accountEntitySessionBeanRemote, bookingEntitySessionBeanRemote, availabilitySessionBeanRemote);
        mainApp.start();
    }    
}
