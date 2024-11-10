/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Account;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Local
public interface AccountEntitySessionBeanLocal {

    public Account createGuest(Account newGuest) throws ConstraintViolationException;

    public Account logIn(String username, String password);
    
    public Account findGuestById(long id);
    
}
