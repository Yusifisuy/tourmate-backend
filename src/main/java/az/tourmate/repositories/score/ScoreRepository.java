package az.tourmate.repositories.score;

import az.tourmate.models.branches.Branch;
import az.tourmate.models.scores.Score;
import az.tourmate.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score,Long> {

    Optional<Score> findByUserAndBranch(User user, Branch branch);
}
