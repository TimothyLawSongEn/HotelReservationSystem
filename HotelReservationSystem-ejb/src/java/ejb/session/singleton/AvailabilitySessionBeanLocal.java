/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.singleton;

import entity.RoomCount;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Local;
import util.exception.InvalidDateRangeException;

/**
 *
 * @author timothy
 */
@Local
public interface AvailabilitySessionBeanLocal {
    
    public void loadRoomTypesAndBookings(); // caller: data init bean
    
    public void incrementBookedCount(LocalDate startDate, LocalDate endDate, long roomTypeId) throws Exception; // caller: booking bean
    
    public List<RoomCount> getAvailableRoomTypesWithCount(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException; // caller: web service

}
