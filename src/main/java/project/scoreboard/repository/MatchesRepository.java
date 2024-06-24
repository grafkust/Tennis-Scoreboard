package project.scoreboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.scoreboard.model.Match;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Integer> {





}
