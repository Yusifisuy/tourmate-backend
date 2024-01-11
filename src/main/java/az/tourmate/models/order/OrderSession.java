package az.tourmate.models.order;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderSession implements Serializable {

    private Long userId;
    private Long roomId;
    private Date entryDate;
    private Date exitDate;
    private Double amount;
    private Integer count;
    @Override
    public String toString() {
        return "OrderSession{" +
                "userId=" + userId +
                ", roomId=" + roomId +
                ", entryDate=" + entryDate +
                ", exitDate=" + exitDate +
                ", amount=" + amount +
                '}';
    }
}
