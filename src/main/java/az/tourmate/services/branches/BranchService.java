package az.tourmate.services.branches;

import az.tourmate.dtos.branches.BranchGetDto;
import az.tourmate.dtos.branches.CreateBranchDto;
import az.tourmate.dtos.comments.BranchCommentGetDto;
import az.tourmate.exceptions.address.CountryNotFoundException;
import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.exceptions.file.ProfileIsNotFoundException;
import az.tourmate.exceptions.users.UserHasNotAccessException;
import az.tourmate.mappers.ImageMapper;
import az.tourmate.mappers.RoomMapper;
import az.tourmate.models.address.Address;
import az.tourmate.models.address.City;
import az.tourmate.models.address.Country;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.branches.Favorite;
import az.tourmate.models.comment.BranchComment;
import az.tourmate.models.files.BranchProfile;
import az.tourmate.models.scores.Score;
import az.tourmate.models.user.User;
import az.tourmate.repositories.address.AddressRepository;
import az.tourmate.repositories.address.CityRepository;
import az.tourmate.repositories.address.CountryRepository;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.repositories.branch.FavoriteRepository;
import az.tourmate.repositories.file.BranchProfileRepository;
import az.tourmate.repositories.file.UserProfileRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final AddressRepository addressRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final BranchProfileRepository branchProfileRepository;
    private final UserProfileRepository userProfileRepository;
    private final FavoriteRepository favoriteRepository;

    public BranchService(BranchRepository branchRepository, AddressRepository addressRepository,
                         CountryRepository countryRepository, CityRepository cityRepository,
                         BranchProfileRepository branchProfileRepository, UserProfileRepository userProfileRepository, FavoriteRepository favoriteRepository) {
        this.branchRepository = branchRepository;
        this.addressRepository = addressRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.branchProfileRepository = branchProfileRepository;
        this.userProfileRepository = userProfileRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public BranchGetDto createBranch(CreateBranchDto branchDto, Long hotelId, Principal connectedUser){

        if (!checkIfUserCanCreateBranch(connectedUser,hotelId)){
            throw new UserHasNotAccessException("Bu əmri icra etmək hüququnuz yoxdur");
        }

        Country country = countryRepository.findCountryByName(branchDto.countryName())
                .orElseThrow(() -> new CountryNotFoundException("Ölkə tapılmadı"));
        City city = cityRepository.findCityByName(branchDto.cityName())
                .orElseThrow(() -> new CountryNotFoundException("Şəhər tapılmadı"));

        Address address = createAddress(country,city,branchDto.street());

        List<BranchProfile> branchProfiles = new ArrayList<>();

        Branch branch =  Branch.builder()
                .name(branchDto.branchName())
                .address(address)
                .commonInfoAboutBranch(branchDto.commonInfoAboutBranch())
                .commonInfoAboutPlace(branchDto.commonInfoAboutPlace())
                .capacity(branchDto.capacity())
                .branchProfiles(branchProfiles)
                .management(UserUtil.getConnectedUser(connectedUser).getManagement())
                .open(true)
                .active(true)
                .build();
        branchRepository.save(branch);
        branchProfiles.add(createDefaultBranchProfile(branch));
        branch.setBranchProfiles(branchProfiles);
        branchRepository.save(branch);
        return new BranchGetDto(branch.getCapacity(),
                branchDto.commonInfoAboutBranch(),
                branch.getCommonInfoAboutPlace(),
                branch.getName(),
                branch.getManagement().getManagementName(),null,null,null,null);
    }



    public BranchGetDto getBranch(Long branchId){
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(()-> new BranchIsNotFoundException(ExceptionTexts.BRANCH_NOT_FOUND));

        List<BranchComment> comments = branch.getBranchComments();

        List<BranchCommentGetDto> branchCommentGetDtos = comments.
                stream().map(comment ->
                        new BranchCommentGetDto(comment.getContent()
                                ,comment.getUser().getFirstName() + " " + comment.getUser().getLastName()
                        ,userProfileRepository.findUserProfileByUserAndActiveIsTrue(comment.getUser())
                                .orElseThrow(() -> new ProfileIsNotFoundException(ExceptionTexts.PROFILE_IS_NOT_FOUND))
                                .getFileName())).toList();

        return new BranchGetDto(branch.getCapacity(),branch.getCommonInfoAboutBranch()
        , branch.getCommonInfoAboutPlace(), branch.getName(),
                branch.getManagement().getManagementName()
                ,getScore(branch),branchCommentGetDtos, ImageMapper.mapImageListToDto(branch.getBranchImages())
        , RoomMapper.mapRoomListToDto(branch.getRooms()));
    }





    public void deleteBranch(Principal connectedUser,Long branchId){
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(()-> new BranchIsNotFoundException(ExceptionTexts.BRANCH_NOT_FOUND));
        User user = UserUtil.getConnectedUser(connectedUser);

        if (branch.getManagement().getUsers().contains(user)){
            branch.setActive(false);
            branchRepository.save(branch);
        }

        else {
            throw new UserHasNotAccessException(ExceptionTexts.ACCESS_DENIED);
        }
    }

    public void closeBranch(Principal connectedUser,Long branchId){
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(()-> new BranchIsNotFoundException(ExceptionTexts.BRANCH_NOT_FOUND));
        User user = UserUtil.getConnectedUser(connectedUser);

        if (branch.getManagement().getUsers().contains(user)){
            branch.setOpen(false);
            branchRepository.save(branch);
        }

        else {
            throw new UserHasNotAccessException(ExceptionTexts.ACCESS_DENIED);
        }
    }


    public void clickFavorite(Principal connectedUser,Long branchId){
        var user = UserUtil.getConnectedUser(connectedUser);
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(() -> new BranchIsNotFoundException("Belə bir filial yoxdur"));

        Optional<Favorite> opsiyonel = favoriteRepository.findByBranchAndUser(branch,user);
        if (opsiyonel.isEmpty()){
            Favorite favorite = Favorite.builder().branch(branch).user(user).active(true).build();
            favoriteRepository.save(favorite);
        }
        else {
            opsiyonel.get().setActive(false);
            favoriteRepository.save(opsiyonel.get());
        }
    }



    private Address createAddress(Country country,City city,String street){
        Address address = Address.builder()
                .country(country)
                .city(city)
                .street(street)
                .active(true).build();

        return addressRepository.save(address);
    }

    private boolean checkIfUserCanCreateBranch(Principal connectedUser,Long hotelId){
        var user = UserUtil.getConnectedUser(connectedUser);

        return user.getManagement().getId().equals(hotelId);
    }

    private Double getScore(Branch branch){
        if (branch.getScores().isEmpty()){
            return null;
        }
        else {
            List<Score> scores = branch.getScores();
            return plusScores(scores) / scores.size();
        }
    }

    private double plusScores(List<Score> scores){
        double result = 0;
        for (Score score : scores){
            result += score.getScore();
        }
        return result;
    }


    private BranchProfile createDefaultBranchProfile(Branch branch){
        BranchProfile branchProfile = BranchProfile.builder().branch(branch).fileName("default.png").active(true).build();
        return branchProfileRepository.save(branchProfile);
    }


}
