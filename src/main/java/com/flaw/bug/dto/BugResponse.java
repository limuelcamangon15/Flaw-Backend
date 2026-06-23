package com.flaw.bug.dto;

import com.flaw.bug.Bug;
import com.flaw.bug.BugCategory;
import com.flaw.bug.BugStatus;

import java.time.LocalDateTime;

public class BugResponse {

    public Long id;
    public String title;
    public String description;
    public BugStatus status;
    public BugCategory category;
    public String reporterName;
    public String assigneeName;
    public Long teamId;
    public String teamName;
    public LocalDateTime createdAt;

    public static BugResponse from(Bug bug){
        BugResponse res = new BugResponse();
        res.id = bug.getId();
        res.title = bug.getTitle();
        res.description = bug.getDescription();
        res.status = bug.getStatus();
        res.category = bug.getCategory();
        res.reporterName = bug.getReporter().getFirstName() + " " + bug.getReporter().getLastName();
        res.assigneeName = bug.getAssignee() != null ? bug.getAssignee().getFirstName() + " " + bug.getAssignee().getLastName() : null;
        res.teamId = bug.getTeam().getId();
        res.teamName = bug.getTeam().getName();
        res.createdAt = bug.getCreatedAt();

        return res;
    }

}
