package doconnor.jpa.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import doconnor.jpa.domain.Player;
@Repository
public class PlayerDAOImpl extends HibernateDAO implements PlayerDAO {
		
	    public PlayerDAOImpl( ) {
			super();
		}
	   
	public void remove( Player player) {
    	getEntityManager().remove(player) ;
	}

	public Player getPlayer(long playerId) {
    	return getEntityManager().find(Player.class, playerId);
	}
	
	public Player getPlayerAndClub(long playerId) {
		return (Player) getEntityManager().createQuery(
			"from Player p LEFT JOIN FETCH p.club where p.id = :id").
			setParameter("id", playerId).getSingleResult();
	}
	
	public void save(Player player) {
		getEntityManager().persist(player);
	}
	
	@SuppressWarnings("unchecked")
	public List<Player> getPlayers( ) {
		return getEntityManager().createQuery("from Player").getResultList();
	}

	public Player reattach(Player player) {
		return getEntityManager().merge(player);	
	}
	
}
