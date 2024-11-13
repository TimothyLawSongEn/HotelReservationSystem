/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import javax.validation.ConstraintViolationException;

/**
 *
 * @author clara
 */
@Local
public interface EmployeeEntitySessionBeanLocal {

    public Employee createEmployee(Employee newEmployee) throws ConstraintViolationException;

    public Employee.EmployeeType logIn(String username, String password);

    public List<Employee> viewAllEmployees();
    
}
