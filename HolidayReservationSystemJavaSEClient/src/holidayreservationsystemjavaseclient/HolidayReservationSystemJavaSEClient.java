/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package holidayreservationsystemjavaseclient;

import ws.hors.HotelReservationSystemWebService_Service;
/**
 *
 * @author clara
 */
public class HolidayReservationSystemJavaSEClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HotelReservationSystemWebService_Service service = new HotelReservationSystemWebService_Service();
        MainApp mainApp = new MainApp(service);
        mainApp.start();
    }
    
}