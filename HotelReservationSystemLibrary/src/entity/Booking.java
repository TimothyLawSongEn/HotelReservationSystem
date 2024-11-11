/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author timothy
 */
@Entity
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "StartDate cannot be null")
    @Column(nullable = false)
    private LocalDate startDate; // todo: localdate vs date??? //NOVALUEINXML
    @NotNull(message = "EndDate cannot be null")
    @Column(nullable = false)
    private LocalDate endDate; //NOVALUEINXML
 
    @NotNull(message = "RoomType cannot be null")
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType; //GOTVALUEINXML

    @ManyToOne
    @JoinColumn
    private Room allocatedRoom; // can be null, until alloc() is called
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Guest guest; //NOVALUEINXML
    
    @NotNull(message = "CheckedIn cannot be null")
    @Column(nullable = false)
    private Boolean checkedIn = false; //GOTVALUEINXML

    public Booking() {
    }

    public Booking(LocalDate startDate, LocalDate endDate, RoomType roomType, Guest guest) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomType = roomType;
        this.guest = guest;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Room getAllocatedRoom() {
        return allocatedRoom;
    }

    public void setAllocatedRoom(Room allocatedRoom) {
        this.allocatedRoom = allocatedRoom;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
    
    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", roomType=" + (roomType != null ? roomType.getName() : "null") +
                ", allocatedRoom=" + (allocatedRoom != null ? allocatedRoom.getRoomNumber() : "null") +
                ", guest=" + (guest != null ? guest.getId() : "null") +
                '}';
    }

}
