package com.flaw.bug;

import com.flaw.auth.AuthUtil;
import com.flaw.team.Team;
import com.flaw.team.TeamRepository;
import com.flaw.user.User;
import com.flaw.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Assign bug to a developer
    public BugResponse assignBug(Long bugId, Long userId){
        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(()-> new RuntimeException("Bug not found"));

        User assignee = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        bug.setAssignee(assignee);

        bugRepository.save(bug);

        return BugResponse.from(bug);
    }

    // Get one bug
    public BugResponse getBugById(Long id){
        Bug bug = bugRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Bug not found"));

        return BugResponse.from(bug);
    }

    // Get bugs assigned to current user
    public List<BugResponse> getMyBugs(){
        User currentUser = authUtil.getCurrentUser();

        return bugRepository.findByAssigneeId(currentUser.getId())
                .stream()
                .map(BugResponse::from)
                .toList();
    }

    // Get all bugs for a team (with optional params filters)
    public List<BugResponse> getBugsByTeam(Long teamId, BugStatus status, BugCategory category){
        List<Bug> bugs;

        if(status != null){
            bugs = bugRepository.findByTeamIdAndStatus(teamId, status);
        }
        else if(category != null){
            bugs = bugRepository.findByTeamIdAndCategory(teamId, category);
        }
        else{
            bugs = bugRepository.findByTeamId(teamId);
        }

        return bugs.stream().map(BugResponse::from).toList();
    }
}
