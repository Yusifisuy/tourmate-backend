package az.tourmate.repositories.branch;

import az.tourmate.models.branches.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch,Long> {

    Optional<Branch> findByIdAndActiveIsTrue(Long aLong);

    List<Branch> findBranchByAddress_City_Name(String cityName);
}
