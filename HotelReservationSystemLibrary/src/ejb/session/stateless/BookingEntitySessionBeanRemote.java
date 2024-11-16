/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Account;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.exception.BookingAlreadyCheckedInException;
import util.exception.BookingNoAllocatedRoomException;
import util.exception.EntityMissingException;

/**
 *
 * @author timothy
 */
@Remote
public interface BookingEntitySessionBeanRemote {

    public List<Booking> reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, int numRooms, long accountId) throws Exception;
    
    public List<Booking> allocateRoomToBookings(LocalDate date);
    
    public Booking getBookingById(Long id);
    
    public List<Booking> getBookingsByAccountId(Long accountId);

    public void checkIn(Long bookingId) throws EntityMissingException, BookingAlreadyCheckedInException, BookingNoAllocatedRoomException;

    public void checkOut(Long bookingId) throws Exception;

    public List<Booking> retrieveActiveBookingsForCheckIn(Long guestId);

    public List<Booking> retrieveCheckedInBookings(Long guestId);

    public Pair<List<Booking>, List<Booking>> getBookingsWithRoomAllocException(LocalDate date);
}
