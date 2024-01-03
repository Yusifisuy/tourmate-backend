package az.tourmate.repositories.management;

import az.tourmate.models.management.Management;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementRepository extends JpaRepository<Management,Long> {
}
