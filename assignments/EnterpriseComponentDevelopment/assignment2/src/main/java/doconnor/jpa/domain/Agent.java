package doconnor.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Agent {
	@Id
	@GeneratedValue		
	private long id ;
	private String name ;
	@OneToMany(mappedBy="agent")		
	private Set<Contract> contracts  = new HashSet<Contract>();
	@ManyToMany(mappedBy="agents")		
	private Set<Club> clubss = new HashSet<Club>();

	public Agent() {
		super();
	}
	public Agent(String name) {
		super();
		this.name = name;
	}
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
	public Set<Contract> getContracts() {
		return contracts;
	}
	public void setContracts(Set<Contract> contracts) {
		this.contracts = contracts;
	}
	public Set<Club> getClubss() {
		return clubss;
	}
	public void setClubss(Set<Club> clubss) {
		this.clubss = clubss;
	}

}
