package doconnor.aop.dao;

import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import doconnor.aop.domain.Club;
import doconnor.aop.domain.Company;
import doconnor.aop.domain.Division;
import doconnor.aop.domain.Player;
@Repository
public class ClubDAOImpl extends HibetnateDAO {
	
    public ClubDAOImpl() {
		super();
	}

	public void add(Club club, long divisionId) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Division division = (Division) session.get(Division.class, divisionId);
            division.getMembers().add(club) ;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
	    }	
	}

	public void addSponsor(long clubId, long companyId) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Company company = (Company) session.load(Company.class, companyId);
            Club club = (Club) session.get(Club.class, clubId);
            club.addSponsor(company) ;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
	    }	
	}

	public void remove(long clubId ) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Club club = (Club) session.get(Club.class, clubId);
            //club.deregister();
            session.delete(club) ;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
	    }	
	}	
	
	public void movePlayer(long playerId, long newClubId) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Player player = (Player) session.get(Player.class, playerId) ;
            Club newClub = (Club) session.load(Club.class,newClubId);
            player.setClub(newClub) ;
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
	    }	
	}	
	
	public void cleanup() throws SQLException{
	  	Session s = null ;
	      try {
	          s = sessionFactory.openSession(); 
	          s.connection().createStatement().execute("SHUTDOWN") ;
	      } catch (HibernateException e) {
	          throw e;
	      } catch (SQLException e) {
	          throw e;
	      }
	      finally {
	          s.close();
		    }
	  }	
}
