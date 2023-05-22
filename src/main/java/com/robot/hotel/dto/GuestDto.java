package com.robot.hotel.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GuestDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String passportNumber;
}
