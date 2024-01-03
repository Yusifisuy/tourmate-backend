package az.tourmate.services.comments;

import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.exceptions.comments.CommentIsNotFoundException;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.comment.BranchComment;
import az.tourmate.repositories.branch.BranchCommentRepository;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.utils.ExceptionTexts;
import az.tourmate.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommentService {

    private final BranchCommentRepository branchCommentRepository;
    private final BranchRepository branchRepository;

    public CommentService(BranchCommentRepository branchCommentRepository,
                          BranchRepository branchRepository) {
        this.branchCommentRepository = branchCommentRepository;
        this.branchRepository = branchRepository;
    }


    public void writeCommentToBranch(String content, Long branchId ,Principal connectedUser){

        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(() -> new BranchIsNotFoundException("Filial tapılmadı"));
        var user = UserUtil.getConnectedUser(connectedUser);

        BranchComment branchComment = BranchComment.builder()
                .content(content)
                .user(user)
                .branch(branch).build();

        branchCommentRepository.save(branchComment);
    }


    public boolean deleteComment(Long commentId){
        BranchComment comment = branchCommentRepository.findByIdAndActiveIsTrue(commentId)
                .orElseThrow(()-> new CommentIsNotFoundException(ExceptionTexts.COMMENT_NOT_FOUND));

        comment.setActive(false);
        branchCommentRepository.save(comment);
        return true;
    }





    /*
    * -- Inserting countries
INSERT INTO countries (name, common_info_about_country) VALUES ('Azerbaijan', 'Info about Azerbaijan');
INSERT INTO countries (name, common_info_about_country) VALUES ('Turkey', 'Info about Turkey');
INSERT INTO countries (name, common_info_about_country) VALUES ('Georgia', 'Info about Georgia');
INSERT INTO countries (name, common_info_about_country) VALUES ('Russia', 'Info about Russia');

-- Inserting cities for Azerbaijan
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Baku', 'Info about Baku', 1);
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Lenkeran', 'Info about Baku', 1);
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Gence', 'Info about Baku', 1);
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Shamaxi', 'Info about Baku', 1);-- Assuming 1 is the ID of Azerbaijan

-- Inserting cities for Turkey
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Ankara', 'Info about Ankara', 2); -- Assuming 2 is the ID of Turkey
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Istanbul', 'Info about Istanbul', 2); -- Assuming 2 is the ID of Turkey

-- Inserting cities for Georgia
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Tbilisi', 'Info about Tbilisi', 3); -- Assuming 3 is the ID of Georgia

-- Inserting cities for Russia
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Moscow', 'Info about Moscow', 4); -- Assuming 4 is the ID of Russia
INSERT INTO cities (name, common_info_about_city, country_id) VALUES ('Saint Petersburg', 'Info about Saint Petersburg', 4); -- Assuming 4 is the ID of Russia
-- Add more cities as needed
    *
    *
    *
    * */
}
