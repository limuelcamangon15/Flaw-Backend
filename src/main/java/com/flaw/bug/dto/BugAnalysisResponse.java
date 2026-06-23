package com.flaw.bug.dto;

import com.flaw.bug.BugAnalysis;

import java.util.List;

public class BugAnalysisResponse {
    public String severity;
    public List<String> labels;
    public List<String> possibleRootCauses;
    public List<String> suggestedNextSteps;

    public static BugAnalysisResponse from(BugAnalysis analysis){
        if (analysis == null) return null;

        BugAnalysisResponse res = new BugAnalysisResponse();
        res.severity = analysis.getSeverity();
        res.labels = analysis.getLabels();
        res.possibleRootCauses = analysis.getPossibleRootCauses();
        res.suggestedNextSteps = analysis.getSuggestedNextSteps();

        return res;
    }
}
