package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Club;
import doconnor.jpa.domain.ClubStats;

@Repository
public class ClubStatsDAOImpl extends HibernateDAO implements ClubStatsDAO {

	public ClubStatsDAOImpl() {
		super();
	}

	public void remove(ClubStats clubstats) {
		getEntityManager().remove(clubstats);
	}

	public ClubStats getClubStatsByClub(long clubId) {
		return (ClubStats) getEntityManager()
				.createQuery("from ClubStats s WHERE s.club =:clubEntity")
				.setParameter("clubEntity",
						getEntityManager().find(Club.class, clubId))
				.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<ClubStats> getClubStats() {
		return getEntityManager().createQuery("from ClubStats").getResultList();
	}

	public ClubStats reattach(ClubStats clubstats) {
		return getEntityManager().merge(clubstats);
	}

	public void save(ClubStats clubstats) {
		getEntityManager().persist(clubstats);
		getEntityManager().flush(); // Had to add flush here! Junit did not
		// recognise the persist event had occurred
		// yet.
	}

}
