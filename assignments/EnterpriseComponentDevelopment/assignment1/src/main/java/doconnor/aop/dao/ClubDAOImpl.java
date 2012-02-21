package doconnor.aop.dao;

import java.sql.SQLException;
import org.springframework.stereotype.Repository;

import doconnor.aop.domain.Club;
@Repository
public class ClubDAOImpl extends HibetnateDAO {
	
    public ClubDAOImpl() {
		super();
	}

	public void add(Club club, long divisionId) {
	}
	
	public void addSponsor(long clubId, long companyId) {
	}


	public void remove(long clubId ) {
	}	
	
	public void movePlayer(long playerId, long newClubId) {
	}	
	
	public void cleanup() throws SQLException{
	  }	
}
