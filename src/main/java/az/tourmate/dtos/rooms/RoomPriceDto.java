package az.tourmate.dtos.rooms;

public record RoomPriceDto(
        Double price,
        boolean breakFast,
        boolean refund)
{
}
