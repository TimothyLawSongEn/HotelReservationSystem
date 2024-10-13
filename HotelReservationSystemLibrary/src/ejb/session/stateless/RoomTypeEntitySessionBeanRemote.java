/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomType;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author timothy
 */
@Remote
public interface RoomTypeEntitySessionBeanRemote {

    public void createRoomType(RoomType roomType);

    public RoomType updateRoomType(RoomType roomType);

    public void deleteRoomType(Long roomTypeId);

    public RoomType findRoomType(Long roomTypeId);

    public List<RoomType> findAllRoomTypes();
    
}
