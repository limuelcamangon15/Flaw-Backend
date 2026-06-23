package com.flaw.bug;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "bug_analysis")
public class BugAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String severity;

    @ElementCollection
    @CollectionTable(name = "bug_analysis_labels", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "label")
    private List<String> labels;

    @ElementCollection
    @CollectionTable(name = "bug_analysis_root_causes", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "root_cause")
    private List<String> possibleRootCauses;

    @ElementCollection
    @CollectionTable(name = "bug_anaylsis_next_steps", joinColumns = @JoinColumn(name = "analysis_id"))
    @Column(name = "next_step")
    private List<String> suggestedNextSteps;

    @OneToOne
    @JoinColumn(name = "bug_id")
    private Bug bug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<String> getPossibleRootCauses() {
        return possibleRootCauses;
    }

    public void setPossibleRootCauses(List<String> possibleRootCauses) {
        this.possibleRootCauses = possibleRootCauses;
    }

    public List<String> getSuggestedNextSteps() {
        return suggestedNextSteps;
    }

    public void setSuggestedNextSteps(List<String> suggestedNextSteps) {
        this.suggestedNextSteps = suggestedNextSteps;
    }

    public Bug getBug() {
        return bug;
    }

    public void setBug(Bug bug) {
        this.bug = bug;
    }
}
