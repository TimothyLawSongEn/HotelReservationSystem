/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author timothy
 */
public class InvalidDateRangeException extends Exception {
    
    public InvalidDateRangeException()
    {
    }
    
    public InvalidDateRangeException(String msg)
    {
        super(msg);
    }
}
