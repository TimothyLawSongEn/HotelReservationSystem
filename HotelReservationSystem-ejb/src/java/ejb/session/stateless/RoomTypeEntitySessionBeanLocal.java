/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author timothy
 */
@Local
public interface RoomTypeEntitySessionBeanLocal {
    
    public void createRoomType(RoomType roomType);

    public RoomType updateRoomType(RoomType roomType);

    public RoomType deleteRoomType(Long roomTypeId) throws Exception;

    public RoomType findRoomType(Long roomTypeId);

    public List<RoomType> findAllRoomTypes();
}
