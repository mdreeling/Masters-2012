package doconnor.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Club {
	@Id
	@GeneratedValue
	private long id ;
	private String name ;
	@Embedded
	private Stadium stadium ;
	@OneToMany(mappedBy="club", fetch = FetchType.LAZY)
	private Set<Player> players = new HashSet<Player>();
	@OneToMany(mappedBy="club", cascade = { CascadeType.REMOVE },
			fetch = FetchType.LAZY)
	private Set<Sponsorship> sponsorships = new HashSet<Sponsorship>() ;
	@ManyToMany( fetch = FetchType.LAZY)
	private Set<Agent> agents = new HashSet<Agent>() ;
	@OneToOne(mappedBy = "club")
	private ClubStats clubstats;


	public Club() {
		super();
	}
	public Club(String name) {
		super();
		this.name = name;
	}

	public Club( String name, Stadium stadium) {
		this.name = name;
		this.stadium = stadium;
	}
	public void deregister () {
        for (Player player : getPlayers()) {
        	player.setClub(null) ;
        }
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
	public Set<Agent> getAgents() {
		return agents;
	}

	/**
	 * @return the clubstats
	 */
	public ClubStats getClubstats() {
		return clubstats;
	}

	public long getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public Set<Sponsorship> getSponsorships() {
		return sponsorships;
	}

	public Stadium getStadium() {
		return stadium;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	public void setAgents(Set<Agent> agents) {
		this.agents = agents;
	}
	/**
	 * @param clubstats the clubstats to set
	 */
	public void setClubstats(ClubStats clubstats) {
		this.clubstats = clubstats;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPlayers(Set<Player> players) {
		this.players = players;
	}
	public void setSponsorships(Set<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}
	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}
}
