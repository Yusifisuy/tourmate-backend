package az.tourmate.repositories.address;

import az.tourmate.models.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
