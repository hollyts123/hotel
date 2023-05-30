package com.robot.hotel.service;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Room;
import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.repository.GuestRepository;
import com.robot.hotel.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final RoomRepository roomRepository;

    public List<GuestDto> findAll() {
        return guestRepository.findAll().stream()
                .map(GuestService::buildGuestDto)
                .collect(Collectors.toList());
}

    public static GuestDto buildGuestDto(Guest guest) {
        var roomNumber = 0;
        if (guest.getRoom() != null) {
        roomNumber = guest.getRoom().getRoomNumber();
        }
        return GuestDto.builder()
                .id(guest.getId())
                .firstName(guest.getFirstName())
                .lastName(guest.getLastName())
                .dateOfBirth(guest.getDateOfBirth())
                .gender(guest.getGender())
                .passportNumber(guest.getPassportNumber())
                .build();
}

    public Optional<Guest> findById(Long id) {
        return guestRepository.findById(id);
    }

    public List<Guest> findGuestsByIds(List<Long> guestIds) {
        return guestRepository.findAllById(guestIds);
    }

    public Optional<Guest> findByLastName(String lastName) {
        return guestRepository.findByLastName(lastName);
    }

    public Optional<Guest> findByPassportNumber(String passportNumber) {
        return guestRepository.findByPassportNumber(passportNumber);
    }

    public void saveGuest(Guest guest) {
        guestRepository.save(guest);
    }

   public void updateFirstName(Long guestId, String firstName) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        guest.setFirstName(firstName);

        guestRepository.save(guest);
   }

    public void updateLastName(Long guestId, String newLastName) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        guest.setLastName(newLastName);

        guestRepository.save(guest);
    }

   public void updatePassportNumber(Long guestId, String passportNumber) {
       Guest guest = guestRepository.findById(guestId)
               .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
       guest.setPassportNumber(passportNumber);

       guestRepository.save(guest);
   }

   public void addRoom(Long guestId, Long roomId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        guest.setRoom(room);

        guestRepository.save(guest);
   }

   public void deleteGuest(Long id) {
       Guest guestToDelete = guestRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("User not found"));
       guestRepository.delete(guestToDelete);
   }

}
