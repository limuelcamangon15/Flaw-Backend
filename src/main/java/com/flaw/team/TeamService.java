package com.flaw.team;

import com.flaw.auth.AuthUtil;
import com.flaw.user.User;
import com.flaw.user.UserRepository;

public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository, AuthUtil authUtil) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.authUtil = authUtil;
    }

    // Create team
    public TeamResponse createTeam(TeamRequest request){
        User currentUser = authUtil.getCurrentUser();

        Team team = new Team();
        team.setName(request.name);
        team.setCreatedBy(currentUser);
        team.getMembers().add(currentUser);

        // save
        teamRepository.save(team);

        return TeamResponse.from(team);
    }

    // Add team member
    public TeamResponse addMember(Long teamId, Long userId){
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found."));

        User newMember = userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found."));

        if(team.getMembers().contains(newMember)){
            throw new RuntimeException("User is already a member");
        }

        team.getMembers().add(newMember);

        // save
        teamRepository.save(team);

        return TeamResponse.from(team);
    }
}
