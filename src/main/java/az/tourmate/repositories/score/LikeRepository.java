package az.tourmate.repositories.score;

import az.tourmate.models.comment.BranchComment;
import az.tourmate.models.scores.Like;
import az.tourmate.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like,Long> {

    Optional<Like> findByUserAndBranchComment(User user, BranchComment comment);
}
