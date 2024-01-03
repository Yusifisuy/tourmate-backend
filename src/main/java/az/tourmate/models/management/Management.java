package az.tourmate.models.management;

import az.tourmate.models.branches.Branch;
import az.tourmate.models.files.ManagementProfile;
import az.tourmate.models.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "managements")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Management {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String managementName;
    private String managementInfo;
    private Double overallScore;

    @OneToMany(mappedBy = "management", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Branch> branches;

    @CreationTimestamp
    private Date creationTime;

    @UpdateTimestamp
    private Date lastUpdate;

    private Boolean active;

    @OneToMany(mappedBy = "management",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<User> users;

    @OneToMany(mappedBy = "management",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<ManagementProfile> managementProfiles;

    @Enumerated(EnumType.STRING)
    private ManagementType managementType;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Management that = (Management) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
