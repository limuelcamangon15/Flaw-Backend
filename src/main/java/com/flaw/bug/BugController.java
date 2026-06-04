package com.flaw.bug;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bugs/")
public class BugController {

    private final BugService bugService;

    public BugController(BugService bugService) {
        this.bugService = bugService;
    }

    @PostMapping
    public ResponseEntity<BugResponse> createBug(@Valid @RequestBody BugRequest request){
        return ResponseEntity.ok(bugService.createBug(request));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<BugResponse>> getBugsByTeam(
            @PathVariable Long teamId,
            @RequestParam(required = false) BugStatus status,
            @RequestParam(required = false) BugCategory category){

        return ResponseEntity.ok(bugService.getBugsByTeam(teamId, status, category));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BugResponse> getBugById(@PathVariable Long id){
        return ResponseEntity.ok(bugService.getBugById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BugResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam  BugStatus status){
        return ResponseEntity.ok(bugService.updateStatus(id, status));
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<BugResponse> assignBug(
            @PathVariable Long id,
            @RequestParam Long userId){
        return ResponseEntity.ok(bugService.assignBug(id, userId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<BugResponse>> getMyBugs(){
        return ResponseEntity.ok(bugService.getMyBugs());
    }
}
