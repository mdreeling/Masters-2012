package doconnor.jpa.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import doconnor.jpa.dao.AgentDAO;
import doconnor.jpa.dao.ClubDAO;
import doconnor.jpa.dao.CompanyDAO;
import doconnor.jpa.dao.DivisionDAO;
import doconnor.jpa.dao.PlayerDAO;
import doconnor.jpa.dao.ResultDAO;
import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Contract;
import doconnor.jpa.domain.Division;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Result;
import doconnor.jpa.domain.Sponsorship;

@Service
public class LeagueManagerImpl implements LeagueManager {

	private PlayerDAO playerDAO;
	private ClubDAO clubDAO;
	private DivisionDAO divisionDAO;
	private CompanyDAO companyDAO;
	private AgentDAO agentDAO;
	private ResultDAO resultDAO;

	public LeagueManagerImpl() {
	}

	@Autowired
	public LeagueManagerImpl(PlayerDAO playerDAO, ClubDAO clubDAO,
			DivisionDAO divisionDAO, CompanyDAO companyDAO, AgentDAO agentDAO,
			ResultDAO resultDAO) {
		super();
		this.playerDAO = playerDAO;
		this.clubDAO = clubDAO;
		this.divisionDAO = divisionDAO;
		this.companyDAO = companyDAO;
		this.agentDAO = agentDAO;
		this.resultDAO = resultDAO;
	}

	@Transactional
	public List<Club> readClubs() {
		return clubDAO.getClubs();
	}

	@Transactional
	public List<Club> listClubs() {
		return clubDAO.getClubs();
	}

	@Transactional
	public Set<Player> readClubPlayers(Club club) {
		Club clubM = clubDAO.reattach(club);
		clubM.getPlayers().size();
		return clubM.getPlayers();
	}

	@Transactional
	public void signPlayer(Player player, Club club) {
		player.setClub(club);
		playerDAO.save(player);
	}

	@Transactional
	public void movePlayer(Player player, Club newClub) {
		Player playerM = playerDAO.reattach(player);
		// Club clubM = clubDAO.reattach(newClub) ;
		playerM.setClub(newClub);
	}

	@Transactional
	public List<Player> readPlayers() {
		return playerDAO.getPlayers();
	}

	@Transactional
	public List<Division> readDivisions() {
		return divisionDAO.getDivisions();
	}

	@Transactional
	public void addClub(Club club, Division division) {
		Division divisionM = divisionDAO.reattach(division);
		divisionM.getMembers().add(club);
	}

	@Transactional
	public void removeClub(Club club) {
		Club clubM = clubDAO.reattach(club);
		clubM.deregister();
		clubDAO.remove(clubM);
	}

	@Transactional
	public Set<Club> listDivision(Division division) {
		Division divisionM = divisionDAO.reattach(division);
		for (Club club : divisionM.getMembers()) {
			// Force lazy loading of players in club
			club.getPlayers().size();
		}
		return divisionM.getMembers();
	}

	@Transactional
	public void removePlayer(Player player) {
		Player playerM = playerDAO.reattach(player);
		playerDAO.remove(playerM);
	}

	@Transactional
	public void removeDivision(Division division) {
		Division divisionM = divisionDAO.reattach(division);
		for (Club club : divisionM.getMembers()) {
			club.deregister();
		}
		divisionDAO.remove(divisionM);
	}

	@Transactional
	public void addCompany(Company c) {
		companyDAO.save(c);
	}

	@Transactional
	public List<Company> readCompanies() {
		return companyDAO.getAll();
	}

	@Transactional
	public void addSponsor(Sponsorship s) {
		companyDAO.save(s);
	}

	@Transactional
	public void addAgent(Agent agent) {
		agentDAO.save(agent);
	}

	@Transactional
	public List<Agent> readAgents() {
		return agentDAO.getAll();
	}

	@Transactional
	public void givePlayerAgent(Contract c) {
		agentDAO.save(c);
	}

	@Transactional
	public void addClubAgent(Club club, Agent agent) {
		Club c = clubDAO.reattach(club);
		if (!checkForAgent(c, agent)) {
			c.getAgents().add(agent);
		}
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

	@Transactional
	public void addResult(Result res) {
		resultDAO.save(res);
	}
}
