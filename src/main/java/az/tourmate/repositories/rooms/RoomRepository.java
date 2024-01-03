package az.tourmate.repositories.rooms;


import az.tourmate.models.branches.Branch;
import az.tourmate.models.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Optional<Room> findByIdAndActiveIsTrue(Long id);

    Optional<List<Room>> findByBranchAndActiveIsTrue(Branch branch);

    //Optional<List<Room>> findByRoomPriceBetweenAndActiveIsTrue(double min, double max);

    @Query("SELECT r FROM Room r JOIN r.roomPrice p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    Optional<List<Room>> findRoomsByPrice(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}
