package com.miagem2.cinema_booking.Controller;

import com.miagem2.cinema_booking.Model.Room;
import com.miagem2.cinema_booking.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping
    public Room createRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable Long roomId) {
        return roomRepository.findById(roomId).orElse(null);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room roomDetails) {
        Room room = roomRepository.findById(roomId).orElse(null);

        if (room != null) {
            // Mettez à jour les détails de la salle
            room.setName(roomDetails.getName());
            room.setCapacity(roomDetails.getCapacity());

            return roomRepository.save(room);
        }

        return null; // Gérer le cas où la salle n'est pas trouvée
    }

    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        roomRepository.deleteById(roomId);
    }
}