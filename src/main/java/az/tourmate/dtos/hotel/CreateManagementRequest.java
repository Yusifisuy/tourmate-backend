package az.tourmate.dtos.hotel;

import az.tourmate.models.management.ManagementType;

public record CreateManagementRequest(
         String hotelName,
         String hotelInfo,
         ManagementType type
) {
}
