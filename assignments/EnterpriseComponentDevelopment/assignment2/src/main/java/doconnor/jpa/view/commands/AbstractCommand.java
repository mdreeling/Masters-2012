package doconnor.jpa.view.commands;

import java.util.List;
import java.util.Scanner;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Division;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Product;
import doconnor.jpa.service.LeagueManager;

public abstract class AbstractCommand {
	protected LeagueManager leagueService ;

    protected Scanner scanner;

    public AbstractCommand(LeagueManager leagueService, Scanner scanner) {
		super();
		this.leagueService = leagueService;
		this.scanner = scanner;
	}

	public Division getDivision() {
		List<Division> divisions = leagueService.readDivisions();
		int selection = 0 ;
		int i = 1 ;
		for (Division d : divisions) {
			System.out.println(i++ + " " +d.getName()) ;
		}
		do {
		    System.out.print("Which Division ?");
			selection = scanner.nextInt();
		} while ( (selection  < 0) || (selection > divisions.size()) );
		return divisions.get(--selection) ;
	}

	public Club getClub() {
		List<Club> clubs = leagueService.readClubs();
		int selection = 0 ;
		showClubs(clubs) ;
		do {
		    System.out.print("Which Club ?");
			selection = scanner.nextInt();
		} while ( (selection  < 0) || (selection > clubs.size()) );
		return clubs.get(--selection) ;
	}

	public Company getCompany() {
		List<Company> companies = leagueService.readCompanies();
		int selection = 0 ;
		showCompanies(companies) ;
		do {
		    System.out.print("Which Company ? ");
			selection = scanner.nextInt();
		} while ( (selection  < 0) || (selection > companies.size()) );
		return companies.get(--selection) ;
	}


	private void showCompanies(List<Company> companies) {
		int i = 1 ;
		for (Company c : companies) {
			System.out.println(i++ + " " +c.getName()) ;
		}
	}

	public Player getPlayer() {
		Player result = null ;
		boolean finished = false ;
		List<Player> players = leagueService.readPlayers();
		for (Player p : players) {
		    	System.out.println(p.getId() + "  " + p.getName());
		}
		do {
		    System.out.print("Player ID?");
			Long playerId = scanner.nextLong();
		    for (Player p : players) {
		    	if (playerId == p.getId()) {
		    		result = p ;
		    		finished = true ;
		    	}
		    }
		} while ( !finished );
		return result ;
	}

	public Agent getAgent() {
		Agent result = null ;
		boolean finished = false ;
		List<Agent> agents = leagueService.readAgents();
		for (Agent p : agents) {
		    	System.out.println(p.getId() + "  " + p.getName());
		}
		do {
		    System.out.print("Which one? ");
			Long agentId = scanner.nextLong();
		    for (Agent p : agents) {
		    	if (agentId == p.getId()) {
		    		result = p ;
		    		finished = true ;
		    	}
		    }
		} while ( !finished );
		return result ;
	}

	/**
	 * @author Michael Dreeling
	 *
	 *         New method to return products
	 *
	 * @return
	 */
	public Product getProduct() {
		Product result = null;
		boolean finished = false;
		List<Product> prds = leagueService.readProducts();

		for (Product p : prds) {

			System.out.println(p.getId() + ". " + p.getName() + " ("
					+ p.getCompany().getName()
					+ ")");
		}
		do {
			System.out.print("Which product is being licensed? ");
			Long pId = scanner.nextLong();
			for (Product p : prds) {
				if (pId == p.getId()) {
					result = p;
					finished = true;
				}
			}
		} while (!finished);
		return result;
	}

	public void showClubs(List<Club> clubs) {
		int i = 1 ;
		for (Club c : clubs) {
			System.out.println(i++ + " " +c.getName()) ;
		}
	}

	public void showPlayers(List<Player> players) {
		int i = 1 ;
		for (Player p : players) {
			System.out.println(i++ + " " + p.getName()) ;
		}
	}

}
