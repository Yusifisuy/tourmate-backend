package az.tourmate.services.user;

import az.tourmate.dtos.user.ChangePasswordRequest;
import az.tourmate.dtos.user.UserProfileResponse;
import az.tourmate.exceptions.file.ProfileIsNotFoundException;
import az.tourmate.exceptions.users.PasswordIsWrongException;
import az.tourmate.exceptions.users.UserIsNotFoundException;
import az.tourmate.models.user.User;
import az.tourmate.repositories.file.UserProfileRepository;
import az.tourmate.repositories.user.UserRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserProfileRepository profileRepository;

    @Value("${application.bucket.user-profile-link}")
    private String userProfileLink;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

        this.profileRepository = profileRepository;
    }


    public void changePasswordOfUser(ChangePasswordRequest request,Principal connectedUser){
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        log.info("RAW PASSWORD : " + request.currentPassword());
        log.info("ENCODED PASSWORD : " + user.getPassword());


        if (!passwordEncoder.matches(request.currentPassword(),user.getPassword())){
            throw new PasswordIsWrongException("Password is not match with current password");
        }

        if (!request.newPassword().equals(request.confirmPassword())){
            throw new PasswordIsWrongException("Passwords are not the same");
        }

        if (passwordEncoder.matches(request.newPassword(),user.getPassword())){
            throw new PasswordIsWrongException("New password can not be same with old one");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }




    public void deleteAccount(Principal connectedUser){
        User user = UserUtil.getConnectedUser(connectedUser);
        user.setActive(false);
        userRepository.save(user);
    }



    public String getUserProfilePicture(String email){
        User user = userRepository.findUserByEmailAndActiveIsTrue(email)
                .orElseThrow(()-> new UserIsNotFoundException("İstifadəçi tapılmadı"));

        String fileName = profileRepository.findUserProfileByUserAndActiveIsTrue(user)
                .orElseThrow(() -> new ProfileIsNotFoundException(ExceptionTexts.PROFILE_IS_NOT_FOUND))
                .getFileName();

        return userProfileLink+fileName;
    }

    public UserProfileResponse getUserProfile(String email){
        User user = userRepository.findUserByEmailAndActiveIsTrue(email)
                .orElseThrow(()-> new UserIsNotFoundException("İstifadəçi tapılmadı"));

        return new UserProfileResponse(user.getFirstName(),user.getLastName(),getUserProfilePicture(email));
    }


}


/*
* {
  "firstName": "Kanye",
  "lastName": "West",
  "email": "ye@gmail.com",
  "password": "vultures",
  "phoneNumber":"+11111111",
  "address":"Baku",
  "citizen":"Azerbaijan",
  "role": "USER"
}
*
*
*
* */
