/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.RoomCount;
import javax.ejb.Local;

/**
 *
 * @author clara
 */
@Local
public interface RoomCountEntitySessionBeanLocal {

    public Long createNewRoomCount(RoomCount roomCount);
    
}
