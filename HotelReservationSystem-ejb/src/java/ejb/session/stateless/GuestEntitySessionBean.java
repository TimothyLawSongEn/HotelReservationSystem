/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Stateless
public class GuestEntitySessionBean implements GuestEntitySessionBeanRemote, GuestEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Guest createGuest(Guest newGuest) throws ConstraintViolationException {
        em.persist(newGuest);
        return newGuest;
    }
    
    @Override
    public Guest logIn(String email, String password) {
        Guest guest = em.createQuery("SELECT g FROM Guest g WHERE g.email = :email", Guest.class)
                .setParameter("email", email)
                .getSingleResult();
        
        if (guest != null && guest.getPassword().equals(password)) {
            return guest;
        }
        
        return null;
    }
    
    @Override
    public Guest findGuestById(long id) {
        return em.find(Guest.class, id);
    }
}
