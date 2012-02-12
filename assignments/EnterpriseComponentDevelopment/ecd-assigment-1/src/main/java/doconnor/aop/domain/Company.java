package doconnor.aop.domain;

import java.util.HashSet;
import java.util.Set;

public class Company {
	private long id ;
	private String name ;
	private Set<Club> support = new HashSet<Club>() ;

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
	public Set<Club> getSupport() {
		return support;
	}
	public void setSupport(Set<Club> support) {
		this.support = support;
	}
		
	public Company() {
		super();
	}

	public Company(String name) {
		super();
		this.name = name;
	}

}
