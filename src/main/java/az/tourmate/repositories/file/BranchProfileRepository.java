package az.tourmate.repositories.file;

import az.tourmate.models.branches.Branch;
import az.tourmate.models.files.BranchProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchProfileRepository extends JpaRepository<BranchProfile,Long> {


    Optional<BranchProfile> findByBranchAndActiveIsTrue(Branch branch);
}
