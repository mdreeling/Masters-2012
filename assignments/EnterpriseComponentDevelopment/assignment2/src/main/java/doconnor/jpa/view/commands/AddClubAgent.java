package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddClubAgent extends AbstractCommand implements Command {

	@Autowired
	public AddClubAgent(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Club club  = getClub() ;
	    doconnor.jpa.domain.Agent agent  = getAgent() ;
	    leagueService.addClubAgent(club,agent);
	}

	public String help() {
		return "Add agent to club";
	}

}
