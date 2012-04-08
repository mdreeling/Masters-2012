package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.service.LeagueManager;


@Component
public class Bankrupt extends AbstractCommand implements Command {
	@Autowired
	public Bankrupt(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Club club = getClub();
    	leagueService.removeClub(club) ;
	}

	public String help() {
		return "delete a club";
	}

}
