/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Room;
import entity.RoomType;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author timothy
 */
@Stateless
public class RoomTypeEntitySessionBean implements RoomTypeEntitySessionBeanRemote, RoomTypeEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Create a new RoomType
    @Override
    public void createRoomType(RoomType roomType) {
        em.persist(roomType);
    }
    
    // Update an existing RoomType
    @Override
    public RoomType updateRoomType(RoomType roomType) {
        return em.merge(roomType);
    }

    // Delete a RoomType by ID
    @Override
    public RoomType deleteRoomType(Long roomTypeId) throws Exception {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        
        if (roomType == null) {
            throw new Exception("RoomType not found.");
        }

        // Check if the RoomType is associated with any rooms
        List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType", Room.class)
                              .setParameter("roomType", roomType)
                              .getResultList();

        if (rooms.isEmpty()) {
            em.remove(roomType); // roomrates are deleted because of cascade
            System.out.println("RoomType and its associated RoomRates deleted successfully.");
            return roomType;
        } else {
            // Handle case where RoomType is in use 
            roomType.setDisabled(true);
            throw new Exception("Cannot delete RoomType with associated rooms. RoomType will be disabled.");
        }

    }

    // Find a RoomType by ID
    @Override
    public RoomType findRoomType(Long roomTypeId) {
        return em.find(RoomType.class, roomTypeId);
    }

    // Get all RoomTypes
    @Override
    public List<RoomType> findAllRoomTypes() {
        TypedQuery<RoomType> query = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class);
        return query.getResultList();
    }
}
