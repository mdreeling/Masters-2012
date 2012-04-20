package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Club;

@Repository
public class ClubDAOImpl extends HibernateDAO implements ClubDAO {

	public ClubDAOImpl() {
		super();
	}

	public void remove(Club club) {
		getEntityManager().remove(club);
	}

	public Club getClub(long clubId) {
		return getEntityManager().find(Club.class, clubId);
	}

	@SuppressWarnings("unchecked")
	public List<Club> getClubs() {
		return getEntityManager().createQuery("from Club").getResultList();
	}

	public Club reattach(Club club) {
		return getEntityManager().merge(club);
	}

	public void save(Club club) {
		getEntityManager().merge(club);
	}

}
