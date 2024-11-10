/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Account;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author timothy
 */
@Local
public interface BookingEntitySessionBeanLocal {

    public List<Booking> getAllBookings();

    public List<Booking> getBookingByGuest(Account guest);

    public Booking getBookingById(Long id);

    public List<Booking> allocateRoomToBookings(LocalDate date);
    
    public Booking reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, long guestId) throws Exception;

    public boolean existBookingWithRoom(Long roomId);

}
