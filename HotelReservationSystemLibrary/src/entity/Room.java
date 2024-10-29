/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author timothy
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Room.findAvailableRoomsForRoomTypeAndDate",
        query = "SELECT r FROM Room r WHERE r.roomType = :roomType AND (r.currentBooking IS NULL)"
//        query = "SELECT r FROM Room r WHERE r.roomType = :roomType AND " +
//                "(r.currentBooking IS NULL OR r.currentBooking.endDate <= :date)" // TODO: make date comparison work
    )
})
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "RoomNumber cannot be null")
    @Pattern(regexp = "\\d{4}", message = "Room number must be a 4-digit number")
    @Column(unique = true, nullable = false)
    private String roomNumber;

    @NotNull(message = "RoomType cannot be null")
    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @OneToOne
    @JoinColumn
    private Booking currentBooking; // alloc: get avail rooms, set allocroom in booking, upon chkin edit this currBooking
    
    @NotNull(message = "Disabled flag cannot be null")
    @Column(nullable = false)
    private boolean disabled; // todo: add getter setter etc!!!

    // No-args constructor
    public Room() {
    }

    // Constructor with parameters
    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    
    public Booking getCurrentBooking() {
        return currentBooking;
    }

    public void setCurrentBooking(Booking currentBooking) {
        this.currentBooking = currentBooking;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Room)) {
            return false;
        }
        Room other = (Room) object;
        return !((this.id == null && other.id != null) || 
                 (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entity.Room[ id=" + id + ", roomNumber=" + roomNumber + " ]";
    }
}
