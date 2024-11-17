/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *
 * @author timothy
 */
@Entity
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "RoomNumber cannot be null")
    @Pattern(regexp = "\\d{4}", message = "Room number must be a 4-digit number")
    @Column(unique = true, nullable = false, length = 4)
    private String roomNumber;

    @NotNull(message = "RoomType cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @OneToOne
    @JoinColumn
    private Booking currentBooking; // set on start date at 12 noon till end date at 12 noon
   
    @OneToOne
    @JoinColumn
    private Booking expectedBooking; // allocated for current day at 2am and removed at 12noon
    
    @NotNull(message = "Disabled flag cannot be null")
    @Column(nullable = false)
    private Boolean disabled = false;
    
    @NotNull(message = "Available flag cannot be null")
    @Column(nullable = false)
    private Boolean available = true;

    // No-args constructor
    public Room() {
    }

    // Constructor with parameters
    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        // available true by default
    }
    
    public Room(String roomNumber, RoomType roomType, boolean available) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
    }
    
    public void updateBookingsAtCheckoutTime(LocalDate date) { // if currentBooking.endDate>= TODAY, replace it with room.getExpectedBooking(), set expected to null
        if (currentBooking == null ||
                (currentBooking != null 
                && (currentBooking.getEndDate().isAfter(date) || currentBooking.getEndDate().isEqual(date)))) {
            currentBooking = expectedBooking;
            expectedBooking = null;
        }
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
    
    public Booking getExpectedBooking() {
        return expectedBooking;
    }

    public void setExpectedBooking(Booking expectedBooking) {
        this.expectedBooking = expectedBooking;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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
