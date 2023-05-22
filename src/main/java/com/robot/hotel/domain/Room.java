package com.robot.hotel.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
