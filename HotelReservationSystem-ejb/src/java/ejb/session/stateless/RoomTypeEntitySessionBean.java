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
import javax.persistence.EntityNotFoundException;

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
    public RoomType persistRoomType(RoomType roomType) {
        em.persist(roomType);
        em.flush();
        return roomType;
    }
    
    @Override
    public RoomType persistRoomType(RoomType roomType, long nextHigherRoomTypeId) {
        RoomType nextHigherRoomType = findRoomType(nextHigherRoomTypeId);
        if (nextHigherRoomType == null) {
            throw new EntityNotFoundException("Next Higher Room Type of id" + nextHigherRoomTypeId + " does not exist.");
        }
        roomType.setNextHigherRoomType(nextHigherRoomType);
        em.persist(roomType);
        em.flush();
        return roomType;
    }
    
    // Update an existing RoomType
    @Override
    public RoomType updateRoomType(RoomType roomType) {
        return em.merge(roomType);
    }

    // Delete a RoomType by ID
    @Override
    public RoomType deleteRoomType(Long roomTypeId) throws Exception {
        RoomType roomTypeToDelete = em.find(RoomType.class, roomTypeId);
        
        if (roomTypeToDelete == null) {
            throw new Exception("RoomType not found.");
        }

        // Check if the RoomType is associated with any rooms
        List<Room> rooms = em.createQuery("SELECT r FROM Room r WHERE r.roomType = :roomType", Room.class)
                              .setParameter("roomType", roomTypeToDelete)
                              .getResultList();

        if (rooms.isEmpty()) {
            // set nextHigherRoomType for entities that are pointing to this roomTypeToDelete
            List<RoomType> allRoomTypes = findAllRoomTypes();
            RoomType newNextHigherRoomType = roomTypeToDelete.getNextHigherRoomType();
            for (RoomType rt : allRoomTypes) {
                if (rt.getNextHigherRoomType() != null && rt.getNextHigherRoomType().equals(roomTypeToDelete)) {
                    rt.setNextHigherRoomType(newNextHigherRoomType);
                    em.merge(rt); // Note: redundant merge call since rt is already managed
                }
            }

//            for (RoomRate rate : roomTypeToDelete.getRates()) {
//                em.remove(rate);  // Remove each RoomRate explicitly
//            }
//
//            roomTypeToDelete.getRates().clear();  // Clear the collection to ensure orphan removal
//            em.flush();  // Flush to ensure all changes are synchronized with the database

            em.remove(roomTypeToDelete); // deletion of associated roomrate automated by orphan removal / cascade
            System.out.println("RoomType and its associated RoomRates deleted successfully.");
            return roomTypeToDelete;
        } else {
            // Handle case where RoomType is in use 
            roomTypeToDelete.setDisabled(true);
            throw new Exception("Cannot delete RoomType with associated rooms. RoomType will be disabled.");
        }
    }

    // Find a RoomType by ID
    @Override
    public RoomType findRoomType(Long roomTypeId) {
//        return em.find(RoomType.class, roomTypeId);

//        List<RoomRate> rates = em.createQuery(
//            "SELECT r FROM RoomRate r WHERE r.roomType.id = :roomTypeId", 
//            RoomRate.class)
//            .setParameter("roomTypeId", roomTypeId)
//            .getResultList();

        RoomType type = em.find(RoomType.class, roomTypeId);
        if (type == null || type.isDisabled()) {
            return null;
        }
        
        // Force a refresh to ensure we get the latest data
        em.refresh(type);
        return type;
    }

    // Get all RoomTypes
    @Override
    public List<RoomType> findAllRoomTypes() {
        //  TypedQuery<RoomType> query = em.createQuery("SELECT rt FROM RoomType rt", RoomType.class);
        //  return query.getResultList();
        return em.createQuery(
            "SELECT DISTINCT rt FROM RoomType rt " +
            "LEFT JOIN FETCH rt.rates " +
            "WHERE rt.disabled = false " +
            "ORDER BY rt.id ASC", 
            RoomType.class)
            .getResultList();
    }
    
    @Override
    public List<RoomType> findAllNonDisabledRoomTypes() {
        TypedQuery<RoomType> query = em.createQuery("SELECT rt FROM RoomType rt WHERE rt.disabled = false", RoomType.class);
        return query.getResultList();
    }
}
