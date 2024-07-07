package project.scoreboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.scoreboard.model.Player;

import java.util.Optional;

public interface PlayersRepository extends JpaRepository<Player, Integer> {
        Optional<Player> findByName(String name);

}
