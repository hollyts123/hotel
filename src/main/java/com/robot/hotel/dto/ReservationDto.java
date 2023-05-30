package com.robot.hotel.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
public class ReservationDto {
    private Long id;

    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String status;
    private List<GuestDto> guests;
    private RoomDto room;
}
