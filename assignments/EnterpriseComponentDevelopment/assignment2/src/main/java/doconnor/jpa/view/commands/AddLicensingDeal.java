package doconnor.jpa.view.commands;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.jpa.domain.LicensingDeal;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Product;
import doconnor.jpa.service.LeagueManager;

@Component
public class AddLicensingDeal extends AbstractCommand implements Command {
	@Autowired
	public AddLicensingDeal(LeagueManager leagueService, Scanner scanner) {
		super(leagueService, scanner);
		}

	@Override
	public void execute() {
		Product product = getProduct();
		Player player = getPlayer();
	    System.out.print("Duration (years)? ");
	    int duration = scanner.nextInt();
	    System.out.print("Value (millions)? ");
	    float value = scanner.nextFloat();
		LicensingDeal s = new LicensingDeal(product, player, value,
				duration);
		leagueService.addLicensingDeal(s);
	}

	@Override
	public String help() {
		return "setup a player licensing deal with a company";
	}

}
