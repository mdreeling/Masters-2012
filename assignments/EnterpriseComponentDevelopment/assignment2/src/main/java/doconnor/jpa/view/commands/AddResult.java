package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Result;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddResult extends AbstractCommand implements Command {
	@Autowired
	public AddResult(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}


	public void execute() {
		System.out.print("Choose home club");
		Club clubHome = getClub();
		System.out.print("Choose away club");
		Club clubAway = getClub();
		System.out.print("Score? (Format is 'Home-Away')");
		String score = scanner.next();
		String[] scores = score.split("-");
		Result r = new Result(clubHome, clubAway, Integer.parseInt(scores[0]),
				Integer.parseInt(scores[1]));

		leagueService.addResult(r);
	}

	public String help() {
		return "Add a new match result";
	}

}
