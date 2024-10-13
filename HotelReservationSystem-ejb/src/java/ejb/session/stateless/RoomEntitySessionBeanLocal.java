/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author timothy
 */
@Local
public interface RoomEntitySessionBeanLocal {
    
    public Room createRoom(Room newRoom) throws ConstraintViolationException;

    public Room updateRoom(Long roomId, String roomNumber, RoomType roomType);

    public void deleteRoom(Long roomId);

    public List<Room> viewAllRooms();

    public Room findRoomById(Long roomId);
}
