package doconnor.aop.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import doconnor.aop.dao.ClubDAOImpl;
import doconnor.aop.dao.CompanyDAOImpl;
import doconnor.aop.dao.DivisionDAOImpl;
import doconnor.aop.dao.PlayerDAOImpl;
import doconnor.aop.domain.Club;
import doconnor.aop.domain.Company;
import doconnor.aop.domain.Division;
import doconnor.aop.domain.Player;

public interface LeagueManager {
	public void signPlayer(long clubId, Player player);
	public  void addClub(Club club, long divisionId) ;
	public  void addSponsor(long clubId, long companyId) ;
	public void transferPlayer(long player,  long newClubId) ;
	public void removePlayer(long playerId) ;
	public  void removeClub(long clubId) ;
	public void setupDivision(Division division);
	public  void removeDivision(long divisionId) ;
	public  Set<Player> listTeammates(long playerId) ;
	public Set<Club> listDivision(long divisionId) ;
	public List<Company>  listCompanies()  ;
	public void shutdown() throws SQLException ;
	public void setPlayerDAO(PlayerDAOImpl playerDAO);
	public void setClubDAO(ClubDAOImpl clubDAO) ;
	public void setDivisionDAO(DivisionDAOImpl divisionDAO);
	public void setCompanyDAO(CompanyDAOImpl companyDAO);
}
