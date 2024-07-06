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
    public String startNewMatch (@RequestParam (name = "error", required = false) String error, Model model){

        if (error.equals("true"))
            model.addAttribute("errorMessage", "Player's names should be unique");

        return "/new-match";
    }


    @PostMapping("/new-match")
    public String startNewMatch (@RequestParam String player1Name,
                                 @RequestParam String player2Name) {

        if (player1Name.equals(player2Name))
            return "redirect:/new-match?error=true";

        Match newMatch = matchService.createNewMatch(player1Name, player2Name);
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
                              @RequestParam int scoredBallPlayerId, Model model) {

        Match currentMatch = matches.get(Integer.parseInt(uuid));
        scoreService.keepScoreAfterGoal(scoredBallPlayerId, currentMatch);

        if (currentMatch.getWinner() != null) {
            model.addAttribute("player1Name", currentMatch.getPlayer1().getName());
            model.addAttribute("player2Name", currentMatch.getPlayer2().getName());
            model.addAttribute("player1Sets", currentMatch.getPlayer1Score().getSet());
            model.addAttribute("player2Sets", currentMatch.getPlayer2Score().getSet());
            model.addAttribute("winnerName", currentMatch.getWinner().getName());

            matches.remove(Integer.parseInt(uuid));
            return "/final-score";
        }
        return "redirect:/match-score/uuid={uuid}";
    }

    @GetMapping("/matches")
    public String allMatches (Model model,
                              @RequestParam(value = "filter_by_player_name", required = false) String playerName,
                              @RequestParam(value = "page", required = false) Integer page) {
        if (playerName == null || page == null)
            model.addAttribute("matches", matchService.findAll());
        else
            model.addAttribute("matches", matchService.findByPagination(page, playerName));

        return "/matches";
    }


    //TODO
    //сделать миграционную базу данных
    //нет деплоя







}
