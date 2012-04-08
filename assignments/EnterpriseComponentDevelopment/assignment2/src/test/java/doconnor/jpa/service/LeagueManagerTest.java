package doconnor.jpa.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Result;

@ContextConfiguration
public class LeagueManagerTest extends
		AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	protected LeagueManager leagueManager;
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void readClub() {
		List<Club> clubs = leagueManager.readClubs();
		assertEquals("Club listing ", countRowsInTable("CLUB"), clubs.size());
	}

	@Test
	public void signPlayer() {
		Player player = new Player("test");
		List<Club> clubs = leagueManager.readClubs();
		int squad_pre = clubs.get(1).getPlayers().size();
		leagueManager.signPlayer(player, clubs.get(1));
		int squad_post = simpleJdbcTemplate.queryForInt(
				"SELECT COUNT(0) FROM PLAYER WHERE PLAYER.CLUB = ?",
				clubs.get(1).getId());
		assertEquals("Sign player failed", squad_post, squad_pre + 1);
	}

	@Test
	public void addResult() {
		List<Club> clubs = leagueManager.readClubs();
		List<Result> results = leagueManager.readResults();
		int res_pre = results.size();

		Result res = new Result(clubs.get(0), clubs.get(1), 3, 0);
		leagueManager.addResult(res);
		int res_post = simpleJdbcTemplate
				.queryForInt("SELECT COUNT(0) FROM RESULT");
		assertEquals("Add Result failed", res_post, res_pre + 1);
	}
}
