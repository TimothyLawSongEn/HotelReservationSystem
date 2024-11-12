/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author timothy
 */
public class WrongAccountTypeException extends Exception {
    
    public WrongAccountTypeException()
    {
    }
    
    public WrongAccountTypeException(String msg)
    {
        super(msg);
    }
}
