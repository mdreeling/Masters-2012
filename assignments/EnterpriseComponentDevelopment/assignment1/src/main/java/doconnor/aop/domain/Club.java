package doconnor.aop.domain;

import java.util.HashSet;
import java.util.Set;

public class Club {
	private long id ;
	private String name ;
	private Set<Player> players = new HashSet<Player>();
	private Company mainSponser ;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Club() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Club other = (Club) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	public void deregister () {
        getPlayers().clear() ;
	}

	public Club(String name) {
		super();
		this.name = name;
	}
	public Company getMainSponser() {
		return mainSponser;
	}
	public void setMainSponser(Company mainSponser) {
		this.mainSponser = mainSponser;
	}
}
