package az.tourmate.repositories.address;

import az.tourmate.models.address.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City,Long> {

    Optional<City> findCityByName(String name);
}
