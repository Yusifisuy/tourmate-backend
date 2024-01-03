package az.tourmate.services.score;

import az.tourmate.exceptions.branch.BranchAlreadyScoredException;
import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.models.branches.Branch;
import az.tourmate.models.scores.Score;
import az.tourmate.models.user.User;
import az.tourmate.repositories.branch.BranchRepository;
import az.tourmate.repositories.score.ScoreRepository;
import az.tourmate.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final BranchRepository branchRepository;

    public ScoreService(ScoreRepository scoreRepository, BranchRepository branchRepository) {
        this.scoreRepository = scoreRepository;
        this.branchRepository = branchRepository;
    }


    public void giveScore(Long branchId, Principal connectedUser,Double score){
        Branch branch = branchRepository.findByIdAndActiveIsTrue(branchId)
                .orElseThrow(() -> new BranchIsNotFoundException("Filial tapilmadi"));

        User user = UserUtil.getConnectedUser(connectedUser);
        Optional<Score> existingScore = scoreRepository.findByUserAndBranch(user,branch);

        if (existingScore.isEmpty()){
            Score newScore = new Score();
            newScore.setUser(user);
            newScore.setBranch(branch);
            newScore.setScore(score);

            scoreRepository.save(newScore);
        }

        else {
            throw new BranchAlreadyScoredException("Eyni məkana 2 dəfə qiymət verə bilməzsiniz");
        }
    }
}
