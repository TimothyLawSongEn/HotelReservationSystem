package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author timothy
 */
@Entity
public class RoomType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key
    private Long id;

    @NotNull(message = "RoomType name cannot be null")
    @Size(min = 1, max = 50, message = "RoomType name must be between 1 and 50 characters")
    @Column(unique = true, nullable = false, length = 50)  // Unique and Non-nullable Name
    private String name;

    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    @NotNull(message = "Published rate cannot be null")
    @Column(nullable = false, precision = 11, scale = 2) // Mandatory published rate
    private Double publishedRate;

    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2)
    @NotNull(message = "Normal rate cannot be null")
    @Column(nullable = false, precision = 11, scale = 2)  // Mandatory normal rate
    private Double normalRate;

    @OneToMany(mappedBy = "roomType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<RoomRate> rates = new ArrayList<>();  // List for peak and promo rates
    
    @OneToOne
    @JoinColumn
    private RoomType nextHigherRoomType;
    
    @NotNull(message = "Disabled flag cannot be null")
    @Column(nullable = false)
    private Boolean disabled = false;
    
    @OneToOne(mappedBy = "roomType", cascade = {CascadeType.ALL})
    private RoomCount roomCount;
    
//    @OneToMany(mappedBy = "name")
//    private RoomType roomType;

    // No-args constructor
    public RoomType() {}

    // Constructor
    public RoomType(String name, Double publishedRate, Double normalRate) {
        this.name = name;
        this.publishedRate = publishedRate;
        this.normalRate = normalRate;
        this.disabled = false;
    }
    
    public double calculateTotalWalkinFee(LocalDate startDate, LocalDate endDate) {
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return days * publishedRate;
    }
    
//    The total reservation fee payable by the guest for a reservation is calculated by summing the
//    prevailing rate per night of each night of stay for the entire duration of stay. For example, if a
//    guest books a Deluxe Room for 3 nights, the reservation fee will be the sum of first day’s rate
//    per night, second day’s rate per night and third day’s rate per night. If either peak rate or
//    promotion rate is defined for a particular room type on a particular date, it will take
//    precedence over the normal rate. If both peak rate and promotion rate are defined for a
//    particular room type on a particular date, the promotion rate will take precedence.
    // Calculate the total reservation fee with precedence: promo > peak > normal rate
    public double calculateTotalGuestReservationFee(LocalDate startDate, LocalDate endDate) {
        double totalFee = 0.0;

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            Double dailyRate = normalRate;
            for (RoomRate rate : rates) {
                if (!rate.getDisabled() && rate.isWithinPeriod(date)) {
                    if (rate.getSpecialRateType().equals(RoomRate.SpecialRateType.PROMO)) {
                        dailyRate = rate.getAmount();
                        break;  // Promo rate has the highest precedence
                    } else if (rate.getSpecialRateType().equals(RoomRate.SpecialRateType.PEAK)) {
                        dailyRate = rate.getAmount();
                    }
                }
            }
            totalFee += dailyRate;
        }

        return totalFee;
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

    public Double getPublishedRate() {
        return publishedRate;
    }

    public void setPublishedRate(Double publishedRate) {
        this.publishedRate = publishedRate;
    }

    public Double getNormalRate() {
        return normalRate;
    }

    public void setNormalRate(Double normalRate) {
        this.normalRate = normalRate;
    }

    public List<RoomRate> getRates() {
        return rates;
    }

    public void addRates(RoomRate rate) {
        this.rates.add(rate);
    }
    
    public RoomType getNextHigherRoomType() {
        return nextHigherRoomType;
    }

    public void setNextHigherRoomType(RoomType nextHigherRoomType) {
        this.nextHigherRoomType = nextHigherRoomType;
    }
    
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    // Equals and Hashcode based on id (Primary Key)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        RoomType other = (RoomType) object;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return String.format(
            "RoomType [ID: %d, Name: %s, Normal Rate: %.2f, Published Rate: %.2f]",
            id, name, normalRate, publishedRate
        );
    }
}
