package doconnor.aop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import doconnor.aop.dao.ClubDAOImpl;
import doconnor.aop.dao.CompanyDAOImpl;
import doconnor.aop.dao.DivisionDAOImpl;
import doconnor.aop.dao.PlayerDAOImpl;
import doconnor.aop.domain.Club;
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

	public void signPlayer(long clubId, Player player) {
		playerDAO.add(  player,  clubId);
	}
	
	public void addClub(Club club, long divisionId) {
		clubDAO.add( club, divisionId);
	}

	public void setupDivision(Division division) {
		divisionDAO.add(division) ;
		
	}
	
}
