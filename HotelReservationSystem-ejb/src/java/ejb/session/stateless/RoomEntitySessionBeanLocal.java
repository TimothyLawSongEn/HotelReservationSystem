/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
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

    public Room updateRoom(Room room);

    public void deleteRoom(Long roomId);

    public List<Room> findAllRooms();

    public Room findRoomById(Long roomId);
    
    public int getNonDisabledRoomCountForRoomType(Long roomTypeId);

    public List<Room> getAvailableRoomsForRoomTypeAndDate(RoomType roomType, LocalDate date);

}
