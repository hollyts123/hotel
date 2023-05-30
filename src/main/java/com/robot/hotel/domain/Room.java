package com.robot.hotel.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@Table
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int roomNumber;
    @Column
    private String roomType;
    @Column
    private double pricePerNight;
    @Column
    private int maxNumberOfGuests;
    @Column
    private boolean isAvailable;

    @OneToMany(mappedBy = "room")
    private List<Guest> guests;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
