package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author timothy
 */
@Entity
public class RoomRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Name cannot be null")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Amount cannot be null")
    @Column(nullable = false) // TODO: might want to use bigdecimal with precision and scale
    private Double amount;

    @NotNull(message = "Start date cannot be null")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private SpecialRateType specialRateType;  // e.g., Peak, Promo

    // Optional: ManyToOne relationship to RoomType
    @ManyToOne
    @JoinColumn(nullable = false)
    private RoomType roomType;  // Relationship to RoomType

    // No-args constructor
    public RoomRate() {
    }

    // Constructor with parameters
    public RoomRate(String name, Double amount, LocalDate startDate, LocalDate endDate, SpecialRateType specialRateType, RoomType roomType) {
        this.name = name;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.specialRateType = specialRateType;
        this.roomType = roomType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

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

    public SpecialRateType getSpecialRateType() {
        return specialRateType;
    }

    public void setSpecialRateType(SpecialRateType specialRateType) {
        this.specialRateType = specialRateType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Override
    public int hashCode() {
        return (id != null) ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RoomRate)) return false;
        RoomRate other = (RoomRate) object;
        return (id != null && id.equals(other.id));
    }

    @Override
    public String toString() {
        return "entity.RoomRate[ id=" + id + ", amount=" + amount + ", specialRateType=" + specialRateType + " ]";
    }

    public enum SpecialRateType {
        PEAK,
        PROMO
    }
}
