package az.tourmate.mappers;

import az.tourmate.dtos.rooms.GetRoomDto;
import az.tourmate.dtos.rooms.RoomPriceDto;
import az.tourmate.models.room.Room;
import az.tourmate.models.room.RoomPrice;

import java.util.List;

public class RoomMapper {

    public static RoomPriceDto mapPriceToDto(RoomPrice price){
        return new RoomPriceDto(price.getPrice(),price.isBreakFast(),price.isRefund());
    }

    public static List<GetRoomDto> mapRoomListToDto(List<Room> rooms){
        return rooms.stream().map(room ->
                new GetRoomDto(room.getId(),ImageMapper.mapRoomImageListToDto(room.getRoomImages()),
                        room.getName(),room.getInfoAboutRoom(),room.getFeatures(),mapPriceToDto(room.getRoomPrice())))
                .toList();
    }
}
