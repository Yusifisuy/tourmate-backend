package az.tourmate.dtos.comments;

public record BranchCommentGetDto(
        String content,
        String username,

        String userProfile
) {
}
