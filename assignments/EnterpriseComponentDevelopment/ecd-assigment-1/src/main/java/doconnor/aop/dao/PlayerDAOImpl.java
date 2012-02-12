package doconnor.aop.dao;

import java.util.Set;
import org.springframework.stereotype.Repository;


import doconnor.aop.domain.Player;
@Repository
public class PlayerDAOImpl extends HibetnateDAO  {
 
	public PlayerDAOImpl( ) {
		super();
	}  

	public void add(Player player, long clubId) {
	}
	
	public void remove( long playerId) {
	}
	
	public Set<Player>  teamMates(long playerId ) {
		Set<Player> result = null ;
        return  result;
	}
	
}
