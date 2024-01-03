package az.tourmate.models.address;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "countries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String commonInfoAboutCountry;

    @OneToMany(mappedBy = "country",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<City> cities;

    @OneToMany(mappedBy = "country",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Address> addresses;
}
