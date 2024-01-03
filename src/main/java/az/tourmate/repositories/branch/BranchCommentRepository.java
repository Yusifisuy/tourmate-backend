package az.tourmate.repositories.branch;

import az.tourmate.models.comment.BranchComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchCommentRepository extends JpaRepository<BranchComment,Long> {

    Optional<BranchComment> findByIdAndActiveIsTrue(Long commentId);
}
