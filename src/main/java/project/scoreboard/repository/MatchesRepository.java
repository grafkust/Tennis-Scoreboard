package project.scoreboard.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.scoreboard.model.Match;

import java.util.List;

@Repository
public interface MatchesRepository extends JpaRepository<Match, Integer> {

    List <Match> findByPlayer1_NameOrPlayer2_Name (String name1, String name2, PageRequest pageRequest);


}
