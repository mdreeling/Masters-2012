package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
@Component
public class Retire extends AbstractCommand implements Command {
	@Autowired
	public Retire(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Long playerId = getPlayerNo();
    	leagueService.removePlayer(playerId);
	}

	public String help() {
		return "retire a player";
	}

}
