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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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
        List<Pair<RoomType, Integer>> roomtypes = availabilitySessionBeanLocal.getAvailableRoomTypesWithCount(startDate, endDate);
        return roomtypes;
    }
    
    @WebMethod(operationName = "getListOfPair")
    public List<Pair<Integer, Integer>> getListOfPair() {
        List<Pair<Integer, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(1,1));
        list.add(new Pair<>(2,2));
        return list;
    }
    
    @WebMethod(operationName = "getListOfPairRt")
    public List<Pair<RoomType, Integer>> getListOfPairRt() {
        List<Pair<RoomType, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(new RoomType("hi", 10.0, 10.0),1));
        list.add(new Pair<>(new RoomType("bye", 20.0, 20.0),2));
        return list;
    }
    
    @WebMethod(operationName = "getLocalDate")
    public LocalDate getLocalDate() {
        return LocalDate.of(2020, 1, 8);
    }
    
    @WebMethod(operationName = "getnullLocalDate")
    public LocalDate getnullLocalDate() {
        return null;
    }
    
    @WebMethod(operationName = "getDate")
    public Date getDate() {
        LocalDate localDate = LocalDate.of(2025, 1, 1);
        // Convert LocalDate to java.util.Date
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }
    
    // TODO: get Date, if works, chg all localdate to date

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
    public List<Booking> viewAllReservations() {
        return bookingEntitySessionBeanLocal.getAllBookings();
    }
    
//    @WebMethod(operationName = "viewAllReservations2")
//    public List<Booking> viewAllReservations(long partnerId) {
//        return bookingEntitySessionBeanLocal.getAllBookings(partnerId);
//    }
}
