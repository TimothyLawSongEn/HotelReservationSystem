/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Guest;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;

/**
 *
 * @author timothy
 */
@Remote
public interface BookingEntitySessionBeanRemote {

    public Booking reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, long guestId) throws Exception;

    public List<Booking> allocateRoomToBookings(LocalDate date);
    
    public List<Booking> getBookingByGuest(Guest guest);
    
    public Booking getBookingById(Long id);

    public void checkIn(Long bookingId) throws Exception;

    public void checkOut(Long bookingId) throws Exception;

    public List<Booking> retrieveActiveBookingsForCheckIn(Long guestId);

    public List<Booking> retrieveCheckedInBookings(Long guestId);

    public Pair<List<Booking>, List<Booking>> getBookingsWithRoomAllocException(LocalDate date);
}
