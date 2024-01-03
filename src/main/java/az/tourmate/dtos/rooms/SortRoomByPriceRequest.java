package az.tourmate.dtos.rooms;

public record SortRoomByPriceRequest(
        double min,
        double max
) {
}
