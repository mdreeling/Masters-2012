package doconnor.aop.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.dao.ClubDAOImpl;
import doconnor.aop.dao.CompanyDAOImpl;
import doconnor.aop.dao.DivisionDAOImpl;
import doconnor.aop.dao.HibetnateDAO;
import doconnor.aop.dao.PlayerDAOImpl;
import doconnor.aop.domain.Club;
import doconnor.aop.domain.Company;
import doconnor.aop.domain.Division;
import doconnor.aop.domain.Player;
@Component
public class LeagueManagerImpl implements LeagueManager {
	private PlayerDAOImpl playerDAO ;
	private ClubDAOImpl clubDAO ;
	private DivisionDAOImpl divisionDAO ;
	private CompanyDAOImpl companyDAO ;
    @Autowired
	public LeagueManagerImpl(PlayerDAOImpl playerDAO, ClubDAOImpl clubDAO,
			DivisionDAOImpl divisionDAO, CompanyDAOImpl companyDAO) {
		super();
		this.playerDAO = playerDAO;
		this.clubDAO = clubDAO;
		this.divisionDAO = divisionDAO;
		this.companyDAO = companyDAO;
	}

	public void setupDivision(Division division) {
		divisionDAO.add(division);

	}
	public void signPlayer(long clubId, Player player) {
		playerDAO.add(  player,  clubId);
	}

	public void addClub(Club club, long divisionId) {
		clubDAO.add( club, divisionId);
	}

	public void shutdown() throws SQLException {
		clubDAO.cleanup();
	}

	public  void addSponsor(long clubId, long companyId) {
		clubDAO.addSponsor(clubId, companyId);
	}

	public  void transferPlayer(long player, long newClubId) {
		clubDAO.movePlayer(  player,  newClubId );
	}

	public  void removePlayer(long playerId) {
		playerDAO.remove(playerId);
	}

	public  void removeClub(long clubId) {
		clubDAO.remove(clubId) ;
	}

	public  void removeDivision(long divisionId) {
		divisionDAO.remove(divisionId) ;
	}


	public  Set<Player> listTeammates(long playerId) {
		return playerDAO.teamMates(playerId) ;
	}

	public Set<Club> listDivision(long divisionId) {
		return divisionDAO.listDivision(divisionId) ;
	}

	public List<Company>  listCompanies() {
		return companyDAO.listAll() ;
	}

	public PlayerDAOImpl getPlayerDAO() {
		return playerDAO;
	}

	public void setPlayerDAO(PlayerDAOImpl playerDAO) {
		this.playerDAO = playerDAO;
	}

	public HibetnateDAO getClubDAO() {
		return clubDAO;
	}

	public void setClubDAO(ClubDAOImpl clubDAO) {
		this.clubDAO = clubDAO;
	}

	public DivisionDAOImpl getDivisionDAO() {
		return divisionDAO;
	}

	public void setDivisionDAO(DivisionDAOImpl divisionDAO) {
		this.divisionDAO = divisionDAO;
	}

	public CompanyDAOImpl getCompanyDAO() {
		return companyDAO;
	}

	public void setCompanyDAO(CompanyDAOImpl companyDAO) {
		this.companyDAO = companyDAO;
	}

}
