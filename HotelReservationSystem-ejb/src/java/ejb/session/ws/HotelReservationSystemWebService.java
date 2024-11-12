/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.singleton.AvailabilitySessionBeanLocal;
import ejb.session.stateless.AccountEntitySessionBeanLocal;
import ejb.session.stateless.BookingEntitySessionBeanLocal;
import entity.Account;
import entity.Booking;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.EntityMissingException;
import util.exception.WrongAccountTypeException;

/**
 *
 * @author timothy
 */
@WebService(serviceName = "HotelReservationSystemWebService")
@Stateless
public class HotelReservationSystemWebService {

    @EJB
    private AccountEntitySessionBeanLocal accountEntitySessionBeanLocal;
    @EJB
    private AvailabilitySessionBeanLocal availabilitySessionBeanLocal;
    @EJB
    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;
    
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "login")
    public Account login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        Account account = accountEntitySessionBeanLocal.logIn(username, password);
        System.out.println(account.getId() +" "+ account.getUsername() +" "+ account.getPassword());
        return account;
    }

    @WebMethod(operationName = "searchRooms")
    public List<Pair<RoomType, Integer>> searchRooms(
        @WebParam(name = "startDate") String startDateStr,
        @WebParam(name = "endDate") String endDateStr) {
    
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // todo: break nextHigherRoomType, roomtype roomrate field can set to null (xmltransient)
        List<Pair<RoomType, Integer>> roomtypes = availabilitySessionBeanLocal.getAvailableRoomTypesWithCount(startDate, endDate);
        return roomtypes;
    }
    
    @WebMethod(operationName = "reserveRoom")
    public Booking reserveRoom(
        @WebParam(name = "startDate") String startDateStr,
        @WebParam(name = "endDate") String endDateStr, 
        @WebParam(name = "roomTypeId") long roomTypeId, 
        @WebParam(name = "partnerId") long partnerId
    ) throws Exception {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        Booking booking = bookingEntitySessionBeanLocal.reserveRoomType(startDate, endDate, roomTypeId, partnerId);
        return booking;
    }

    @WebMethod(operationName = "viewReservationDetails")
    public Booking viewReservationDetails(@WebParam(name = "reservationId") long reservationId) {
        return bookingEntitySessionBeanLocal.getBookingById(reservationId);
    }

    @WebMethod(operationName = "viewAllReservations")
    public List<Booking> viewAllReservationsByPartnerId(@WebParam(name = "accountId") long accountId) throws EntityMissingException, WrongAccountTypeException {
        Account account = accountEntitySessionBeanLocal.findGuestById(accountId);

        if (account == null) {
            throw new EntityMissingException("Account with the provided ID does not exist.");
        }

        if (account.getAccountType() != Account.AccountType.PARTNER) {
            throw new WrongAccountTypeException("AccountId provided does not belong to a partner account.");
        }

        return bookingEntitySessionBeanLocal.getBookingsByAccountId(accountId);
    }
}
