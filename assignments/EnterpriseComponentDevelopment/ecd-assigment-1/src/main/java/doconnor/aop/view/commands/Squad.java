package doconnor.aop.view.commands;

import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.domain.Player;
import doconnor.aop.service.LeagueManager;
@Component
public class Squad extends AbstractCommand implements Command {
	@Autowired
	public Squad(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Long playerId = getPlayerNo();
    	Set<Player> team = leagueService.listTeammates(playerId);
    	for (Player player : team)  {
    	    System.out.println(player.getName());
    	}
	}

	public String help() {
		return "list a player's team-mates";
	}

}
