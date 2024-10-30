/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Guest;
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

    public List<Booking> findAllBookings();

//    public Booking createBooking(Booking newBooking) throws ConstraintViolationException;

    public List<Booking> getBookingByGuest(Guest guest);

    public Booking getBookingById(Long id);

    public List<Booking> allocateRoomToBookings(LocalDate date);
    
}
