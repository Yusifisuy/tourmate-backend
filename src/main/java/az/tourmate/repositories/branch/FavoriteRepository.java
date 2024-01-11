package az.tourmate.repositories.branch;

import az.tourmate.models.branches.Branch;
import az.tourmate.models.branches.Favorite;
import az.tourmate.models.user.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    Optional<Favorite> findByBranchAndUser(Branch branch, User user);
}
