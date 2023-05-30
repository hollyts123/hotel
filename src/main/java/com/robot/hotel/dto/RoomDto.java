package com.robot.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomDto {
    private Long id;

    private int roomNumber;
    private String roomType;
    private double pricePerNight;
    private int maxNumberOfGuests;
    private boolean isAvailable;
}
