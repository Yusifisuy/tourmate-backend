package az.tourmate.repositories.rooms;

import az.tourmate.models.room.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomPriceRepository extends JpaRepository<RoomPrice,Long> {

    List<RoomPrice> findRoomPriceByPriceBetween(double min, double max);
}
