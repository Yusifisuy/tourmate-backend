package az.tourmate.dtos.user;

public record UserProfileResponse(
        String firstName,
        String lastName,
        String profilePictureUrl
) {
}
