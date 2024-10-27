/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Guest;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Stateless
public class BookingEntitySessionBean implements BookingEntitySessionBeanRemote, BookingEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Booking createBooking(Booking newBooking) throws ConstraintViolationException {
        em.persist(newBooking);
        return newBooking;
    }
    
    @Override
    public List<Booking> getBookingByGuest(Guest guest) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.guest = :guest", Booking.class)
                .setParameter("guest", guest)
                .getResultList();
    }
    
    @Override
    public Booking getBookingById(Long id) {
        return em.find(Booking.class, id);
    }
}
