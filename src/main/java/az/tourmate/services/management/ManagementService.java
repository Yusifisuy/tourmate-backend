package az.tourmate.services.management;

import az.tourmate.dtos.hotel.CreateManagementRequest;
import az.tourmate.dtos.hotel.ManagementGetDto;
import az.tourmate.dtos.hotel.UpdateManagementDto;
import az.tourmate.exceptions.hotel.ManagementIsNotFoundException;
import az.tourmate.exceptions.users.UserAlreadyHasManagementException;
import az.tourmate.exceptions.users.UserHasNotAccessException;
import az.tourmate.exceptions.users.UserIsNotFoundException;
import az.tourmate.models.files.ManagementProfile;
import az.tourmate.models.management.Management;
import az.tourmate.models.user.User;
import az.tourmate.repositories.file.ManagementProfileRepository;
import az.tourmate.repositories.management.ManagementRepository;
import az.tourmate.repositories.user.UserRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ManagementService {

    private final ManagementRepository managementRepository;
    private final UserRepository userRepository;

    private final ManagementProfileRepository managementProfileRepository;

    public ManagementService(ManagementRepository managementRepository, UserRepository userRepository,
                             ManagementProfileRepository managementProfileRepository) {
        this.managementRepository = managementRepository;
        this.userRepository = userRepository;
        this.managementProfileRepository = managementProfileRepository;
    }


    public ManagementGetDto createManagement(CreateManagementRequest hotelDto, Principal connectedUser){

        List<ManagementProfile> managementProfiles = new ArrayList<>();

        Management management = Management.builder()
                .managementName(hotelDto.hotelName())
                .managementInfo(hotelDto.hotelInfo())
                .managementProfiles(managementProfiles)
                .active(true)
                .managementType(hotelDto.type())
                .build();
        managementRepository.save(management);
        managementProfiles.add(createDefaultProfileForManagement(management));
        management.setManagementProfiles(managementProfiles);
        managementRepository.save(management);
        addUserToManagement(management,connectedUser);

        return new ManagementGetDto(management.getManagementName(), management.getManagementInfo());
    }

    public void deleteManagement(Long hotelId, Principal connectedUser){
        Management management = getHotel(hotelId);
        var user = UserUtil.getConnectedUser(connectedUser);
        if (!checkAccess(management,user)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur!");
        } else {
            managementRepository.deleteById(hotelId);
        }
    }

    public ManagementGetDto updateManagement(Long hotelId, Principal connectedUser, UpdateManagementDto hotelDto){
        Management management = getHotel(hotelId);
        User user = UserUtil.getConnectedUser(connectedUser);
        if (!checkAccess(management,user)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur!");
        }
        else {
            management.setManagementName(hotelDto.hotelName());
            management.setManagementInfo(hotelDto.hotelInfo());
            managementRepository.save(management);
        }

        return new ManagementGetDto(management.getManagementName(), management.getManagementInfo());
    }


    private boolean checkAccess(Management management, User user){
      return management.getUsers().contains(user);
    }

    private Management getHotel(Long hotelId){
        return managementRepository.findById(hotelId)
                .orElseThrow(() -> new ManagementIsNotFoundException(ExceptionTexts.MANAGEMENT_NOT_FOUND));
    }

    private void addUserToManagement(Management management, Principal connectedUser){
        User user = UserUtil.getConnectedUser(connectedUser);
        user.setManagement(management);
        userRepository.save(user);
    }


    public boolean addAdminToManagement(Principal connectedUser,String email,Long managementId){
        Management management = managementRepository.findById(managementId)
                .orElseThrow(() -> new ManagementIsNotFoundException(ExceptionTexts.MANAGEMENT_NOT_FOUND));
        log.info("MANAGEMENT :" + management);
        User user = UserUtil.getConnectedUser(connectedUser);

        log.info("MANAGEMENT OF USER :" + user.getManagement());

        User addedUser = userRepository.findUserByEmailAndActiveIsTrue(email)
                .orElseThrow(() -> new UserIsNotFoundException(ExceptionTexts.USER_NOT_FOUND));


        if (user.getManagement().equals(management)){
            if (addedUser.getManagement()==null){
                log.info("OWNER OF " + management.getManagementName() + " added new user: " + addedUser.getEmail());
                addedUser.setManagement(management);
                userRepository.save(addedUser);
                return true;
            }
            else {
                throw new UserAlreadyHasManagementException(ExceptionTexts.USER_ALREADY_HAS_MANAGEMENT);
            }
        }
        else {
            throw new UserHasNotAccessException(ExceptionTexts.ACCESS_DENIED);
        }
    }


    private ManagementProfile createDefaultProfileForManagement(Management management){
        ManagementProfile managementProfile = ManagementProfile.builder().management(management)
                .fileName("default.png").active(true).build();

        return managementProfileRepository.save(managementProfile);
    }




}