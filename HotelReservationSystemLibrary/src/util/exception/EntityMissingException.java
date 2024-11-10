/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.exception;

/**
 *
 * @author timothy
 */
public class EntityMissingException extends Exception {
    
    public EntityMissingException()
    {
    }
    
    public EntityMissingException(String msg)
    {
        super(msg);
    }
}
