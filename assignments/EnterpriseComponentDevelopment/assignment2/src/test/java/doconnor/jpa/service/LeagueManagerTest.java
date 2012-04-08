package doconnor.jpa.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.LicensingDeal;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Product;
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

		Result res = new Result(new Date(), clubs.get(0), clubs.get(1), 3, 0);
		leagueManager.addResult(res);
		int res_post = simpleJdbcTemplate
				.queryForInt("SELECT COUNT(0) FROM RESULT");
		assertEquals("Add Result failed", res_post, res_pre + 1);
	}

	@Test
	public void addProduct() {

		Product product = new Product("Gillette Fusion Proglide");
		List<Company> comps = leagueManager.readCompanies();
		List<Product> prods = leagueManager.readProducts();
		int res_pre = prods.size();

		leagueManager.createProduct(product, comps.get(0));
		int res_post = simpleJdbcTemplate
				.queryForInt("SELECT COUNT(0) FROM PRODUCT");
		assertEquals("Add Product failed", res_post, res_pre + 1);
	}

	@Test
	public void addLicensingDeal() {
		List<LicensingDeal> licdeals = leagueManager.readLicensingDeals();
		List<Product> prods = leagueManager.readProducts();
		List<Player> players = leagueManager.readPlayers();
		int res_pre = licdeals.size();

		LicensingDeal s = new LicensingDeal(prods.get(0), players.get(0), 100,
				4);
		leagueManager.addLicensingDeal(s);
		int res_post = simpleJdbcTemplate
				.queryForInt("SELECT COUNT(0) FROM LICENSINGDEAL");
		assertEquals("Add Licensing Deal failed", res_post, res_pre + 1);
	}
}
