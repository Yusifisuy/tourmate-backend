package az.tourmate.models.room;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @JsonIgnore
    @OneToOne(targetEntity = RoomPrice.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "price_id")
    private RoomPrice roomPrice;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private String infoAboutRoom;

    private Integer capacity;

    private Integer count;

    private Integer activeCount;

    @ElementCollection
    @CollectionTable(name = "facilities",joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "facility")
    private List<String> features;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<RoomImage> roomImages;


    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id",referencedColumnName = "id")
    private Branch branch;

    @CreationTimestamp
    private Date creationTime;

    @UpdateTimestamp
    private Date lastUpdate;

    private boolean active;


}
