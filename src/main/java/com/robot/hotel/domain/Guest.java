package com.robot.hotel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String dateOfBirth;
    @Column
    private String gender;
    @Column
    private String passportNumber;

    @ManyToOne
    @JoinColumn(name="room_number")
    private Room room;
}
