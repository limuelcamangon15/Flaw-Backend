package com.flaw.team;

import java.util.List;

public class TeamResponse {
    public Long id;
    public String name;
    public String createdByName;
    public List<String> memberNames;

    public static TeamResponse from(Team team){
        TeamResponse res = new TeamResponse();
        res.id = team.getId();
        res.name = team.getName();
        res.createdByName = team.getCreatedBy().getFirstName() + " " + team.getCreatedBy().getLastName();
        res.memberNames = team.getMembers()
                .stream()
                .map(user -> user.getFirstName() + " " + user.getLastName())
                .toList();

        return res;
    }
}
