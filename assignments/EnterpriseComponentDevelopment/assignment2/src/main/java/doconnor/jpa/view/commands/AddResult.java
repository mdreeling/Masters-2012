package doconnor.jpa.view.commands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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


	@Override
	public void execute() {

		System.out.print("Match date? (dd/mm/yyyy)");
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = scanner
				.next("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]");
		if (dateString != null) {
			dateString = dateString.trim();
		}

		if ((dateString == null) || (dateString.length() == 0)) {
			// throw new Exception("Bad dates");
		}

		// Parse the date
		try {
			date = df.parse(dateString);

		} catch (ParseException pe) {
			System.out.println("ERROR: could not parse date in string \""
					+ dateString + "\"");
		}

		System.out.print("Choose home club");
		Club clubHome = getClub();
		System.out.print("Choose away club");
		Club clubAway = getClub();
		System.out.print("Score? (Format is 'Home-Away')");
		String score = scanner.next();
		String[] scores = score.split("-");
		Result r = new Result(date, clubHome, clubAway,
				Integer.parseInt(scores[0]),
				Integer.parseInt(scores[1]));

		leagueService.addResult(r);
	}

	@Override
	public String help() {
		return "Add a new match result";
	}

}
