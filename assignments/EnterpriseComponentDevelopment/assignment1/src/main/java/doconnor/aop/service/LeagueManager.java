package doconnor.aop.service;

import doconnor.aop.domain.Club;
import doconnor.aop.domain.Division;
import doconnor.aop.domain.Player;

public interface LeagueManager {
	public void signPlayer(long clubId, Player player);
	public  void addClub(Club club, long divisionId) ;
	public void setupDivision(Division division);
	
}
