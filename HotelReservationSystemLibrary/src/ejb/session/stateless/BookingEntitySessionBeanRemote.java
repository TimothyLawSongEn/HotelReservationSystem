/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Guest;
import java.util.List;
import javax.ejb.Remote;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Remote
public interface BookingEntitySessionBeanRemote {
    public Booking createBooking(Booking newBooking) throws ConstraintViolationException;

    public List<Booking> getBookingByGuest(Guest guest);
    
    public Booking getBookingById(Long id);
}
