/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author timothy
 */
@Remote
public interface BookingEntitySessionBeanRemote {

    public Booking reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId) throws Exception;

    public List<Booking> allocateRoomToBookings(LocalDate date);
    
}
