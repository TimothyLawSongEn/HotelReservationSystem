/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import ejb.session.singleton.AvailabilitySessionBeanLocal;
import entity.Booking;
import entity.Guest;
import entity.Room;
import entity.RoomType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    GuestEntitySessionBeanLocal guestEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;

    public BookingEntitySessionBean() {
    }
    
    // TODO: set rollback with child createBooking method
    @Override
    public Booking reserveRoomType(LocalDate startDate, LocalDate endDate, long roomTypeId, long guestId) throws Exception {
        // Increment booked count for the given room type and dates
        try {
            availabilitySessionBeanLocal.incrementBookedCount(startDate, endDate, roomTypeId);
        } catch (Exception e) {
            throw new Exception("Failed to increment booked count: " + e.getMessage());
        }

        // Create and persist the new booking
        return createBooking(startDate, endDate, roomTypeId, guestId);
    }

    private Booking createBooking(LocalDate startDate, LocalDate endDate, long roomTypeId, long guestId) throws Exception {
        Guest guest = guestEntitySessionBeanLocal.findGuestById(guestId);
        if (guest == null) {
            throw new Exception("Guest not found.");
        }

        RoomType roomType = roomTypeEntitySessionBeanLocal.findRoomType(roomTypeId); // Implement this method to find a room type by ID
        if (roomType == null) {
            throw new Exception("Room type not found.");
        }

        Booking newBooking = new Booking(startDate, endDate, roomType, guest);
        em.persist(newBooking);
        return newBooking;
    }

//    @Override
//    public Booking createBooking(Booking newBooking) throws ConstraintViolationException {
//        em.persist(newBooking);
//        return newBooking;
//    }

    public List<Booking> getBookingByGuest(Guest guest) {
        return em.createQuery("SELECT b FROM Booking b WHERE b.guest = :guest", Booking.class)
                .setParameter("guest", guest)
                .getResultList();
    }
    
    @Override
    public Booking getBookingById(Long id) {
        return em.find(Booking.class, id);
    }
    
    @Override
    public List<Booking> findAllBookings() {
        Query query = em.createQuery("SELECT b FROM Booking b");
        return query.getResultList();
    }
    
    private List<Booking> findBookingsByRoomTypeAndStartDate(RoomType roomType, LocalDate startDate) {
        // Query to find bookings for the given room type that start on the specified date.
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.roomType = :roomType AND b.startDate = :startDate", Booking.class)
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
    
    // scheduled
    // chk which rooms clear beforehand
//    alloc: get avail rooms, set allocroom in booking, upon chkin edit this currBooking    
    @Override
    public List<Booking> allocateRoomToBookings(LocalDate date) {
        // for each roomtype
        // 1) get avail rooms for that roomtype (room.currBooking == null || room.currBooking.endDate <= date)
        // 2) get bookings that start on date for that roomtype 
        // set allocroom field for the booking
        List<RoomType> roomTypes = roomTypeEntitySessionBeanLocal.findAllRoomTypes();
        List<Booking> allBookingsStartingToday = new ArrayList<>();

        for (RoomType roomType : roomTypes) {
            // Step 1: Get available rooms for this room type
            List<Room> availableRooms = roomEntitySessionBeanLocal.getAvailableRoomsForRoomTypeAndDate(roomType, date);

            // Step 2: Get bookings for this room type that start on the given date
            List<Booking> bookingsStartingToday = findBookingsByRoomTypeAndStartDate(roomType, date);

            // Allocate rooms to bookings
            Iterator<Room> roomIterator = availableRooms.iterator();
            for (Booking booking : bookingsStartingToday) {
                if (roomIterator.hasNext()) {
                    Room room = roomIterator.next();
                    booking.setAllocatedRoom(room); // Set the allocated room for the booking
                    updateBooking(booking); // Save booking with the allocated room
                } else {
                    // No more available rooms for this room type; any remaining bookings will be unallocated
                    // save into exception report (dont implement yet)
                    break;
                }
            }
            allBookingsStartingToday.addAll(bookingsStartingToday);
        }
        return allBookingsStartingToday;
    }
    
    public void checkIn(Long bookingId) throws Exception {
        Booking booking = em.find(Booking.class, bookingId);
        if (booking == null || booking.isCheckedIn()) {
            throw new Exception("Booking not found or already checked in.");
        }

        Room allocatedRoom = booking.getAllocatedRoom();
        if (allocatedRoom == null) {
            throw new Exception("Allocated room not found for this booking.");
        }

        // Mark booking as checked in and update room’s current booking
        booking.setCheckedIn(true);
        allocatedRoom.setCurrentBooking(booking);

        em.merge(booking);
        em.merge(allocatedRoom);
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
        allocatedRoom.setCurrentBooking(null);

        em.merge(booking);
        em.merge(allocatedRoom);
    }
    
    @Override
    public List<Booking> retrieveActiveBookingsForCheckIn(Long guestId) {
        LocalDate today = LocalDate.now();
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.guest.id = :guestId AND b.checkedIn = false " +
            "AND b.startDate <= :today AND :today < b.endDate",
            Booking.class
        )
        .setParameter("guestId", guestId)
        .setParameter("today", today)
        .getResultList();
    }

    @Override
    public List<Booking> retrieveCheckedInBookings(Long guestId) {
        LocalDate today = LocalDate.now();
        return em.createQuery(
            "SELECT b FROM Booking b WHERE b.guest.id = :guestId AND b.checkedIn = true " +
            "AND b.startDate <= :today AND :today <= b.endDate",
            Booking.class
        )
        .setParameter("guestId", guestId)
        .setParameter("today", today)
        .getResultList();
    }

}
