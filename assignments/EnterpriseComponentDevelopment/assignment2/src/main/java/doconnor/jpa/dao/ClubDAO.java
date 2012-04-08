package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Club;

public interface ClubDAO {

	public abstract void remove(Club club);

	public abstract Club getClub(long clubId);

	public abstract List<Club> getClubs();

	public Club reattach(Club club);
}