/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import javax.ejb.Remote;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Remote
public interface GuestEntitySessionBeanRemote {
    
    public Guest createGuest(Guest newGuest) throws ConstraintViolationException;

    public Guest logIn(String email, String password);
    
    public Guest findGuestById(long id);
    
}
