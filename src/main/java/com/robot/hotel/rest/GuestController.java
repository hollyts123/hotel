package com.robot.hotel.rest;

import com.robot.hotel.domain.Guest;
import com.robot.hotel.dto.GuestDto;
import com.robot.hotel.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GuestController {
    private final GuestService guestService;

    @GetMapping("/guests")
    public ResponseEntity<List<GuestDto>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @GetMapping("/guests/{id}")
    public ResponseEntity<Guest> findById(@PathVariable Long id) {
        return guestService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guests/name/{lastName}")
    public ResponseEntity<Guest> findByLastName(@PathVariable String lastName) {
        return guestService.findByLastName(lastName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guests/name/{passportNumber}")
    public ResponseEntity<Guest> findByPassportNumber(@PathVariable String passportNumber) {
        return guestService.findByPassportNumber(passportNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/guests")
    public ResponseEntity<Void> save(@RequestBody Guest guest) {
        guestService.saveGuest(guest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/guests/{id}/firstName")
    public ResponseEntity<String> updateFirstName(@PathVariable Long id, @RequestBody String firstName) {
        guestService.updateFirstName(id, firstName);

        return ResponseEntity.ok("Guest's first name updated successfully");
    }

    @PutMapping("/guests/{id}/lastName")
    public ResponseEntity<String> updateLastName(@PathVariable Long id, @RequestBody String lastName) {
        guestService.updateLastName(id, lastName);

        return ResponseEntity.ok("Guest's last name updated successfully");
    }

    @PutMapping("/guests/{id}/passportNumber")
    public ResponseEntity<String> updatePassportNumber(@PathVariable Long id, @RequestBody String passportNumber) {
        guestService.updatePassportNumber(id, passportNumber);

        return ResponseEntity.ok("Guest's passport number updated successfully");
    }

//    Test this endpoint after I make the controller for the room
    @PostMapping("/guests/{id}/rooms/{roomId}")
    public ResponseEntity<Void> addRoom(@PathVariable Long id, @PathVariable Long roomId) {
        guestService.addRoom(id, roomId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/guests/{id}")
    public void deleteGuest(@PathVariable Long id) {
        guestService.deleteGuest(id);
    }
}
