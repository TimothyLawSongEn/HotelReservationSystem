/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.BookingEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import entity.Room;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Startup;

/**
 *
 * @author timothy
 */
@Startup
@Singleton
@LocalBean
public class ScheduledTaskSessionBean {
    
    @EJB
    BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;
    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    
    @Schedule(hour = "2", minute = "0", second = "0", persistent = false)
    public void runScheduledAllocation() {
        bookingEntitySessionBeanLocal.allocateRoomToBookings(LocalDate.now()); // Passes today's date
    }
    
    @Schedule(hour = "12", minute = "0", second = "0", persistent = false)
    public void runScheduledRoomBookingsUpdate() {
        roomEntitySessionBeanLocal.updateRoomBookingsAtCheckoutTime(LocalDate.now());
    }

}
