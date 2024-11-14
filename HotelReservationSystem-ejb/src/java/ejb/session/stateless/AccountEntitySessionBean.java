/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Booking;
import entity.Account;
import entity.Account.AccountType;
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
    public Account createAccount(Account account) throws ConstraintViolationException {
        em.persist(account);
        return account;
    }
    
    @Override
    public Account logIn(String username, String password) {
        Account account = em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class)
                .setParameter("username", username)
                .getSingleResult();
        
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        
        return null;
    }
    
    @Override
    public Account logInForGuest(String username, String password) {
        Account account = em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class)
                .setParameter("username", username)
                .getSingleResult();
        
        if (account != null && account.getPassword().equals(password) && account.getAccountType().equals(Account.AccountType.GUEST)) {
            return account;
        }
        
        return null;
    }
    
    @Override
    public Account logInForPartner(String username, String password) {
        Account account = em.createQuery("SELECT a FROM Account a WHERE a.username = :username", Account.class)
                .setParameter("username", username)
                .getSingleResult();
        
        if (account != null && account.getPassword().equals(password) && account.getAccountType().equals(Account.AccountType.PARTNER)) {
            return account;
        }
        
        return null;
    }
    
    @Override
    public Account findGuestById(long id) {
        return em.find(Account.class, id);
    }
    
    @Override
    public List<Account> getAllPartnerAccounts() {
        return em.createQuery("SELECT a FROM Account a WHERE a.accountType = :type").setParameter("type", AccountType.PARTNER).getResultList();
    }
}
