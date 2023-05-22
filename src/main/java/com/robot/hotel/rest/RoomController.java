package com.robot.hotel.rest;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.domain.Room;
import com.robot.hotel.dto.RoomDto;
import com.robot.hotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomDto>> findAll() {
        return ResponseEntity.ok(roomService.findAll());
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> findById(@PathVariable Long id) {
        return roomService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/rooms/{roomNumber}")
    public ResponseEntity<Room> findByRoomNumber(@PathVariable int roomNumber) {
        return roomService.findByRoomNumber(roomNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @GetMapping("/rooms/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        List<Room> availableRooms = roomService.getAvailableRooms();
        return ResponseEntity.ok(availableRooms);
    }

    @GetMapping("/rooms/unavailable")
    public ResponseEntity<List<Room>> getUnavailableRooms() {
        List<Room> unavailableRooms = roomService.getUnavailableRooms();
        return ResponseEntity.ok(unavailableRooms);
    }

   @GetMapping("/rooms/findByType/{roomType}")
   public ResponseEntity<List<Room>> getRoomsByType(@PathVariable String roomType) {
       List<Room> rooms = roomService.findByRoomType(roomType);
       return ResponseEntity.ok(rooms);
   }

   @GetMapping("/rooms/findByMaxNumber/{maxNumberOfGuests}")
   public ResponseEntity<List<Room>> getRoomsByMaxNumberOfGuests(@PathVariable int maxNumberOfGuests) {
        List<Room> rooms = roomService.findByMaxNumberOfGuests(maxNumberOfGuests);
       return ResponseEntity.ok(rooms);
   }

    @PostMapping("/rooms")
    public ResponseEntity<Void> save(@RequestBody Room room) {
        roomService.saveRoom(room);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("rooms/{roomId}/guests")
    public ResponseEntity<String> addGuestToRoom(@PathVariable Long roomId, @RequestBody Guest guest) {
        Optional<Room> room = roomService.findById(roomId);
        if (room.isPresent()) {
            roomService.addGuestToRoom(room, guest);
            return ResponseEntity.ok("Guest added to room successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/rooms/{id}/roomNumber")
    public ResponseEntity<String> updateRoomNumber(@PathVariable Long id, @RequestBody int roomNumber) {
        roomService.updateRoomNumber(id, roomNumber);

        return ResponseEntity.ok("Room's number updated successfully");
    }

    @PutMapping("/rooms/{id}/roomType")
    public ResponseEntity<String> updateRoomType(@PathVariable Long id, @RequestBody String roomType) {
        roomService.updateRoomType(id, roomType);

        return ResponseEntity.ok("Room type updated successfully");
    }

    @PutMapping("/rooms/{id}/pricePerNight")
    public ResponseEntity<String> updatePricePerNight(@PathVariable Long id, @RequestBody double pricePerNight) {
        roomService.updatePricePerNight(id, pricePerNight);

        return ResponseEntity.ok("Price per night updated successfully");
    }

    @PutMapping("/rooms/{id}/maxNumberOfGuests")
    public ResponseEntity<String> updateMaxNumberOfGuests(@PathVariable Long id, @RequestBody int maxNumberOfGuests) {
        roomService.updateMaxNumberOfGuests(id, maxNumberOfGuests);

        return ResponseEntity.ok("Max number of guests updated successfully");
    }

    @PutMapping("/rooms/{id}/isAvailable")
    public ResponseEntity<String> updateIsAvailable(@PathVariable Long id, @RequestBody boolean isAvailable) {
        roomService.updateIsAvailable(id, isAvailable);

        return ResponseEntity.ok("Room status updated successfully");
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }

}
