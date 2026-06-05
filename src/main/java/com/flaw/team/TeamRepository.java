package com.flaw.team;

import com.flaw.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findByMembersContaining(User user);
}
