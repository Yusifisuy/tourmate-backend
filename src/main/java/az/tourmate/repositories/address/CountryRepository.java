package az.tourmate.repositories.address;

import az.tourmate.models.address.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country,Long> {
    Optional<Country> findCountryByName(String name);
}
