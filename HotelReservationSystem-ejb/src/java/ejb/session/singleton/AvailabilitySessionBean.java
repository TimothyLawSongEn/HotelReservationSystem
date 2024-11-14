/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.BookingEntitySessionBeanLocal;
import ejb.session.stateless.RoomEntitySessionBeanLocal;
import ejb.session.stateless.RoomTypeEntitySessionBeanLocal;
import entity.Booking;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import util.dto.RoomCount;
import util.exception.EntityMissingException;
import util.exception.InvalidDateRangeException;

/**
 *
 * @author timothy
 */
@Singleton
public class AvailabilitySessionBean implements AvailabilitySessionBeanRemote, AvailabilitySessionBeanLocal {

    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    @EJB
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;
    @EJB
    BookingEntitySessionBeanLocal bookingEntitySessionBeanLocal;

    Map<Long, Map<LocalDate, Integer>> roomTypeToBookedCountMap = new HashMap<>(); // map<roomtype id, map<date, bookedcount>>
    
    // caller: DataInitSessionBean postConstruct
    @Override
    public void loadRoomTypesAndBookings() { // TODO: can call this at postconstruct instead, since datainit instantiated at start while availability instantiated later
        initializeRoomTypeToBookedCountMap();
        populateBookingsIntoMap();
    }
    
    private void initializeRoomTypeToBookedCountMap() {
        List<RoomType> roomTypes = roomTypeEntitySessionBeanLocal.findAllRoomTypes();
        for (RoomType roomType : roomTypes) {
            Long roomTypeId = roomType.getId();
            roomTypeToBookedCountMap.put(roomTypeId, new HashMap<>());
        }
    }
    
    private void populateBookingsIntoMap() {
        List<Booking> bookings = bookingEntitySessionBeanLocal.getAllBookings();
        LocalDate today = LocalDate.now();

        for (Booking booking : bookings) {
            Long roomTypeId = booking.getRoomType().getId();
            LocalDate startDate = booking.getStartDate();
            LocalDate endDate = booking.getEndDate();

            if (!endDate.isAfter(today)) {
                continue;
            }

            LocalDate effectiveStartDate = startDate.isBefore(today) ? today : startDate;
            for (LocalDate date = effectiveStartDate; date.isBefore(endDate) ; date = date.plusDays(1)) {
                Map<LocalDate, Integer> bookedCountMap = roomTypeToBookedCountMap.get(roomTypeId);
                bookedCountMap.put(date, bookedCountMap.getOrDefault(date, 0) + 1);
            }
        }
    }
    

    // caller: client during search rooms
    @Override
    public List<RoomCount> getAvailableRoomTypesWithCount(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        validateBookingDateRange(startDate, endDate);
        List<RoomCount> availableRoomTypes = new ArrayList<>();

        // Get all room types
        List<RoomType> roomTypes = roomTypeEntitySessionBeanLocal.findAllRoomTypes();
        List<Long> roomTypeIds = roomTypes.stream()
            .map(RoomType::getId)
            .collect(Collectors.toList());

        // Iterate over all room types
        for (Long roomTypeId : roomTypeIds) {
            roomTypeToBookedCountMap.computeIfAbsent(roomTypeId, k -> new HashMap<>()); // add roomtypeid into map if absent
            Map<LocalDate, Integer> bookedCountMap = roomTypeToBookedCountMap.get(roomTypeId);

            int roomCount = roomEntitySessionBeanLocal.getNonDisabledRoomCountForRoomType(roomTypeId);
            int maxOccupied = 0;

            // Fetch RoomType entity using roomTypeId
            RoomType roomType = roomTypeEntitySessionBeanLocal.findRoomType(roomTypeId);
            if (roomType == null) {
                continue;  // If room type not found, skip to next iteration
            }

            for (LocalDate date = startDate; date.isBefore(endDate) ; date = date.plusDays(1)) {
                int occupiedCount = bookedCountMap.getOrDefault(date, 0);
                maxOccupied = Math.max(maxOccupied, occupiedCount);

                if (occupiedCount >= roomCount) {
                    maxOccupied = roomCount;
                    break; // Fully occupied, stop checking further dates for this room type
                }
            }

            if (maxOccupied < roomCount) {
//                availableRoomTypes.add(new Pair<>(roomType, roomCount - maxOccupied));
                availableRoomTypes.add(new RoomCount(roomType, roomCount - maxOccupied));
            }
        }
        return availableRoomTypes;
    }

    
    // caller: bookingBean when booking is created
    // must be atomic
    @Override
    @Lock(LockType.WRITE)
    public void incrementBookedCount(LocalDate startDate, LocalDate endDate, long roomTypeId) throws Exception {  
        validateBookingDateRange(startDate, endDate);
        // chk that roomtypeid can be found via roomtypebean
        RoomType roomType = roomTypeEntitySessionBeanLocal.findRoomType(roomTypeId);
        if (roomType == null) {
            throw new Exception("RoomType with ID " + roomTypeId + " not found.");
        }
        
        roomTypeToBookedCountMap.computeIfAbsent(roomTypeId, k -> new HashMap<>()); // add roomtypeid into map if absent
        Map<LocalDate, Integer> roomTypeBookingsMap = roomTypeToBookedCountMap.get(roomTypeId);

        int roomCount = roomEntitySessionBeanLocal.getNonDisabledRoomCountForRoomType(roomTypeId);

        // Store unavailable dates if any
        List<LocalDate> unavailableDates = new ArrayList<>();

        // First check if all dates are available
        for (LocalDate date = startDate; date.isBefore(endDate) ; date = date.plusDays(1)) {
            int bookedCount = roomTypeBookingsMap.getOrDefault(date, 0);

            if (bookedCount >= roomCount) {
                unavailableDates.add(date);
            }
        }

        if (!unavailableDates.isEmpty()) {
            throw new Exception("Not enough rooms available on the following dates: " + unavailableDates);
        }

        // If all dates are available, increment the counts
        for (LocalDate date = startDate; date.isBefore(endDate) ; date = date.plusDays(1)) {
            int bookedCount = roomTypeBookingsMap.getOrDefault(date, 0);
            roomTypeBookingsMap.put(date, bookedCount + 1);
        }
    }
    
    private void validateBookingDateRange(LocalDate startDate, LocalDate endDate) throws InvalidDateRangeException {
        // 1. Check if startDate or endDate are null
        if (startDate == null || endDate == null) {
            throw new InvalidDateRangeException("Start date and end date cannot be null.");
        }

        // 2. Check if endDate is after startDate
        if (!endDate.isAfter(startDate)) {
            throw new InvalidDateRangeException("End date must be after the start date.");
        }

        // 3. Optional: Check if dates are in the future or at least today
        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(LocalDate.now())) {
            throw new InvalidDateRangeException("Start date and end date must not be in the past.");
        }
    }
    
    @Override
    public double calculateReservationFee(Long roomTypeId, LocalDate startDate, LocalDate endDate) throws EntityMissingException {
        RoomType roomType = roomTypeEntitySessionBeanLocal.findRoomType(roomTypeId);
        if (roomType == null) {
            throw new EntityMissingException("Room type not found");
        }
        return roomType.calculateTotalGuestReservationFee(startDate, endDate);
    }
    
}
