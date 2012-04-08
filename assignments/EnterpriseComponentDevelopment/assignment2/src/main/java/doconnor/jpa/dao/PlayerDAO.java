package doconnor.jpa.dao;

import java.util.List;

import doconnor.jpa.domain.Player;

public interface PlayerDAO {

	public abstract void remove(Player player);

	public abstract Player getPlayer(long playerId);

	public abstract List<Player> getPlayers();

	public Player getPlayerAndClub(long playerId);

	public void save(Player player);

	public abstract Player reattach(Player player);

}