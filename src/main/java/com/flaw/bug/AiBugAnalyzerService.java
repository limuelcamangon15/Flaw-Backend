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
        
            Rules:
            - severity must be exactly one of: Low, Medium, High, Critical
            - labels must be short readable tags, not slugs. Example: "Mobile Safari", "Authentication", "Input Validation"
            - possibleRootCauses must be full readable sentences. Example: "Special characters may not be URL-encoded before sending to the server."
            - suggestedNextSteps must be full actionable sentences. Example: "Test the login endpoint directly using Postman with special character passwords."
            - Each list should have 2 to 4 items maximum.
        
            Use this JSON structure as SAMPLE STRUCTURE ONLY:
            {
              "severity": "High",
              "labels": ["Authentication", "Mobile", "Input Validation"],
              "possibleRootCauses": ["Special characters are not being sanitized before processing.", "The mobile client may not be encoding the request body correctly."],
              "suggestedNextSteps": ["Test the endpoint directly with special character inputs.", "Review input sanitization on the auth service.", "Check mobile client request encoding."]
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
