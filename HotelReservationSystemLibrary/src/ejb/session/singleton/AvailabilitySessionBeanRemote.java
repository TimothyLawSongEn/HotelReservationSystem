/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.singleton;

import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import util.dto.RoomCount;
import util.exception.InvalidDateRangeException;

/**
 *
 * @author timothy
 */
@Remote
public interface AvailabilitySessionBeanRemote {

    public List<RoomCount> getAvailableRoomTypesWithCount(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException;
    
}
