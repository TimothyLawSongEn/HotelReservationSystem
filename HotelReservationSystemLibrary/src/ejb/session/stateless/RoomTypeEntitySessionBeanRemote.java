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

    public RoomType persistRoomType(RoomType roomType);

    public RoomType persistRoomType(RoomType roomType, long nextHigherRoomTypeId);

    public RoomType updateRoomType(RoomType roomType);

    public RoomType deleteRoomType(Long roomTypeId) throws Exception;

    public RoomType findRoomType(Long roomTypeId);

    public List<RoomType> findAllRoomTypes();

    public List<RoomType> findAllNonDisabledRoomTypes();
    
}
