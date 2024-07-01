package project.scoreboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.scoreboard.model.Match;
import project.scoreboard.service.MatchService;
import project.scoreboard.service.ScoreService;

import java.util.HashMap;

@Controller
public class MatchController {

    private final MatchService matchService;
    private final ScoreService scoreService;
    private final HashMap<Integer,Match> matches = new HashMap<>();

    public MatchController(MatchService matchService, ScoreService scoreService) {
        this.matchService = matchService;
        this.scoreService = scoreService;
    }

    @GetMapping("/")
    public String mainPage(){
        return "/main";
    }

    @GetMapping("/new-match")
    public String startNewMatch (){
        return "/new-match";
    }

    @PostMapping("/new-match")
    public String startNewMatch (@RequestParam String player1,
                                 @RequestParam String player2) {

        Match newMatch = matchService.createNewMatch(player1, player2);
        int uuid = newMatch.hashCode();
        matches.put(uuid, newMatch);

        return "redirect:/match-score/uuid=" + uuid;
    }

    @GetMapping("/match-score/uuid={uuid}")
    public String matchScore (@PathVariable(name = "uuid") String uuid, Model model){

        Match currentMatch = matches.get(Integer.parseInt(uuid));

        model.addAttribute("player1", currentMatch.getPlayer1());
        model.addAttribute("player2", currentMatch.getPlayer2());
        model.addAttribute("player1Score", currentMatch.getPlayer1Score());
        model.addAttribute("player2Score", currentMatch.getPlayer2Score());
        model.addAttribute("uuid", uuid);

        return "/match-score";
    }

    @PostMapping("/match-score/{uuid}")
    public String matchScore (@PathVariable(name = "uuid") String uuid,
                              @RequestParam int scoredBallPlayerId) {

        Match currentMatch = matches.get(Integer.parseInt(uuid));
        scoreService.keepScore(scoredBallPlayerId, currentMatch);

        if (currentMatch.getWinner() != null) {
            matches.remove(Integer.parseInt(uuid));
            //Вернуть новое представление завершенного матча
            return "redirect:/matches";
        }

        return "redirect:/match-score/uuid={uuid}";
    }


    @GetMapping("/matches")
    public String allMatches (Model model){
        model.addAttribute("matches", matchService.findAll());
        return "/matches";
    }







}
