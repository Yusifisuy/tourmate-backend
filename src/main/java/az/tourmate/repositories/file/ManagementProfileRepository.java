package az.tourmate.repositories.file;

import az.tourmate.models.files.ManagementProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementProfileRepository extends JpaRepository<ManagementProfile,Long> {
}
