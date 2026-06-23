package com.flaw.bug;

import com.flaw.auth.AuthUtil;
import com.flaw.bug.dto.BugRequest;
import com.flaw.bug.dto.BugResponse;
import com.flaw.team.Team;
import com.flaw.team.TeamRepository;
import com.flaw.user.Role;
import com.flaw.user.User;
import com.flaw.user.UserRepository;
import com.flaw.utils.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugService {

    private final BugRepository bugRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final AiBugAnalyzerService aiBugAnalyzerService;

    public BugService(BugRepository bugRepository, TeamRepository teamRepository, UserRepository userRepository, AuthUtil authUtil, AiBugAnalyzerService aiBugAnalyzerService) {
        this.bugRepository = bugRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.aiBugAnalyzerService = aiBugAnalyzerService;
    }

    // Create bug
    public BugResponse createBug(BugRequest request){
        User reporter = authUtil.getCurrentUser();

        Team team  = teamRepository.findById(request.teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        // Check if the reporter is a member of team
        boolean isMember = team.getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(reporter.getId()));

        if(!isMember){
            throw new IllegalArgumentException("You can only report bugs in teams you belong to");
        }

        Bug bug = new Bug();

        bug.setTitle(request.title);
        bug.setDescription(request.description);
        bug.setCategory(request.category);
        bug.setStatus(BugStatus.OPEN);
        bug.setTeam(team);
        bug.setReporter(reporter);

        if(request.assigneeId != null){
            User assignee = userRepository.findById(request.assigneeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

            bug.setAssignee(assignee);
        }

        // save to database
        Bug savedBug = bugRepository.save(bug);

        BugAnalysis analysis = aiBugAnalyzerService.analyze(savedBug);
        savedBug.setAnalysis(analysis);

        return BugResponse.from(savedBug);
    }

    // Update status
    public BugResponse updateStatus(Long id, BugStatus newStatus){
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bug not found"));
        bug.setStatus(newStatus);

        // save
        bugRepository.save(bug);

        return BugResponse.from(bug);
    }

    // Assign bug to a developer
    public BugResponse assignBug(Long bugId, Long userId){
        User currentUser = authUtil.getCurrentUser();

        Bug bug = bugRepository.findById(bugId)
                .orElseThrow(()-> new ResourceNotFoundException("Bug not found"));

        User assignee = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        // Check if QA and if self assigning the bug
        boolean isQA = currentUser.getRole() == Role.QA;
        boolean isSelfAssign = currentUser.getId().equals(assignee.getId());

        if(!isQA && !isSelfAssign){
            throw new IllegalArgumentException("Developers can only self-assign bugs");
        }

        // Check if the assignee is a member of the team
        boolean isTeamMember = bug.getTeam()
                .getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(assignee.getId()));

        if(!isTeamMember){
            throw new IllegalArgumentException("Assignee must be a member of the bug's team");
        }

        bug.setAssignee(assignee);
        // save
        bugRepository.save(bug);

        return BugResponse.from(bug);
    }

    // Get one bug
    public BugResponse getBugById(Long id){
        Bug bug = bugRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Bug not found"));

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
