package com.robot.hotel.service;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Reservation;
import com.robot.hotel.domain.Room;
import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.dto.ReservationDto;
import com.robot.hotel.dto.RoomDto;
import com.robot.hotel.repository.GuestRepository;
import com.robot.hotel.repository.ReservationRepository;
import com.robot.hotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;
    private final RoomService roomService;


    public List<ReservationDto> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationService::buildReservationDto)
                .collect(Collectors.toList());
    }

    public static ReservationDto buildReservationDto(Reservation reservation) {
        RoomDto roomDto = RoomDto.builder()
                .id(reservation.getRoom().getId())
                .roomNumber(reservation.getRoom().getRoomNumber())
                .roomType(reservation.getRoom().getRoomType())
                .pricePerNight(reservation.getRoom().getPricePerNight())
                .maxNumberOfGuests(reservation.getRoom().getMaxNumberOfGuests())
                .isAvailable(reservation.getRoom().isAvailable())
                .build();

        List<GuestDto> guestDtos = reservation.getGuests().stream()
                .map(GuestService::buildGuestDto)
                .collect(Collectors.toList());

        return ReservationDto.builder()
                .id(reservation.getId())
                .checkinDate(reservation.getCheckinDate())
                .checkoutDate(reservation.getCheckoutDate())
                .status(reservation.getStatus())
                .guests(guestDtos)
                .room(roomDto)
                .build();
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findByCheckinDate(LocalDate checkinDate) {
        return reservationRepository.findByCheckinDate(checkinDate);
    }



    public void createReservation(Reservation reservation, List<Long> guestIds, Long roomId) {
        List<Guest> guests = guestRepository.findAllById(guestIds);

        Room room = getRoomById(roomId);
        checkRoomAvailability(room);
        checkNumberOfGuests(room, guests);
        if (!guests.isEmpty()) {
            checkRoomAvailabilityForDates(room, reservation.getCheckinDate(), reservation.getCheckoutDate());
        }

        configureReservation(reservation, room, guests);
        updateRoomAvailability(room);

        reservationRepository.save(reservation);
    }

    private Room getRoomById(Long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            throw new IllegalArgumentException("Invalid room ID: " + roomId);
        }
        return optionalRoom.get();
    }

    private void checkRoomAvailability(Room room) {
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available for reservation.");
        }
    }

    private void checkNumberOfGuests(Room room, List<Guest> guests) {
        int maxNumberOfGuests = room.getMaxNumberOfGuests();
        if (guests.size() > maxNumberOfGuests) {
            throw new IllegalStateException("The maximum number of guests for this room is " + maxNumberOfGuests);
        }
    }

    private void checkRoomAvailabilityForDates(Room room, LocalDate checkinDate, LocalDate checkoutDate) {
        if (!roomService.isRoomAvailable(room.getId(), checkinDate, checkoutDate)) {
            throw new IllegalStateException("The room is not available for these dates.");
        }
    }

    private void configureReservation(Reservation reservation, Room room, List<Guest> guests) {
        reservation.setRoom(room);
        reservation.setGuests(guests);
        for (Guest guest : guests) {
            guest.setRoom(room);
            guest.setReservation(reservation);
        }
    }

    private void updateRoomAvailability(Room room) {
        room.setIsAvailable(false);
    }


    public void addGuestsToReservation(Long reservationId, List<Long> guestIds) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            if (reservation.getStatus().equals("Completed")) {
                throw new IllegalStateException("Cannot add guests to a completed reservation.");
            }

            List<Guest> guests = guestRepository.findAllById(guestIds);

            Room room = reservation.getRoom();
            int maxNumberOfGuests = room.getMaxNumberOfGuests();
            int currentNumberOfGuests = reservation.getGuests().size();
            int totalNumberOfGuests = currentNumberOfGuests + guests.size();
            if (totalNumberOfGuests > maxNumberOfGuests) {
                throw new IllegalStateException("The maximum number of guests for this room is " + maxNumberOfGuests);
            }

            reservation.getGuests().addAll(guests);
            for (Guest guest : guests) {
                guest.setRoom(room);
                guest.setReservation(reservation);
            }

            reservationRepository.save(reservation);
        } else {
            throw new IllegalArgumentException("Invalid reservation ID: " + reservationId);
        }
    }

    public void removeGuestsFromReservation(Long reservationId, List<Long> guestIds) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            List<Guest> guestsToRemove = guestRepository.findAllById(guestIds);
            List<Guest> currentGuests = reservation.getGuests();

            currentGuests.removeAll(guestsToRemove);
            for (Guest guest : guestsToRemove) {
                guest.setRoom(null);
                guest.setReservation(null);
            }
            guestRepository.saveAll(guestsToRemove);

            Room room = reservation.getRoom();
            int currentNumberOfGuests = currentGuests.size();
            if(currentNumberOfGuests == 0) {
                room.setIsAvailable(true);
                deleteReservation(reservationId);
            }
            roomRepository.save(room);
        } else {
            throw new IllegalArgumentException("Invalid reservation ID: " + reservationId);
        }
    }

    public void moveGuestToRoom(Long guestId, Long currentRoomId, Long newRoomId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid guest ID: " + guestId));

        Room currentRoom = roomRepository.findById(currentRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid current room ID: " + currentRoomId));

        Room newRoom = roomRepository.findById(newRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid new room ID: " + newRoomId));

        Reservation reservation = guest.getReservation();

        if (reservation == null) {
            throw new IllegalStateException("No existing reservations found for this guest");
        }

        if (!currentRoom.equals(reservation.getRoom())) {
            throw new IllegalStateException("Guest is not staying in the current room");
        }

        if (newRoom.getGuests().size() == newRoom.getMaxNumberOfGuests()) {
            throw new IllegalStateException("The number of guests in the room already reached the maximum");
        }

        if (newRoom.getMaxNumberOfGuests() <= reservation.getGuests().size()) {
            throw new IllegalStateException("The number of guests exceeds the room capacity");
        }

        guest.setRoom(newRoom);
        guestRepository.save(guest);

        reservation.setRoom(newRoom);
        reservationRepository.save(reservation);

        if(currentRoom.getGuests().size() == 0) {
            currentRoom.setIsAvailable(true);
        }
        newRoom.setIsAvailable(false);
        roomRepository.saveAll(List.of(currentRoom, newRoom));
    }

    public void deleteReservation(Long id) {
        Reservation reservationToDelete = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        reservationRepository.delete(reservationToDelete);

    }
}


