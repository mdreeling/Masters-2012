package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
@Component
public class Bankrupt extends AbstractCommand implements Command {
	@Autowired
	public Bankrupt(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
	    Long clubId = getClubNo();
    	leagueService.removeClub(clubId) ;
	}

	public String help() {
		return "delete a club";
	}

}
