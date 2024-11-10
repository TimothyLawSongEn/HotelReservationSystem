/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author timothy
 */
public class BookingNoAllocatedRoomException extends Exception {
    
    public BookingNoAllocatedRoomException()
    {
    }
    
    
    
    public BookingNoAllocatedRoomException(String msg)
    {
        super(msg);
    }
}
