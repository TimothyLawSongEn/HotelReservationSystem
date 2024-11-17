/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.singleton.AvailabilitySessionBeanLocal;
import entity.Booking;
import entity.Account;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.util.Pair;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import util.exception.BookingAlreadyCheckedInException;
import util.exception.BookingNoAllocatedRoomException;
import util.exception.EntityMissingException;

/**
 *
 * @author timothy
 */
@Stateless
public class BookingEntitySessionBean implements BookingEntitySessionBeanRemote, BookingEntitySessionBeanLocal {

    @EJB
    AvailabilitySessionBeanLocal availabilitySessionBeanLocal;
    @EJB
    RoomTypeEntitySessionBeanLocal roomTypeEntitySessionBeanLocal;
    @EJB
    RoomEntitySessionBeanLocal roomEntitySessionBeanLocal;
    @EJB
    AccountEntitySessionBeanLocal guestEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public BookingEntitySessionBean() {
    }
    
    // TODO: set rollback with child createBooking method
    @Override
    public List<Booking> reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, int numRooms, long accountId) throws Exception {
        // Increment booked count for the given room type and dates
        try {
            availabilitySessionBeanLocal.incrementBookedCount(startDate, endDate, roomTypeId, numRooms);
        } catch (Exception e) {
            throw new Exception("Failed to increment booked count: " + e.getMessage());
        }
        
        List<Booking> bookings = new ArrayList<>();
        for (int i = 0 ; i < numRooms ; i++) {
            // Create and persist the new booking
            Booking booking = createBooking(startDate, endDate, roomTypeId, accountId);

            // if same day after 2am, allocate room immediately!!
            LocalDate today = LocalDate.now(); LocalTime currentTime = LocalTime.now();
            LocalTime twoAM = LocalTime.of(2, 0); // 2:00 AM
            if (booking.getStartDate().isEqual(today) && currentTime.isAfter(twoAM)) {
                allocateRoomToBooking(booking);
            }
            
            bookings.add(booking);
        }

        return bookings;
    }

    private Booking createBooking(LocalDate startDate, LocalDate endDate, long roomTypeId, long accountId) throws Exception {
        Account account = guestEntitySessionBeanLocal.findGuestById(accountId);
        if (account == null) {
            throw new Exception("Guest not found.");
        }

        RoomType roomType = roomTypeEntitySessionBeanLocal.findRoomType(roomTypeId); // Implement this method to find a room type by ID
        if (roomType == null) {
            throw new Exception("Room type not found.");
        }

        Booking newBooking = new Booking(startDate, endDate, roomType, account);
        em.persist(newBooking);
        return newBooking;
    }

    @Override
    public List<Booking> getBookingsByAccountId(Long accountId) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.account.id = :accountId", Booking.class)
                .setParameter("accountId", accountId)
                .getResultList();
    }
    
    @Override
    public Booking getBookingById(Long id) {
        return em.find(Booking.class, id);
    }
    
    @Override
    public List<Booking> getAllBookings() {
        Query query = em.createQuery("SELECT b FROM Booking b");
        return query.getResultList();
    }
    
    private List<Booking> findUnallocatedBookingsByRoomTypeAndStartDate(RoomType roomType, LocalDate startDate) {
        // Query to find bookings for the given room type that start on the specified date.
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.allocatedRoom IS NULL AND b.roomType = :roomType AND b.startDate = :startDate", Booking.class)
            .setParameter("roomType", roomType)
            .setParameter("startDate", startDate)
            .getResultList();
    }
    
    private Booking updateBooking(Booking booking) {
        Booking origBooking = em.find(Booking.class, booking.getId());
        if (origBooking == null) {
            System.out.println("Programming error: BookingId " + booking.getId() + " cannot be found. Did not update!");
            return null;
        }
        em.merge(booking);  // Update the room in the database
        return booking;
    }
    
    @Override
    public List<Booking> allocateRoomToBookings(LocalDate date) {
        // for each roomtype
        // 1) get avail rooms for that roomtype (room.currBooking == null || room.currBooking.endDate <= date)
        // 2) get bookings that start on date for that roomtype 
        // set allocroom field for the booking
        List<RoomType> roomTypes = roomTypeEntitySessionBeanLocal.findAllRoomTypes();
        List<Booking> allBookingsStartingToday = new ArrayList<>();
        List<Booking> failedAllocBookings = new ArrayList<>();

        for (RoomType roomType : roomTypes) {
            // Step 1: Get available rooms for this room type
            List<Room> availableRooms = roomEntitySessionBeanLocal.getAvailableNonDisabledRoomsForRoomTypeAndDate(roomType, date);

            // Step 2: Get bookings for this room type that start on the given date
            List<Booking> unallocatedBookingsStartingToday = findUnallocatedBookingsByRoomTypeAndStartDate(roomType, date);

            // Allocate rooms to bookings
            Iterator<Room> roomIterator = availableRooms.iterator();
            for (Booking booking : unallocatedBookingsStartingToday) {
                if (roomIterator.hasNext()) {
                    Room room = roomIterator.next();
                    associateBookingAndRoom(booking, room);
                } else {
                    // No more available rooms for this room type; any remaining bookings will be unallocated
                    // save into exception report (dont implement yet)
                    failedAllocBookings.add(booking);
//                    break;
                }
            }
            allBookingsStartingToday.addAll(unallocatedBookingsStartingToday);
        }
        
        for (Booking failedAllocBooking : failedAllocBookings) {
            allocateHigherRoomToBooking(failedAllocBooking);
        }
        return allBookingsStartingToday;
    }
    
    private void allocateHigherRoomToBooking(Booking booking) {
        RoomType nextHigherRoomType = booking.getRoomType().getNextHigherRoomType();
        if (nextHigherRoomType == null) {
            return;
        }
        List<Room> availableHigherRooms = roomEntitySessionBeanLocal.getAvailableNonDisabledRoomsForRoomTypeAndDate(
            nextHigherRoomType, booking.getStartDate()
        );

        if (availableHigherRooms.isEmpty()) {
            return;
        }
        Room room = availableHigherRooms.get(0);
        associateBookingAndRoom(booking, room);
    }

    private Booking allocateRoomToBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking is null when trying to allocate a room");
        }

        List<Room> availableRooms = roomEntitySessionBeanLocal.getAvailableNonDisabledRoomsForRoomTypeAndDate(
            booking.getRoomType(), booking.getStartDate()
        );

        if (availableRooms.isEmpty()) {
            throw new IllegalStateException("No available rooms for the specified room type and date.");
        }
        Room room = availableRooms.get(0);
        associateBookingAndRoom(booking, room);

        return booking;
    }
    
    private void associateBookingAndRoom(Booking booking, Room room) {
        booking.setAllocatedRoom(room); // Set the allocated room for the booking
        updateBooking(booking); // Save booking with the allocated room
        room.setExpectedBooking(booking);
        roomEntitySessionBeanLocal.updateRoom(room);
    }
    
