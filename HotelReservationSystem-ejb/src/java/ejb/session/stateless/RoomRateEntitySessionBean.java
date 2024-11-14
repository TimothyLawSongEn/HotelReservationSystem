/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomRate;
import java.time.LocalDate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import util.exception.InvalidDateRangeException;

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
    public void persistRoomRate(RoomRate roomRate) throws InvalidDateRangeException {
        validateRoomRateDateRange(roomRate.getStartDate(), roomRate.getEndDate());
        em.persist(roomRate);
    }

    // Update an existing RoomRate
    @Override
    public RoomRate updateRoomRate(RoomRate roomRate) throws InvalidDateRangeException {
        // todo: ensure roomrate id exists
        validateRoomRateDateRange(roomRate.getStartDate(), roomRate.getEndDate());
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
    
    private void validateRoomRateDateRange(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        // 1. Check if startDate or endDate are null
        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException("Start date and end date cannot be null.");
        }

        // 2. Check if endDate is after startDate
        if (endDate.isBefore(startDate)) {
            throw new InvalidDateRangeException("End date cannot be before Start date.");
        }

        // 3. Optional: Check if dates are in the future or at least today
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            throw new InvalidDateRangeException("Start date and end date must not be in the past.");
        }
    }
}
