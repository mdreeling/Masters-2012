package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.ClubStats;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddClubStat extends AbstractCommand implements Command {
	@Autowired
	public AddClubStat(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
	}

	public void execute() {
		Club club = getClub();
		System.out.print("won?");
		int won = scanner.nextInt();
		System.out.print("lost? ");
		int lost = scanner.nextInt();
		System.out.print("draw?");
		int draw = scanner.nextInt();
		System.out.print("points?");
		int points = scanner.nextInt();
		ClubStats clubst = new ClubStats(club, won, draw, lost, points);
		leagueService.addClubStats(clubst);
	}

	public String help() {
		return "add new club to division";
	}

}