//    • Generate a report showing two types of room allocation
//    exception.
//    • First type is no available room for reserved room type but
//    upgrade to next higher room type is available – A room is
//    automatically allocated by the system.
//    • Second type is no available room for reserved room type
//    and no upgrade to next higher room type is available – No
//    room is automatically allocated by the system.
    public Pair<List<Booking>, List<Booking>> getBookingsWithRoomAllocExceptionForLastAlloc() {
        LocalDate date;
        // Check if current time is between 12am and 2am
        if (LocalTime.now().isAfter(LocalTime.MIDNIGHT) && LocalTime.now().isBefore(LocalTime.of(2, 0))) {
            date = LocalDate.now().minusDays(1);  // Yesterday’s date
        } else {
            date = LocalDate.now();  // Today’s date
        }
        return getBookingsWithRoomAllocException(date);
    }
    
    public Pair<List<Booking>, List<Booking>> getBookingsWithRoomAllocException(LocalDate date) {
        List<Booking> bookingsWoRoom = getBookingsWithoutRoom(date);
        List<Booking> bookingsWHigherRoom = getBookingsWithHigherRoom(date);

        return new Pair<>(bookingsWoRoom, bookingsWHigherRoom);
    }
    
    private List<Booking> getBookingsWithoutRoom(LocalDate date) {
        return em.createQuery(
            "SELECT b FROM Booking b " +
            "WHERE b.startDate = :date AND b.allocatedRoom IS NULL", Booking.class)
            .setParameter("date", date)
            .getResultList();
    }

    private List<Booking> getBookingsWithHigherRoom(LocalDate date) {
        return em.createQuery(
            "SELECT b FROM Booking b " +
            "WHERE b.startDate = :date AND b.allocatedRoom IS NOT NULL " +
            "AND b.allocatedRoom.roomType != b.roomType", Booking.class)
            .setParameter("date", date)
            .getResultList();
    }

    @Override
    public void checkIn(Long bookingId) throws EntityMissingException, BookingAlreadyCheckedInException, BookingNoAllocatedRoomException {
        Booking booking = em.find(Booking.class, bookingId);
        if (booking == null) {
            throw new EntityMissingException("Booking with ID " + bookingId + " not found when checking in.");
        }
        if (booking.isCheckedIn()) {
            throw new BookingAlreadyCheckedInException();
        }

        Room allocatedRoom = booking.getAllocatedRoom();
        if (allocatedRoom == null) {
            throw new BookingNoAllocatedRoomException();
        }

        // Mark booking as checked in
        booking.setCheckedIn(true);
        em.merge(booking);
    }

    
    public void checkOut(Long bookingId) throws Exception {
        Booking booking = em.find(Booking.class, bookingId);
        if (booking == null || !booking.isCheckedIn()) {
            throw new Exception("Booking not found or not checked in.");
        }

        Room allocatedRoom = booking.getAllocatedRoom();
        if (allocatedRoom == null) {
            throw new Exception("Allocated room not found for this booking.");
        }

        // Mark booking as checked out and release the room
        booking.setCheckedIn(false);
//        allocatedRoom.setCurrentBooking(null);

        em.merge(booking);
//        em.merge(allocatedRoom);
    }
    
    @Override
    public List<Booking> retrieveActiveBookingsForCheckIn(Long accountId) {
        LocalDate today = LocalDate.now();
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.account.id = :accountId AND b.checkedIn = false " +
            "AND b.startDate <= :today AND :today < b.endDate",
            Booking.class
        )
        .setParameter("accountId", accountId)
        .setParameter("today", today)
        .getResultList();
    }

    @Override
    public List<Booking> retrieveCheckedInBookings(Long accountId) {
        LocalDate today = LocalDate.now();
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.account.id = :accountId AND b.checkedIn = true " +
            "AND b.startDate <= :today AND :today <= b.endDate",
            Booking.class
        )
        .setParameter("accountId", accountId)
        .setParameter("today", today)
        .getResultList();
    }
    
    // caller: deleteRoom() in room bean
    @Override
    public boolean existBookingWithRoom(Long roomId) {
        // Construct the query to count bookings with the given room ID
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(b) FROM Booking b WHERE b.allocatedRoom.id = :roomId", Long.class);

        query.setParameter("roomId", roomId);

        Long count = query.getSingleResult();

        return count > 0;
    }

}
