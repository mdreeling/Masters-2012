package doconnor.aop.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.service.LeagueManager;
@Component
public class Windup extends AbstractCommand implements Command {
	@Autowired
	public Windup(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
    	Long divisionId = getDivisionNo();
    	leagueService.removeDivision(divisionId);
	}

	public String help() {
		return "delete a division (including its clubs";
	}

}
