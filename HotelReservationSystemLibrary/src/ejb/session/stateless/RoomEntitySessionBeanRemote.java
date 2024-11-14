/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.Remote;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author timothy
 */
@Remote
public interface RoomEntitySessionBeanRemote {

    public Room createRoom(Room newRoom) throws ConstraintViolationException;

    public Room updateRoom(Room room);

    public void deleteRoom(Long roomId) throws Exception;

    public List<Room> findAllRooms();

    public Room findRoomById(Long roomId);

    public void updateRoomBookingsAtCheckoutTime(LocalDate date);
    
}
