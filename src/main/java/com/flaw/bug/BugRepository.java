package com.flaw.bug;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BugRepository extends JpaRepository<Bug, Long> {
    List<Bug> findByTeamId(Long teamId);
    List<Bug> findByTeamIdAndStatus(Long teamId, BugStatus status);
    List<Bug> findByTeamIdAndCategory(Long teamId, BugCategory category);
    List<Bug> findByAssigneeId(Long assigneeId);
}
