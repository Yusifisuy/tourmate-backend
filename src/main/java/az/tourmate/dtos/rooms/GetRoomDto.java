package az.tourmate.dtos.rooms;

import az.tourmate.dtos.images.RoomImageDto;

import java.util.List;

public record GetRoomDto(
        Long id,
        List<RoomImageDto> images,
        String roomName,
        String info,
        List<String> features,
        RoomPriceDto priceDto
) {
}
