/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Stateless
public class EmployeeEntitySessionBean implements EmployeeEntitySessionBeanRemote, EmployeeEntitySessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PersistenceContext(unitName = "HotelReservationSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Employee createEmployee(Employee newEmployee) throws ConstraintViolationException {
        em.persist(newEmployee);
        return newEmployee;
    }
    
    @Override
    public Employee.EmployeeType logIn(String username, String password) {
        Employee employee = findEmployeeByUsername(username);
        if (employee != null && employee.getPassword().equals(password)) {
            return employee.getEmployeeType();
        }
        return null;
    }
    
    @Override
    public List<Employee> viewAllEmployees() {
        TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e", Employee.class);
        return query.getResultList();
    }
    
    private Employee findEmployeeByUsername(String username) {
        return em.createQuery("SELECT e FROM Employee e WHERE e.username = :username", Employee.class)
                .setParameter("username", username)
                .getSingleResult();
    }
}
