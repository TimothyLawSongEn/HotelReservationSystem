package holidayreservationsystemjavaseclient;

import java.util.List;
import ws.hors.Booking;
import ws.hors.HotelReservationSystemWebService_Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author timothy
 */
public class HolidayReservationSystemJavaSEClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HotelReservationSystemWebService_Service service = new HotelReservationSystemWebService_Service();
        List<Booking> bookings = service.getHotelReservationSystemWebServicePort().viewAllReservations();
        
        for (Booking b:bookings) {
            System.out.println("Start Date: " + b.getStartDate() + "\nEnd Date: " + b.getEndDate() + "\nRoom Type: " + b.getRoomType());
        }
    } 
    
}
