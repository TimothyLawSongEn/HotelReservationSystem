package entity;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @Size(min = 1, max = 50, message = "RoomRate name must be between 1 and 50 characters")
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    @Column(nullable = false, precision = 11, scale = 2) // TODO: might want to use bigdecimal with precision and scale
    private Double amount;

    @NotNull(message = "Start date cannot be null")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Column(nullable = false)
    private LocalDate endDate;
    
    @AssertTrue(message = "StartDate must not be before EndDate")
    public boolean isValidDateRange() { return !startDate.isAfter(endDate);}

    @Enumerated(EnumType.STRING)
    private SpecialRateType specialRateType;  // e.g., Peak, Promo

    // needed in management client
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RoomType roomType;
    
    @NotNull(message = "Disabled flag cannot be null")
    @Column(nullable = false)
    private Boolean disabled = false;

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
    
    public boolean isWithinPeriod(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
               (date.isEqual(endDate) || date.isBefore(endDate));
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
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
