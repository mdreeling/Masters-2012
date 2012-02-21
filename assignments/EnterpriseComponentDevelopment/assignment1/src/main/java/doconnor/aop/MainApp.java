package doconnor.aop;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import doconnor.aop.domain.Agent;
import doconnor.aop.domain.Club;
import doconnor.aop.domain.Company;
import doconnor.aop.domain.Division;
import doconnor.aop.domain.Player;
import doconnor.aop.service.LeagueManager;

public class MainApp extends TestCase {
	LeagueManager leagueManager;

	public static void main(String[] args) {
		// new MainApp();
	}

	public MainApp() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		leagueManager = (LeagueManager) context.getBean("leagueManagerImpl");
	}

	public void test1() {
		Club club = new Club("testclub1");
		leagueManager.addClub(club, 1);
		club = new Club();
		// Should fail
		leagueManager.addClub(club, 1);
	}

	public void test2() {
		Club club = new Club("testclub1");
		club.setMainSponser(new Company("testCompany1"));
		leagueManager.addClub(club, 1);
		club.setMainSponser(new Company(""));
		// Should fail
		leagueManager.addClub(club, 1);
	}

	public void test3() {
		Player player = new Player("testplayer1");
		leagueManager.signPlayer(1, player);
		player = new Player("");
		// Should fail
		leagueManager.signPlayer(1, player);
		player = new Player("testplayer2");
		player.setAgent(new Agent(""));
		// Should fail
		leagueManager.signPlayer(1, player);
	}

	public void test4() {
		Club club = new Club("club1");
		club.getPlayers().add(new Player("testplayer1"));
		club.getPlayers().add(new Player("testplayer2"));
		leagueManager.addClub(club, 1);
		club = new Club("club2");
		club.getPlayers().add(new Player("testplayer1"));
		club.getPlayers().add(new Player());
		// Should fail
		leagueManager.addClub(club, 1);
	}

	public void test5() {
		Division division = new Division("Division 1");
		Club club = new Club("club1");
		club.getPlayers().add(new Player("testplayer1"));
		division.getMembers().add(club);
		leagueManager.setupDivision(division);
		club.getPlayers().add(new Player(""));
		// Should fail
		leagueManager.setupDivision(division);
	}

	/**
	 * Test that we can validate primitives per:
	 * "2. Non-domain object arguments, e.g. primitives only."
	 */
	public void test6() {
		Club club = new Club("testclub1");
		leagueManager.addClub(club, 1);
		club = new Club("testclub2");
		// Should fail
		leagueManager.addClub(club, -1);
	}
}
