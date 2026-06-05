package com.flaw.team;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService){
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasRole('QA')")
    public ResponseEntity<TeamResponse> createTeam(@Valid @RequestBody TeamRequest request){
        return ResponseEntity.ok(teamService.createTeam(request));
    }

    @PostMapping("/{teamId}/members/{userId}")
    @PreAuthorize("hasRole('QA')")
    public ResponseEntity<TeamResponse> addMember(@PathVariable Long teamId, @PathVariable Long userId){
        return ResponseEntity.ok(teamService.addMember(teamId, userId));
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<TeamResponse>> getMyTeams(){
        return ResponseEntity.ok(teamService.getMyTeams());
    }
}
