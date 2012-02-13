package doconnor.aop.dao;

import java.util.Set;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;


import doconnor.aop.domain.Club;
import doconnor.aop.domain.Player;
@Repository
public class PlayerDAOImpl extends HibetnateDAO  {
 
	public PlayerDAOImpl( ) {
		super();
	}  

	public void add(Player player, long clubId) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
			Club club = (Club) session.load(Club.class,clubId);
			player.setClub(club) ;
			session.save(player) ;
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
	
	public void remove( long playerId) {
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
			Player player= (Player) session.get(Player.class,playerId);
			session.delete(player) ;
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
	
	public Set<Player>  teamMates(long playerId ) {
		  Set<Player> result = null ;
		Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Player player = (Player) session.get(Player.class, playerId);
            result = player.getClub().getPlayers() ;
            result.size() ;  // force eager fetching
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
	    }	
        return  result;
	}
	
}
