package com.flaw.bug;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiBugAnalyzerService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final BugAnalysisRepository bugAnalysisRepository;

    public AiBugAnalyzerService(ChatClient.Builder builder, ObjectMapper objectMapper, BugAnalysisRepository bugAnalysisRepository){
        this.chatClient = builder.build();
        this.objectMapper = objectMapper;
        this.bugAnalysisRepository = bugAnalysisRepository;
    }

    public BugAnalysis analyze(Bug bug){
        String prompt = """
            You are a senior software engineer analyzing a bug report.
            Analyze the following bug and respond ONLY with a valid JSON object, no explanation, no markdown, no backticks.

            Bug Title: %s
            Bug Description: %s
            Category: %s

            Respond with exactly this JSON structure:
            {
              "severity": "Low | Medium | High | Critical",
              "labels": ["label1", "label2"],
              "possibleRootCauses": ["cause1", "cause2"],
              "suggestedNextSteps": ["step1", "step2"]
            }
            """.formatted(
                bug.getTitle(),
                bug.getDescription(),
                bug.getCategory()
        );

        BugAnalysis analysis = new BugAnalysis();
        analysis.setBug(bug);

        try {
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            String clean = response
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            var map = objectMapper.readValue(clean, java.util.Map.class);

            analysis.setSeverity((String) map.get("severity"));
            analysis.setLabels((List<String>) map.get("labels"));
            analysis.setPossibleRootCauses((List<String>) map.get("possibleRootCauses"));
            analysis.setSuggestedNextSteps((List<String>) map.get("suggestedNextSteps"));
        }
        catch (Exception e){
            analysis.setSeverity("Unknown");
            analysis.setLabels(List.of());
            analysis.setPossibleRootCauses(List.of("AI analysis unavailable"));
            analysis.setSuggestedNextSteps(List.of("Manually review the bug"));
        }

        return bugAnalysisRepository.save(analysis);
    }
}
