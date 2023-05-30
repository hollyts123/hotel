package com.robot.hotel.rest;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Room;
import com.robot.hotel.domain.Reservation;
import com.robot.hotel.dto.ReservationDto;
import com.robot.hotel.repository.GuestRepository;
import com.robot.hotel.repository.RoomRepository;
import com.robot.hotel.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationDto>> findAll() {
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationDto> findById(@PathVariable("id") Long id) {
        return reservationService.findById(id)
                .map(reservation -> ResponseEntity.ok(ReservationService.buildReservationDto(reservation)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/reservations/findByCheckinDate/{checkinDate}")
    public ResponseEntity<List<ReservationDto>> findByCheckinDate(@PathVariable LocalDate checkinDate) {
        List<ReservationDto> reservations = reservationService.findByCheckinDate(checkinDate).stream()
                .map(ReservationService::buildReservationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @PostMapping("/reservations/guestIds/{guestIds}")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation, @PathVariable List<Long> guestIds) {
        List<Guest> guests = guestRepository.findAllById(guestIds);
        if (guests.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid guest IDs");
        }

        Room room = reservation.getRoom();
        reservation.setGuests(guests);

        Optional<Room> optionalRoom = roomRepository.findById(room.getId());
        if (optionalRoom.isPresent()) {
            Room foundRoom = optionalRoom.get();
            reservation.setRoom(foundRoom);

            reservationService.createReservation(reservation, guestIds, foundRoom.getId());
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
