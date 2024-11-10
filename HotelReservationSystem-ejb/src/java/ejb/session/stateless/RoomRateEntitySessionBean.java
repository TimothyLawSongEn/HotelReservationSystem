/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author timothy
 */
@Stateless
public class RoomRateEntitySessionBean implements RoomRateEntitySessionBeanRemote, RoomRateEntitySessionBeanLocal {

    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    // Create a new RoomRate
    @Override
    public void persistRoomRate(RoomRate roomRate) {
        em.persist(roomRate);
    }

    // Update an existing RoomRate
    @Override
    public RoomRate updateRoomRate(RoomRate roomRate) {
        // todo: ensure roomrate id exists
        return em.merge(roomRate);
    }

    // Delete a RoomRate by ID
    @Override
    public void deleteRoomRate(Long roomRateId) {
        RoomRate roomRate = em.find(RoomRate.class, roomRateId);
        if (roomRate != null) {
            em.remove(roomRate);
        }
    }

    // Find a RoomRate by ID
    @Override
    public RoomRate findRoomRate(Long roomRateId) {
        return em.find(RoomRate.class, roomRateId);
    }

    // Get all RoomRates
    @Override
    public List<RoomRate> findAllRoomRates() {
        TypedQuery<RoomRate> query = em.createQuery("SELECT rr FROM RoomRate rr", RoomRate.class);
        return query.getResultList();
    }
}
