/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author timothy
 */
@Stateless
public class RoomEntitySessionBean implements RoomEntitySessionBeanRemote, RoomEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;

    // Create a new room
    @Override
    public Room createRoom(Room newRoom) {
        em.persist(newRoom);
        return newRoom;
    }

    // Update room details
    @Override
    public Room updateRoom(Room room) {
        Room origRoom = em.find(Room.class, room.getId());
        if (origRoom == null) {
            System.out.println("Programming error: RoomId " + room.getId() + " cannot be found. Did not update!");
            return null;
        }
        em.merge(room);  // Update the room in the database
        return room;
    }

    // Delete a room by ID
    @Override
    public void deleteRoom(Long roomId) throws Exception {
        Room room = em.find(Room.class, roomId);
        if (room == null) {
            throw new Exception("Room with roomId " + roomId + "does not exist.");
        }
        if (bookingEntitySessionBeanLocal.existBookingWithRoom(roomId)) {
            room.setDisabled(true);
            throw new Exception("Cannot delete RoomType with associated rooms. RoomType will be disabled.");
        }
        em.remove(room);
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

    @Override
    public int getNonDisabledRoomCountForRoomType(Long roomTypeId) {
        Query query = em.createQuery("SELECT COUNT(r) FROM Room r WHERE r.roomType.id = :roomTypeId AND r.disabled = FALSE");
        query.setParameter("roomTypeId", roomTypeId);

        return ((Long) query.getSingleResult()).intValue();
    }
    
    @Override
    public List<Room> getAvailableRoomsForRoomTypeAndDate(RoomType roomType, LocalDate date) { // FIXME
        return em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType AND (r.currentBooking IS NULL) AND (r.expectedBooking IS NULL)", Room.class)
                 .setParameter("roomType", roomType)
                 .setParameter("date", date)
                 .getResultList();
        
    //        query = "SELECT r FROM Room r WHERE r.roomType = :roomType AND " +
    //                "(r.currentBooking IS NULL OR r.currentBooking.endDate <= :date) AND r.expectedBooking IS NULL" // TODO: make date comparison work
    }

}