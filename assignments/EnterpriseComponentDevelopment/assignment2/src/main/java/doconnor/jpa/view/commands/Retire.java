package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Player;
import doconnor.jpa.service.LeagueManager;

@Component
public class Retire extends AbstractCommand implements Command {
	@Autowired
	public Retire(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Player player = getPlayer();
    	leagueService.removePlayer(player);
	}

	public String help() {
		return "retire a player";
	}

}
