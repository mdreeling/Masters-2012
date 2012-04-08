package doconnor.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Company {
	@Id
	@GeneratedValue			
	private long id ;
	private String name ;
	@OneToMany(mappedBy="company",
			cascade = { CascadeType.REMOVE },
			fetch = FetchType.LAZY) 
	private Set<Sponsorship> sponsorships = new HashSet<Sponsorship>() ;

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
	
	public Company() {}

	public Company(String name) {
		this.name = name;
	}

	public Set<Sponsorship> getSponsorships() {
		return sponsorships;
	}

	public void setSponsorships(Set<Sponsorship> sponsorships) {
		this.sponsorships = sponsorships;
	}

}
