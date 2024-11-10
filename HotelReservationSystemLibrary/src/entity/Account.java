/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author clara
 */
@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Username cannot be null")
    @Column(nullable = false, unique = true)
    private String username;
    
    @NotNull(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;
    
    @NotNull(message = "Email cannot be null")
    @Column(nullable = false)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Employee Type cannot be null")
    @Column(nullable = false)
    private AccountType accountType;
    
//    @OneToMany(mappedBy = "guest")
//    private List<Booking> bookings = new ArrayList<>();

    public Account() {
    }

    // Constructor for Guest
    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = AccountType.GUEST;
//        this.bookings = new ArrayList<>();
    }

    // Constructor for Partner
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
        this.accountType = AccountType.PARTNER;
    }

    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
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
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Guest[ id=" + id + " ]";
    }
    
    public enum AccountType {
        GUEST,
        PARTNER,
    }
    
}
