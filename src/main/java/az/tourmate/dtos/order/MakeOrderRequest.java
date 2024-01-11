package az.tourmate.dtos.order;

import java.util.Date;

public record MakeOrderRequest(
        Long roomId,
        Integer count,
        Date entryDate,
        Date outDate
) {
}
