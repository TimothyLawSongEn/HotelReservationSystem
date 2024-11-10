/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Account;
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
public class AccountEntitySessionBean implements AccountEntitySessionBeanRemote, AccountEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Account createGuest(Account newGuest) throws ConstraintViolationException {
        em.persist(newGuest);
        return newGuest;
    }
    
    @Override
    public Account logIn(String email, String password) {
        Account guest = em.createQuery("SELECT g FROM Guest g WHERE g.email = :email", Account.class)
                .setParameter("email", email)
                .getSingleResult();
        
        if (guest != null && guest.getPassword().equals(password)) {
            return guest;
        }
        
        return null;
    }
    
    @Override
    public Account findGuestById(long id) {
        return em.find(Account.class, id);
    }
}
