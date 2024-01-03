package az.tourmate.services.score;

import az.tourmate.models.comment.BranchComment;
import az.tourmate.models.scores.Like;
import az.tourmate.models.user.User;
import az.tourmate.repositories.branch.BranchCommentRepository;
import az.tourmate.repositories.score.LikeRepository;
import az.tourmate.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final BranchCommentRepository commentRepository;

    public LikeService(LikeRepository likeRepository, BranchCommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
    }


    public void likeComment(Principal connectedUser,Long commentId){

        User user = UserUtil.getConnectedUser(connectedUser);
        BranchComment comment = commentRepository.findById(commentId)
                .orElseThrow();

        Optional<Like> existingLike = likeRepository.findByUserAndBranchComment(user,comment);

        if (existingLike.isEmpty()){
            Like like = Like.builder()
                    .user(user)
                    .branchComment(comment).build();
            likeRepository.save(like);
        }
        else {
            likeRepository.delete(existingLike.get());
        }
    }

}
