///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
// */
//package ejb.session.ws;
//
//import ejb.session.singleton.AvailabilitySessionBeanLocal;
//import ejb.session.stateless.BookingEntitySessionBeanLocal;
//import entity.Booking;
//import entity.RoomType;
//import java.time.LocalDate;
//import java.util.List;
//import javafx.util.Pair;
//import javax.ejb.EJB;
//import javax.jws.WebService;
//import javax.jws.WebMethod;
//import javax.jws.WebParam;
//import javax.ejb.Stateless;
//
///**
// *
// * @author timothy
// */
//@WebService(serviceName = "HotelReservationSystemWebService")
//@Stateless
//public class HotelReservationSystemWebService {
//
//    /**
//     * This is a sample web service operation
//     */
//    @WebMethod(operationName = "hello")
//    public String hello(@WebParam(name = "name") String txt) {
//        return "Hello " + txt + " !";
//    }
//    
//    @EJB
//    private PartnerEntitySessionBeanLocal partnerEntitySessionBeanLocal;
//    
//    @EJB
//    private AvailabilitySessionBeanLocal availabilitySessionBeanLocal;
//    
//    @EJB
//    private BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;
//
//    public String login(String username, String password) {
//        return partnerEntitySessionBeanLocal.login(username, password);
//    }
//
//    public List<Pair<RoomType, Integer>> searchRooms(LocalDate startDate, LocalDate endDate) {
//        return availabilitySessionBeanLocal.getAvailableRoomTypesWithCount(startDate, endDate);
//    }
//
//    public Booking reserveRoom(LocalDate startDate, LocalDate endDate, long roomTypeId, long partnerId) {
//        return bookingEntitySessionBeanLocal.reserveRoomType(startDate, endDate, roomTypeId, partnerId);
//    }
//
//    public Booking viewReservationDetails(Long reservationId) {
//        return bookingEntitySessionBeanLocal.getBookingById(reservationId);
//    }
//
//    public List<Booking> viewAllReservations() {
//        return bookingEntitySessionBeanLocal.getAllBookings();
//    }
//}
