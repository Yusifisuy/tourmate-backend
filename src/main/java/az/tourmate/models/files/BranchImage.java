package az.tourmate.models.files;

import az.tourmate.models.branches.Branch;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "branch_images")
public class BranchImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    private Branch branch;

    private boolean active;

    @CreationTimestamp
    private Date creationTime;

    @UpdateTimestamp
    private Date lastUpdate;

}