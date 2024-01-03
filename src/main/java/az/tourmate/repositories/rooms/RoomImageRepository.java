package az.tourmate.repositories.rooms;

import az.tourmate.models.room.RoomImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomImageRepository extends JpaRepository<RoomImage, Long> {
}