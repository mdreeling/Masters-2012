package doconnor.aop.view.commands;

import java.util.Scanner;

import doconnor.aop.service.LeagueManager;

public abstract class AbstractCommand {
	
	protected LeagueManager leagueService ;
    
    protected Scanner scanner;

    public AbstractCommand(LeagueManager leagueService, Scanner scanner) {
		super();
		this.leagueService = leagueService;
		this.scanner = scanner;
	}
    
    public long getClubNo() {
	    System.out.print("Club no?");
	    return scanner.nextLong();
    }
    
    public long getDivisionNo() {
	    System.out.print("Division no?");
	    return scanner.nextLong();
    }
    
    public long getPlayerNo() {
	    System.out.print("Player no?");
	    return scanner.nextLong();
    }
    
    public long getCompanyNo() {
	    System.out.print("Company no?");
	    return scanner.nextLong();
    }


}
