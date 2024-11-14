/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Account;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Local
public interface AccountEntitySessionBeanLocal {

    public Account createAccount(Account account) throws ConstraintViolationException;

    public Account logIn(String username, String password);
    
    public Account findGuestById(long id);

    public List<Account> getAllPartnerAccounts();

    public Account logInForGuest(String username, String password);

    public Account logInForPartner(String username, String password);
    
}
