package az.tourmate.controller.comments;

import az.tourmate.services.comments.CommentService;
import az.tourmate.services.score.LikeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/branch/{branchId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final LikeService likeService;

    public CommentController(CommentService commentService, LikeService likeService) {
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @PostMapping("/add-comment")
    public void writeComment(@PathVariable("branchId") Long branchId, Principal connectedUser, @RequestBody String content){
        commentService.writeCommentToBranch(content,branchId,connectedUser);
    }

    @PostMapping("/{commentId}/toggle")
    public void likeComment(@PathVariable("commentId") Long commentId,Principal connectedUser){
        likeService.likeComment(connectedUser,commentId);
    }

    @DeleteMapping("/{commentId}/delete-comment")
    @PreAuthorize("hasAuthority('owner:delete')")
    public boolean deleteComment(@PathVariable("commentId") Long commentId){
        return commentService.deleteComment(commentId);
    }

}
