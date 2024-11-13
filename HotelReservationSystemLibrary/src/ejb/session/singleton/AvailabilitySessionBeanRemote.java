/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.singleton;

import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.Remote;
import util.exception.InvalidDateRangeException;

/**
 *
 * @author timothy
 */
@Remote
public interface AvailabilitySessionBeanRemote {

    public List<Pair<RoomType, Integer>> getAvailableRoomTypesWithCount(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException;
    
}
