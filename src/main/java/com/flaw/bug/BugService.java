package com.flaw.bug;

import com.flaw.auth.AuthUtil;
import com.flaw.team.Team;
import com.flaw.team.TeamRepository;
import com.flaw.user.User;
import com.flaw.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public BugService(BugRepository bugRepository, TeamRepository teamRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.bugRepository = bugRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    // Create bug
    public BugResponse createBug(BugRequest request){
        User reporter = authUtil.getCurrentUser();

        Team team  = teamRepository.findById(request.teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Bug bug = new Bug();

        bug.setTitle(request.title);
        bug.setDescription(request.description);
        bug.setCategory(request.category);
        bug.setStatus(BugStatus.OPEN);
        bug.setTeam(team);
        bug.setReporter(reporter);

        if(request.assigneeId != null){
            User assignee = userRepository.findById(request.assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));

            bug.setAssignee(assignee);
        }

        // save to database
        bugRepository.save(bug);

        return BugResponse.from(bug);
    }

    // Update status
    public BugResponse updateStatus(Long id, BugStatus newStatus){
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
        bug.setStatus(newStatus);

        // save
        bugRepository.save(bug);

        return BugResponse.from(bug);
    }


}
