package az.tourmate.mappers;

import az.tourmate.dtos.comments.BranchCommentGetDto;
import az.tourmate.models.comment.BranchComment;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class CommentMapper {

    @Value("${application.bucket.user-profile-link}")
    private static String userProfileLink;

    public static List<BranchCommentGetDto> mapBranchCommentListToDto(List<BranchComment> comments,String fileName){

        return comments.
                stream().map(comment ->
                        new BranchCommentGetDto(comment.getContent()
                                ,comment.getUser().getFirstName() + " " + comment.getUser().getLastName()
                                ,userProfileLink+fileName)).toList();
    }





}
