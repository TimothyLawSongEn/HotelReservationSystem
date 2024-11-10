/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author timothy
 */
public class BookingAlreadyCheckedInException extends Exception {
    
    public BookingAlreadyCheckedInException()
    {
    }
    
    public BookingAlreadyCheckedInException(String msg)
    {
        super(msg);
    }
}
