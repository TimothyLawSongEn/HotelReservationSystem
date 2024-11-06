/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.singleton.AvailabilitySessionBeanLocal;
import ejb.session.stateless.BookingEntitySessionBeanLocal;
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

/**
 *
 * @author timothy
 */
@WebService(serviceName = "HotelReservationSystemWebService")
@Stateless
public class HotelReservationSystemWebService {

//    @EJB
//    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
    
    @EJB
    private AvailabilitySessionBeanLocal availabilitySessionBeanLocal;
    
    @EJB
    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;

//    @WebMethod(operationName = "login")
//    public String login(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
//        return partnerEntitySessionBeanLocal.login(username, password);
//    }
    
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "searchRooms")
    public List<Pair<RoomType, Integer>> searchRooms(
        @WebParam(name = "startDate") String startDateStr,
        @WebParam(name = "endDate") String endDateStr) {
    
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        // todo: break nextHigherRoomType, roomtype roomrate field can set to null (xmltransient)
        return availabilitySessionBeanLocal.getAvailableRoomTypesWithCount(startDate, endDate);
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
        return bookingEntitySessionBeanLocal.reserveRoomType(startDate, endDate, roomTypeId, partnerId);
    }

    @WebMethod(operationName = "viewReservationDetails")
    public Booking viewReservationDetails(@WebParam(name = "reservationId") long reservationId) {
        return bookingEntitySessionBeanLocal.getBookingById(reservationId);
    }

    @WebMethod(operationName = "viewAllReservations")
    public List<Booking> viewAllReservations() {
        return bookingEntitySessionBeanLocal.getAllBookings();
    }
    
//    @WebMethod(operationName = "viewAllReservations2")
//    public List<Booking> viewAllReservations(long partnerId) {
//        return bookingEntitySessionBeanLocal.getAllBookings(partnerId);
//    }
}
