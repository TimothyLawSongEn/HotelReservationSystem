/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.util.List;
import javax.ejb.Local;
import util.exception.InvalidDateRangeException;

/**
 *
 * @author timothy
 */
@Local
public interface RoomRateEntitySessionBeanLocal {
    
    public void persistRoomRate(RoomRate roomRate) throws InvalidDateRangeException;

    public RoomRate updateRoomRate(RoomRate roomRate) throws InvalidDateRangeException;

    public void deleteRoomRate(Long roomRateId);

    public RoomRate findRoomRate(Long roomRateId);

    public List<RoomRate> findAllRoomRates();
}
