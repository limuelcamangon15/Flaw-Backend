package com.flaw.bug;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BugAnalysisRepository extends JpaRepository<BugAnalysis, Long> {
}
