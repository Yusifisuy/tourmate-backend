package az.tourmate.services.files;

import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.exceptions.file.ProfileIsNotFoundException;
import az.tourmate.exceptions.files.ImageIsWrongException;
import az.tourmate.exceptions.files.SomethingWentWrongInIOException;
import az.tourmate.exceptions.hotel.ManagementIsNotFoundException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.exceptions.users.UserHasNotAccessException;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.files.BranchImage;
import az.tourmate.models.files.BranchProfile;
import az.tourmate.models.files.ManagementProfile;
import az.tourmate.models.files.UserProfile;
import az.tourmate.models.management.Management;
import az.tourmate.models.room.Room;
import az.tourmate.models.room.RoomImage;
import az.tourmate.models.user.User;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.repositories.file.BranchImageRepository;
import az.tourmate.repositories.file.BranchProfileRepository;
import az.tourmate.repositories.file.ManagementProfileRepository;
import az.tourmate.repositories.file.UserProfileRepository;
import az.tourmate.repositories.management.ManagementRepository;
import az.tourmate.repositories.rooms.RoomImageRepository;
import az.tourmate.repositories.rooms.RoomRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class ImageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.bucket.branch-profile-name}")
    private String branchProfileBucketName;

    @Value("${application.bucket.user-profile-name}")
    private String userProfileBucketName;

    @Value("${application.bucket.management-profile-name}")
    private String managementProfileBucketName;

    @Value("${application.bucket.rooms-image}")
    private String roomImagesBucketName;
    private final AmazonS3 amazonS3Client;
    private final BranchImageRepository branchImageRepository;
    private final UserProfileRepository userProfileRepository;
    private final BranchRepository branchRepository;
    private final ManagementRepository managementRepository;
    private final BranchProfileRepository branchProfileRepository;
    private final ManagementProfileRepository managementProfileRepository;
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;


    public ImageService(AmazonS3 amazonS3Client, BranchImageRepository branchImageRepository, UserProfileRepository userProfileRepository,
                        BranchRepository branchRepository, ManagementRepository managementRepository, BranchProfileRepository branchProfileRepository, ManagementProfileRepository managementProfileRepository, RoomRepository roomRepository, RoomImageRepository roomImageRepository) {
        this.amazonS3Client = amazonS3Client;
        this.branchImageRepository = branchImageRepository;
        this.userProfileRepository = userProfileRepository;
        this.branchRepository = branchRepository;
        this.managementRepository = managementRepository;
        this.branchProfileRepository = branchProfileRepository;
        this.managementProfileRepository = managementProfileRepository;
        this.roomRepository = roomRepository;
        this.roomImageRepository = roomImageRepository;
    }


    public String uploadFileToBranches(MultipartFile multipartFile, Principal connectedUser, Long branchId){
        var user = UserUtil.getConnectedUser(connectedUser);

        Branch branch = getBranch(branchId);

        if (!checkIfUserHasAccessToBranch(user,branch)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur");
        }

        checkImage(multipartFile);

        File file = multipartToFile(multipartFile);

        String fileName = multipartFile.getOriginalFilename()
                + user.getFirstName() + "_"
                + user.getLastName() +  "_"
                + branch.getName() + UUID.randomUUID();

        amazonS3Client.putObject(bucketName,fileName,file);

        BranchImage branchImage = BranchImage.builder().fileName(fileName).branch(branch).active(true).build();
        branchImageRepository.save(branchImage);

        return fileName;
    }

    public String uploadFileToRooms(MultipartFile multipartFile, Principal connectedUser, Long roomId){
        var user = UserUtil.getConnectedUser(connectedUser);

        Room room = roomRepository.findByIdAndActiveIsTrue(roomId)
                .orElseThrow(() -> new RoomIsNotFoundException(ExceptionTexts.ROOM_IS_NOT_FOUND));

        if (!checkIfUserHasAccessToRoom(user,room)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur");
        }

        checkImage(multipartFile);

        File file = multipartToFile(multipartFile);

        String fileName = multipartFile.getOriginalFilename()
                + user.getFirstName() + "_"
                + user.getLastName() +  "_"
                + room.getName() + UUID.randomUUID();

        amazonS3Client.putObject(roomImagesBucketName,fileName,file);

        RoomImage roomImage = RoomImage.builder().fileName(fileName).room(room).active(true).build();
        roomImageRepository.save(roomImage);

        return fileName;
    }

    public void uploadProfileToUserProfile(MultipartFile multipartFile, Principal connectedUser){
        var user = UserUtil.getConnectedUser(connectedUser);
        checkImage(multipartFile);

        File file = multipartToFile(multipartFile);

        String fileName = multipartFile.getOriginalFilename()
                + user.getFirstName() + "_"
                + user.getLastName() +  "_"
                + "profile:" + UUID.randomUUID();

        amazonS3Client.putObject(userProfileBucketName,fileName,file);
        makeOldProfileDeactive(user);
        UserProfile userProfile = UserProfile.builder().fileName(fileName).active(true).user(user).build();
        userProfileRepository.save(userProfile);

        log.info("PROFILE SAVED : " + fileName);
    }

    public String uploadFileToBranchProfile(MultipartFile multipartFile, Principal connectedUser, Long branchId){
        var user = UserUtil.getConnectedUser(connectedUser);

        Branch branch = getBranch(branchId);

        if (!checkIfUserHasAccessToBranch(user,branch)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur");
        }

        checkImage(multipartFile);

        File file = multipartToFile(multipartFile);

        String fileName = multipartFile.getOriginalFilename()
                + user.getFirstName() + "_"
                + user.getLastName() +  "_"
                + branch.getName() + UUID.randomUUID();

        amazonS3Client.putObject(branchProfileBucketName,fileName,file);

        BranchProfile branchProfile = BranchProfile.builder().fileName(fileName).active(true).build();
        branchProfileRepository.save(branchProfile);

        return fileName;
    }


    public String uploadFileToManagementProfile(MultipartFile multipartFile, Principal connectedUser, Long managementId){
        var user = UserUtil.getConnectedUser(connectedUser);

        Management management = getManagement(managementId);

        if (!checkIfUserHasAccessToManagement(user,management)){
            throw new UserHasNotAccessException("Bu əmri icra etmə hüququnuz yoxdur");
        }

        checkImage(multipartFile);

        File file = multipartToFile(multipartFile);

        String fileName = multipartFile.getOriginalFilename()
                + user.getFirstName() + "_"
                + user.getLastName() +  "_"
                + management.getManagementName() + UUID.randomUUID();

        amazonS3Client.putObject(managementProfileBucketName,fileName,file);

        ManagementProfile managementProfile = ManagementProfile.builder().fileName(fileName).active(true).build();
        managementProfileRepository.save(managementProfile);

        return fileName;
    }


    public void getFiles(Long branchId,HttpServletResponse response){

        Branch branch = getBranch(branchId);
        List<S3Object> s3ObjectList = new ArrayList<>();
        List<BranchImage> branchImages = branch.getBranchImages();

        for (BranchImage branchImage : branchImages){
            S3Object s = amazonS3Client.getObject(bucketName, branchImage.getFileName());
            log.info("OBJECT TOOK FROM S3 BUCKET : " + s);
            s3ObjectList.add(s);
        }
        for (S3Object s3Object : s3ObjectList){
            InputStream inputStream = s3Object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int length;
            byte[] buffer = new byte[4096];
            while (true){
                try {
                    if (!((length=inputStream.read(buffer,0,buffer.length))!=-1)) break;
                } catch (IOException e) {
                    throw new SomethingWentWrongInIOException("XƏTA");
                }
                outputStream.write(buffer,0,length);
            }

            byte[] byteArray = new byte[outputStream.size()];
            int i = 0;
            for (Byte b : outputStream.toByteArray()){
                byteArray[i++]=b;
            }
            response.setContentType("image/png");
            InputStream in = new ByteArrayInputStream(byteArray);
            try {
                IOUtils.copy(in,response.getOutputStream());
            } catch (IOException e) {
                throw new SomethingWentWrongInIOException("XƏTA");
            }
        }

    }

    private void checkImage(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new ImageIsWrongException("Şəkil fayllardan başqa fayllara icazə yoxdur!");
        }

        long maxSizeBytes = 10 * 1024 * 1024;
        if (file.getSize() > maxSizeBytes) {
            throw new ImageIsWrongException("Şəklin ölçüsü 10mb dan böyük olmamalıdır!");
        }
    }

    private File multipartToFile(MultipartFile multipartFile){
        File file = new File(multipartFile.getOriginalFilename());

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(multipartFile.getBytes());
        }
        catch (IOException exception){
        }
        return file;
    }


    private boolean checkIfUserHasAccessToBranch(User user, Branch branch){
        return user.getManagement().getId().equals(branch.getManagement().getId());
    }

    private boolean checkIfUserHasAccessToRoom(User user, Room room){
        return user.getManagement().getBranches().contains(room.getBranch());
    }

    private boolean checkIfUserHasAccessToManagement(User user, Management management){
        return user.getManagement().getId().equals(management.getId());
    }


    private Branch getBranch(Long branchId){
        return branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(() -> new BranchIsNotFoundException("Filial tapılmadı"));
    }

    private Management getManagement(Long managementId){
        return managementRepository.findById(managementId)
                .orElseThrow(() -> new ManagementIsNotFoundException("Biznes tapılmadı"));
    }

    private void makeOldProfileDeactive(User user){
        UserProfile userProfile = userProfileRepository.findUserProfileByUserAndActiveIsTrue(user)
                .orElseThrow(() -> new ProfileIsNotFoundException(ExceptionTexts.PROFILE_IS_NOT_FOUND));
        userProfile.setActive(false);
        userProfileRepository.save(userProfile);
    }


}



/*
* {
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicRead",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::birseyyoxlayiram/*"
        }
    ]
}
*/
