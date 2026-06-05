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

        teamRepository.save(team);

        return TeamResponse.from(team);
    }
}
