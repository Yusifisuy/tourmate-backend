package az.tourmate.models.branches;

import az.tourmate.models.address.Address;
import az.tourmate.models.comment.BranchComment;
import az.tourmate.models.files.BranchImage;
import az.tourmate.models.files.BranchProfile;
import az.tourmate.models.management.Management;
import az.tourmate.models.room.Room;
import az.tourmate.models.scores.Score;
import az.tourmate.models.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "branches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long capacity;
    private String commonInfoAboutBranch;
    private String commonInfoAboutPlace;
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id",referencedColumnName = "id")
    private Management management;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @JsonIgnore
    @OneToOne(targetEntity = Address.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "branch", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<BranchImage> branchImages;

    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<BranchProfile> branchProfiles;

    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<BranchComment> branchComments;

    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Score> scores;

    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Room> rooms;

    @CreationTimestamp
    private Date creationTime;

    @UpdateTimestamp
    private Date lastUpdate;

    private Boolean active;

    private boolean open;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id) && Objects.equals(capacity, branch.capacity) && Objects.equals(commonInfoAboutBranch, branch.commonInfoAboutBranch) && Objects.equals(commonInfoAboutPlace, branch.commonInfoAboutPlace) && Objects.equals(name, branch.name) && Objects.equals(management, branch.management) && Objects.equals(user, branch.user) && Objects.equals(address, branch.address) && Objects.equals(branchImages, branch.branchImages) && Objects.equals(branchProfiles, branch.branchProfiles) && Objects.equals(branchComments, branch.branchComments) && Objects.equals(scores, branch.scores) && Objects.equals(rooms, branch.rooms) && Objects.equals(creationTime, branch.creationTime) && Objects.equals(lastUpdate, branch.lastUpdate) && Objects.equals(active, branch.active);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, capacity, commonInfoAboutBranch, commonInfoAboutPlace, name, management, user, address, branchImages, branchProfiles, branchComments, scores, rooms, creationTime, lastUpdate, active);
    }
}
