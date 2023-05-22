package com.robot.hotel.repository;

import com.robot.hotel.domain.Room;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
    Optional<Room> findByRoomNumber(int roomNumber);
    List<Room> findByIsAvailable(boolean isAvailable);
    List<Room> findByRoomType(String roomType);
    List<Room> findByMaxNumberOfGuests(int maxNumberOfGuests);

}
