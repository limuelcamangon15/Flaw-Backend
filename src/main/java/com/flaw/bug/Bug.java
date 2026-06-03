package com.flaw.bug;

import com.flaw.team.Team;
import com.flaw.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Entity
@Table(name = "bugs")
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugStatus status = BugStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asignee_id")
    private User asignee;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();

}
