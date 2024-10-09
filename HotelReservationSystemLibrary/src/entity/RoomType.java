/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author timothy
 */
@Entity
public class RoomType implements Serializable {
    
    @Id
    private String name;
    
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    private List<RoomRate> rates;
    
//    @ElementCollection
//    @CollectionTable(name = "published_rate", joinColumns = @JoinColumn(name = "room_type"))
//    private List<RoomRate> publishedRates;
//
//    @ElementCollection
//    @CollectionTable(name = "normal_rate", joinColumns = @JoinColumn(name = "room_type"))
//    private List<RoomRate> normalRates;
//
//    @ElementCollection
//    @CollectionTable(name = "peak_rate", joinColumns = @JoinColumn(name = "room_type"))
//    private List<RoomRate> peakRates;
//
//    @ElementCollection
//    @CollectionTable(name = "promo_rate", joinColumns = @JoinColumn(name = "room_type"))
//    private List<RoomRate> promoRates;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoomType)) {
            return false;
        }
        RoomType other = (RoomType) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RoomType[ name=" + name + " ]";
    }
    
}
