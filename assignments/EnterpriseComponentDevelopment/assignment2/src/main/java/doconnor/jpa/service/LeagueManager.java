package doconnor.jpa.service;

import java.util.List;
import java.util.Set;

import doconnor.jpa.domain.Agent;
import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.Company;
import doconnor.jpa.domain.Contract;
import doconnor.jpa.domain.Division;
import doconnor.jpa.domain.Player;
import doconnor.jpa.domain.Result;
import doconnor.jpa.domain.Sponsorship;

public interface LeagueManager {
	public  List<Club>  readClubs();
	public List<Club> listClubs() ;
	public Set<Player> readClubPlayers(Club club) ;
	public void signPlayer(Player player, Club club);
	public void movePlayer(Player player, Club club);
	public List<Player> readPlayers();
	public List<Division> readDivisions();
	public void addClub(Club club, Division division);
	public void removeClub(Club club);
	public Set<Club> listDivision(Division division);
	public void removePlayer(Player player);
	public void removeDivision(Division division);
	public void addCompany(Company c);
	public List<Company> readCompanies();
	public void addSponsor(Sponsorship s);
	public void addAgent(Agent agent);
	public List<Agent> readAgents();
	public void givePlayerAgent(Contract c);
	public void addClubAgent(Club club, Agent agent);
	public void addResult(Result res);
}
