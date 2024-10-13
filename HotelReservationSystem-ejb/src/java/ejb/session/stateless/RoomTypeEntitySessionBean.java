/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

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
    public void deleteRoomType(Long roomTypeId) {
        RoomType roomType = em.find(RoomType.class, roomTypeId);
        if (roomType != null) {
            em.remove(roomType);
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
