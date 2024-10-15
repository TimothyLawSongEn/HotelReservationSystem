/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author timothy
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Create a new room
    @Override
    public Room createRoom(Room newRoom) throws ConstraintViolationException {
        em.persist(newRoom);
        return newRoom;
    }

    // Update room details
    @Override
    public Room updateRoom(Long roomId, String roomNumber, RoomType roomType) {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            room.setRoomNumber(roomNumber);
            room.setRoomType(roomType);
            em.merge(room);  // Update the room in the database
        }
        return room;
    }

    // Delete a room by ID
    @Override
    public void deleteRoom(Long roomId) {
        Room room = em.find(Room.class, roomId);
        if (room != null) {
            em.remove(room);
        }
    }

    // View all rooms
    @Override
    public List<Room> findAllRooms() {
        TypedQuery<Room> query = em.createQuery("SELECT r FROM Room r", Room.class);
        return query.getResultList();
    }

    // Find a room by ID
    @Override
    public Room findRoomById(Long roomId) {
        return em.find(Room.class, roomId);
    }
}