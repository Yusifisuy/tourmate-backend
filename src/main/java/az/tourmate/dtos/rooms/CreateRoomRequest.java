package az.tourmate.dtos.rooms;

import az.tourmate.models.room.RoomType;

import java.util.List;

public record CreateRoomRequest(
        String name,
        RoomType type,
        String info,
        Double price,
        Integer count,
        Integer capacity,
        List<String> features,
        boolean breakfastInclude,
        boolean refund
) {
}
