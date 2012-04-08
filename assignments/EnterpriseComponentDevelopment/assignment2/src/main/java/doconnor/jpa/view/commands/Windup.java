package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Division;
import doconnor.jpa.service.LeagueManager;

@Component
public class Windup extends AbstractCommand implements Command {
	@Autowired
	public Windup(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
    	Division division = getDivision();
    	leagueService.removeDivision(division);
	}

	public String help() {
		return "delete a division (including its clubs";
	}

}
