package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.ClubStats;

public interface ClubStatsDAO {

	public abstract void remove(ClubStats clubstats);

	public abstract ClubStats getClubStatsByClub(long clubId);

	public abstract List<ClubStats> getClubStats();

	public ClubStats reattach(ClubStats clubstats);

	public abstract void save(ClubStats l);

}