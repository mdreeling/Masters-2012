package doconnor.jpa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import doconnor.jpa.annotations.meta.LMReadOnly;
import doconnor.jpa.annotations.meta.LMWrite;
import doconnor.jpa.dao.AgentDAO;
import doconnor.jpa.dao.ClubDAO;
import doconnor.jpa.dao.ClubStatsDAO;
import doconnor.jpa.dao.CompanyDAO;
import doconnor.jpa.dao.DivisionDAO;
import doconnor.jpa.dao.LicensingDAO;
import doconnor.jpa.dao.PlayerDAO;
import doconnor.jpa.dao.ProductDAO;
import doconnor.jpa.dao.ResultDAO;
import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.ClubStats;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Contract;
import doconnor.jpa.domain.Division;
import doconnor.jpa.domain.LicensingDeal;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Product;
import doconnor.jpa.domain.Result;
import doconnor.jpa.domain.Sponsorship;

@Service
public class LeagueManagerImpl implements LeagueManager {

	private PlayerDAO playerDAO;
	private ClubDAO clubDAO;
	private DivisionDAO divisionDAO;
	private CompanyDAO companyDAO;
	private LicensingDAO licensingDAO;
	private AgentDAO agentDAO;
	private ResultDAO resultDAO;
	private ProductDAO productDAO;
	private ClubStatsDAO clubStatsDAO;

	public LeagueManagerImpl() {
	}

	@Autowired
	public LeagueManagerImpl(PlayerDAO playerDAO, ClubDAO clubDAO,
			DivisionDAO divisionDAO, CompanyDAO companyDAO, AgentDAO agentDAO,
			ResultDAO resultDAO, LicensingDAO licensingDAO,ProductDAO productDAO, ClubStatsDAO clubStatsDAO) {
		super();
		this.playerDAO = playerDAO;
		this.clubDAO = clubDAO;
		this.divisionDAO = divisionDAO;
		this.companyDAO = companyDAO;
		this.agentDAO = agentDAO;
		this.resultDAO = resultDAO;
		this.licensingDAO = licensingDAO;
		this.productDAO = productDAO;
		this.setClubStatsDAO(clubStatsDAO);
	}


	@LMWrite
	public void addAgent(Agent agent) {
		agentDAO.save(agent);
	}


	@LMWrite
	public void addClub(Club club, Division division) {
		Division divisionM = divisionDAO.reattach(division);
		divisionM.getMembers().add(club);
	}

	@LMWrite
	public void addClubAgent(Club club, Agent agent) {
		Club c = clubDAO.reattach(club);
		if (!checkForAgent(c, agent)) {
			c.getAgents().add(agent);
		}
	}

	@LMWrite
	public void addClubStats(ClubStats l) {
		clubStatsDAO.save(l);
	}


	@LMWrite
	public void addCompany(Company c) {
		companyDAO.save(c);
	}


	@LMWrite
	public void addLicensingDeal(LicensingDeal l) {
		System.out.println("Adding licensing deal for player "
				+ l.getPlayer().getId() + " and product "
				+ l.getProduct().getName() + " for " + l.getDuration()
				+ " years @ " + l.getValue() + " million");

		licensingDAO.save(l);
	}


	@LMWrite
	public void addResult(Result res) {
		resultDAO.save(res);
	}


	@LMWrite
	public void addSponsor(Sponsorship s) {
		companyDAO.save(s);
	}


	private boolean checkForAgent(Club c, Agent a) {
		Set<Long> agentIds = new HashSet<Long>();
		for (Player p : c.getPlayers()) {
			if (p.getContract() != null) {
				agentIds.add(p.getContract().getAgent().getId());
			}
		}
		if (agentIds.contains(a.getId())) {
			return true;
		}
		return false;
	}


	@LMWrite
	public void createProduct(Product pr, Company com) {
		pr.setCompany(com);
		productDAO.save(pr);
	}


	/**
	 * @return the clubStatsDAO
	 */
	public ClubStatsDAO getClubStatsDAO() {
		return clubStatsDAO;
	}


	@LMWrite
	public void givePlayerAgent(Contract c) {
		agentDAO.save(c);
	}


	@LMReadOnly
	public List<Club> listClubs() {
		return clubDAO.getClubs();
	}

	@LMReadOnly
	public Set<Club> listDivision(Division division) {
		Division divisionM = divisionDAO.reattach(division);
		for (Club club : divisionM.getMembers()) {
			// Force lazy loading of players in club
			club.getPlayers().size();
		}
		return divisionM.getMembers();
	}

	@LMWrite
	public void movePlayer(Player player, Club newClub) {
		Player playerM = playerDAO.reattach(player);
		// Club clubM = clubDAO.reattach(newClub) ;
		playerM.setClub(newClub);
	}

	@LMReadOnly
	public List<Agent> readAgents() {
		return agentDAO.getAll();
	}

	@LMReadOnly
	public Set<Player> readClubPlayers(Club club) {
		Club clubM = clubDAO.reattach(club);
		clubM.getPlayers().size();
		return clubM.getPlayers();
	}

	@LMReadOnly
	public List<Club> readClubs() {
		return clubDAO.getClubs();
	}

	@LMReadOnly
	public List<ClubStats> readClubStats() {
		return clubStatsDAO.getClubStats();
	}

	@LMReadOnly
	public List<Company> readCompanies() {
		return companyDAO.getAll();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_UNCOMMITTED, timeout = 5)
	public List<Division> readDivisions() {
		return divisionDAO.getDivisions();
	}

	@LMReadOnly
	public List<LicensingDeal> readLicensingDeals() {
		return licensingDAO.getAll();
	}

	@LMReadOnly
	public List<Player> readPlayers() {
		return playerDAO.getPlayers();
	}

	@LMReadOnly
	public List<Product> readProducts() {
		return productDAO.getProducts();
	}

	@LMReadOnly
	public List<Result> readResults() {
		return resultDAO.getAll();
	}

	@LMWrite
	public void removeClub(Club club) {
		Club clubM = clubDAO.reattach(club);
		clubM.deregister();
		clubDAO.remove(clubM);
	}


	@LMWrite
	public void removeDivision(Division division) {
		Division divisionM = divisionDAO.reattach(division);
		for (Club club : divisionM.getMembers()) {
			club.deregister();
		}
		divisionDAO.remove(divisionM);
	}

	@LMWrite
	public void removePlayer(Player player) {
		Player playerM = playerDAO.reattach(player);
		playerDAO.remove(playerM);
	}

	@LMWrite
	public void signPlayer(Player player, Club club) {
		player.setClub(club);
		playerDAO.save(player);
	}

	@LMWrite
	public void removeResult(Result res) {
		Result ress = resultDAO.reattach(res);
		resultDAO.remove(ress);
	}

	@LMWrite
	public void removeProduct(Product product) {
		Product ress = productDAO.reattach(product);
		productDAO.remove(ress);

	}

	@LMWrite
	public void removeClubStats(ClubStats s) {
		ClubStats ress = clubStatsDAO.reattach(s);
		clubStatsDAO.remove(ress);

	}

	@LMWrite
	public void removeLicensingDeal(LicensingDeal s) {
		LicensingDeal ress = licensingDAO.reattach(s);
		licensingDAO.remove(ress);

	}

	public Product reattachProduct(Product product) {
		return productDAO.reattach(product);
	}

	/**
	 * @param clubStatsDAO
	 *            the clubStatsDAO to set
	 */
	public void setClubStatsDAO(ClubStatsDAO clubStatsDAO) {
		this.clubStatsDAO = clubStatsDAO;
	}
}
