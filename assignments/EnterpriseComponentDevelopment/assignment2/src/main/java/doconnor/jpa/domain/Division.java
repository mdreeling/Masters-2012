package doconnor.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Division {
	@Id
	@GeneratedValue			
	private long id ;
	private String name ;
	@OneToMany( cascade = { CascadeType.PERSIST,  CascadeType.REMOVE },
			      fetch = FetchType.LAZY)	
	@JoinColumn(name="div", nullable = true)		
	Set<Club> members = new HashSet<Club>() ;

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

	public Set<Club> getMembers() {
		return members;
	}

	public void setMembers(Set<Club> members) {
		this.members = members;
	}
	public Division() {
		super();
	}
	
	public void terminate() {
		for (Club club : getMembers() )  {
			club.deregister() ;
		}
		
	}
	
}
