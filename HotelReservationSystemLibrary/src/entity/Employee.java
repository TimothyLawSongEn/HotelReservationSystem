/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author clara
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Username cannot be null")
    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 1, max = 20, message = "Username must be between 1 and 20 characters")
    private String username;
        
    @NotNull(message = "Password cannot be null")
    @Column(nullable = false)
    private String password;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Employee Type cannot be null")
    @Column(nullable = false)
    private EmployeeType employeeType;

    public Employee() {
    }

    public Employee(String username, String password, EmployeeType employeeType) {
        this.username = username;
        this.password = password;
        this.employeeType = employeeType;
    }
    
    public Long getId() {
        return id;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + id + " ]";
    }
    
    public enum EmployeeType {
        SYSTEMADMIN,
        OPSMANAGER,
        SALESMANAGER,
        GUESTRELATIONOFFICER,
        ALLACCESS
    }
    
}
