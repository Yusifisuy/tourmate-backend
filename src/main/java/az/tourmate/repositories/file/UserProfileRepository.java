package az.tourmate.repositories.file;

import az.tourmate.models.files.UserProfile;
import az.tourmate.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    Optional<UserProfile> findUserProfileByActiveIsTrue();
    Optional<UserProfile> findUserProfileByFileNameAndActiveIsTrue(String fileName);
    Optional<UserProfile> findUserProfileByUserAndActiveIsTrue(User user);
}
