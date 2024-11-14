/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util.dto;

import entity.RoomType;
import java.io.Serializable;

/**
 *
 * @author clara
 */
public class RoomCount implements Serializable {
    private static final long serialVersionUID = 1L;
    private RoomType roomType;
    private int count;

    public RoomCount() {
    }

    public RoomCount(RoomType roomType, int count) {
        this.roomType = roomType;
        this.count = count;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return "RoomType: " + this.roomType + ", Count: " + count;
    }
}
