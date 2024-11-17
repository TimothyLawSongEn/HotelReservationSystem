/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author timothy
 */
@Local
public interface BookingEntitySessionBeanLocal {

    public List<Booking> getAllBookings();

    public Booking getBookingById(Long id);
    
    public List<Booking> getBookingsByAccountId(Long accountId);

    public List<Booking> allocateRoomToBookings(LocalDate date);
    
    public List<Booking> reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, int numRooms, long accountId) throws Exception;

    public boolean existBookingWithRoom(Long roomId);

}
