package doconnor.aop.domain;

import java.util.HashSet;
import java.util.Set;

public class Division {
	private long id ;
	private String name ;
	Set<Club> members = new HashSet<Club>() ;

	public Division(String name) {
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
